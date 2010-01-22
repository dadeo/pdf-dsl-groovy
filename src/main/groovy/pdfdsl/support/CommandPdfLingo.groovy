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

import com.lowagie.text.Font
import com.lowagie.text.pdf.BaseFont

class CommandPdfLingo {

  private def commands
  private def defaultSettings
  private def commandDefinitions
  private def commandPath

  CommandPdfLingo(commands, defaults, commandDefinitions, commandPath="/") {
    this.commands = commands
    this.defaultSettings = defaults
    this.commandDefinitions = commandDefinitions
    this.commandPath = commandPath
  }

  def methodMissing(String name, args) {
    def commandDefinition = commandDefinitions[name]
    if (!commandDefinition) {
      throw new RuntimeException("$name is not a valid command; valid commands for '$commandPath' are (${commandDefinitions.keySet().join(", ")})")
    }

    def builtCommand = commandDefinition.build((args.size() != 0 && args[0] instanceof Map) ? args[0] : [:])
    builtCommand.COMMAND_NAME = name

    Closure closure = findClosure(args)
    if(closure) {
      builtCommand.CHILDREN = []
      closure.delegate = new CommandPdfLingo(builtCommand.CHILDREN, defaultSettings, commandDefinition.commandDefinitions, appendToPath(commandPath, name))
      closure.resolveStrategy = Closure.DELEGATE_FIRST
      use(LocationPdfLingo) {
        closure()
      }
    }

    commands << builtCommand
  }

  private def findClosure(args) {
    if(args) {
      if (args.size() == 1 && (args[0] instanceof Closure)) {
        return args[0]
      } else if (args.size() == 2 && (args[1] instanceof Closure)) {
        return args[1]
      }
    }
    null
  }

  private def appendToPath(path, name) {
    if(path == "/") {
      path + name
    } else {
      path + "/" + name
    }
  }

//  def page(lingo, closure) {
//    closure.resolveStrategy = Closure.DELEGATE_FIRST
//
//    def pageNumber = lingo.number
//    lingo.remove "number"
//    def newDefaults = defaultSettings + lingo
//    newDefaults.page = pageNumber
//
//    closure.delegate = new CommandPdfLingo(commands, newDefaults)
//    use(CommandPdfLingo) {
//      closure()
//    }
//  }

//  def insert(lingo) {
//    commands << new InsertCommand(lingo: defaultSettings + lingo)
//  }
//
//  def line(lingo) {
//    commands << new LineCommand(lingo: defaultSettings + lingo)
//  }
//
//  def write(lingo) {
//    commands << new WriteCommand(lingo: defaultSettings + lingo)
//  }
//
//  def section(closure) {
//    section([:], closure)
//  }
//
//  def section(lingo, closure) {
//    SectionCommand command = new SectionCommand(lingo: defaultSettings + lingo)
//    closure.delegate = command
//    closure.resolveStrategy = Closure.DELEGATE_FIRST
//    use(LocationPdfLingo) {
//      closure()
//    }
//    commands << command
//  }
//
//  def table(lingo, closure) {
//    TableCommand command = new TableCommand(lingo: defaultSettings + lingo)
//    closure.delegate = command
//    closure()
//    commands << command
//  }
//
//  def rectangle(lingo) {
//    commands << new RectangleCommand(lingo: defaultSettings + lingo)
//  }
//
//  def canvas(lingo, closure) {
//    commands << new CanvasCommand(lingo: defaultSettings + lingo, closure: closure)
//  }
//
//  def column(lingo, closure) {
//    commands << new ColumnCommand(lingo: defaultSettings + lingo, closure: closure)
//  }

//  def columns(closure) {
//    columns [:], closure
//  }

//  def columns(lingo, closure) {
//    def command = new ColumnsCommand(lingo: defaultSettings + lingo, closure: closure)
//    closure.delegate = command
//    closure.resolveStrategy = Closure.DELEGATE_FIRST
//    closure()
//    commands << command
//  }

  def font(lingo) {
    if (!lingo.id) throw new RuntimeException("Font id required")
    def embedded = lingo?.embedded ? BaseFont.EMBEDDED : BaseFont.NOT_EMBEDDED
    def encoding = lingo?.encoding ?: BaseFont.WINANSI
    if (lingo.name || lingo.file) {
      defaultSettings.configuredFonts[lingo.id] = BaseFont.createFont(lingo.name ?: lingo.file, encoding, embedded)
    } else {
      throw new RuntimeException("Font name or file required")
    }
  }

  def namedFont(lingo) {
    def baseFont = defaultSettings.configuredFonts[lingo.font]
    def font = new Font(baseFont, lingo.size)
    if (lingo.color) {
      font.color = lingo.color
    }
    defaultSettings.namedFonts[lingo.id] = font
  }

  def namedFont(String id) {
    defaultSettings.namedFonts[id]
  }

  def getNamedFont() {
    defaultSettings.namedFonts
  }

  def namedColor(lingo) {
    defaultSettings.namedColors[lingo.id] = lingo.color
  }

  def namedColor(String id) {
    defaultSettings.namedColors[id]
  }

  def getNamedColor() {
    defaultSettings.namedColors
  }

  def getFont(String id, int size) {
    new Font(defaultSettings.configuredFonts[id], size)
  }

  def each(collection, closure) {
    closure.delegate = this
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    collection.each closure
  }

}