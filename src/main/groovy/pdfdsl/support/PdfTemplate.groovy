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

  PdfTemplate(commands) {
    this.commands = commands
  }

  byte[] create() {
    def dslWriter = new NewPdfWriter()
    def lastPage = 1
    commands.sort {it.lingo.page}.each {command ->
      def page = command.lingo.page
      while (lastPage < page) {
        dslWriter.insertPage()
        lastPage += 1
      }
      command.stampWith(dslWriter)
    }
    dslWriter.bytes()
  }

  byte[] stamp(byte[] original) {
    def dslWriter = new ExistingPdfWriter(original)
    commands.each {command ->
      def page = command.lingo.page
      while (dslWriter.pageCount() < page) {
        dslWriter.insertPage()
      }
      command.stampWith(dslWriter)
    }
    dslWriter.bytes()
  }

}