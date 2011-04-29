/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package pdfdsl.support

class PdfTemplate {
  private def commands
  private def pageCommands
  private def defaultSettings

  PdfTemplate(commands, pageCommands, defaultSettings) {
    this.commands = commands
    this.pageCommands = pageCommands
    this.defaultSettings = defaultSettings
  }

  byte[] create() {
    def dslWriter = new NewPdfWriter()
    def lastPage = 1
    commands.sort {it.page ?: defaultSettings.page}.each {command ->
      lastPage = executeRootCommand(command, dslWriter, lastPage)
    }
    stampPageCommands(dslWriter.bytes())
  }

  byte[] stamp(byte[] original) {
    def dslWriter = new ExistingPdfWriter(original)
    commands.each {command ->
      executeRootCommand(command, dslWriter, dslWriter.pageCount())
    }
    stampPageCommands(dslWriter.bytes())
  }

  byte[] stampPageCommands(byte[] original) {
    if (!pageCommands.headers) return original

    def dslWriter = new ExistingPdfWriter(original)
    dslWriter.pageCount().times { pageIndex ->
      pageCommands.headers?.each {command ->
        command.page = pageIndex + 1
        executeRootCommand(command, dslWriter, dslWriter.pageCount())
      }
    }
    dslWriter.bytes()
  }

  private int executeRootCommand(command, DslWriter dslWriter, int lastPage) {
    def lingo = defaultSettings + command
    lastPage = ensurePageExists(lastPage, dslWriter, lingo)

    execute command, lingo, dslWriter

    if (LastPosition.lastY < 0) {
      throw new RuntimeException("pdf page contents overflow")
    }
    return lastPage
  }

  private void executeChildCommand(parentExecutableCommand, command, DslWriter dslWriter, int index) {
    def lingo = parentExecutableCommand.preChildExecute(command, index)

    execute command, lingo, dslWriter

    parentExecutableCommand.postChildExecute(command, index)
  }

  private void execute(command, lingo, dslWriter) {
    def executableCommand = command.COMMAND_OBJECT.getClass().newInstance()
    executableCommand.lingo = lingo
    executableCommand.stampWith(dslWriter)

    command.CHILDREN.eachWithIndex {child, index ->
      executeChildCommand executableCommand, child, dslWriter, index
    }
    executableCommand.postChildrenExecute()
  }

  private int ensurePageExists(int lastPage, DslWriter dslWriter, lingo) {
    while (lastPage < lingo.page) {
      dslWriter.insertPage()
      lastPage += 1
    }
    return lastPage
  }

}