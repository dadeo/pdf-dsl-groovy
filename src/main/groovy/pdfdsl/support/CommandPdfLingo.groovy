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
  private ClosureExecutor closureExecutor = new ClosureExecutor()
  private def commands
  private def pageCommands
  private def defaultSettings
  private def commandDefinitions
  private def commandPath

  CommandPdfLingo(commands, defaults, commandDefinitions, String commandPath = "/") {
    this(commands, null, defaults, commandDefinitions, commandPath)
  }

  CommandPdfLingo(commands, pageCommands, defaults, commandDefinitions, String commandPath = "/") {
    this.commands = commands
    this.pageCommands = pageCommands
    this.defaultSettings = defaults
    this.commandDefinitions = commandDefinitions
    this.commandPath = commandPath
  }

  def methodMissing(String name, args) {
    commands << buildCommand(name, args)
  }

  def header(args) {
    pageCommands.headers = [buildCommand("header", [args])]
  }

  private def buildCommand(String name, args) {
    def commandDefinition = commandDefinitions[name]
    if (!commandDefinition) {
      throw new RuntimeException("$name is not a valid command; valid commands for '$commandPath' are (${commandDefinitions.keySet().join(", ")})")
    }

    def builtCommand = commandDefinition.build((args.size() != 0 && args[0] instanceof Map) ? args[0] : [:])
    builtCommand.COMMAND_NAME = name
    verifyFontsExist builtCommand
    Closure closure = findClosure(args)
    if (closure) {
      builtCommand.CHILDREN = []
      closureExecutor.execute closure, new CommandPdfLingo(builtCommand.CHILDREN, defaultSettings, commandDefinition.commandDefinitions, appendToPath(commandPath, name))
    }
    builtCommand
  }

  private def verifyFontsExist(command) {
    def specifiedFont = command.font
    if (specifiedFont instanceof String) {
      if (!(defaultSettings.configuredFonts[specifiedFont] || defaultSettings.namedFonts[specifiedFont])) {
        throw new RuntimeException("font with id of '${specifiedFont}' is not configured")
      }
    }
  }

  private def findClosure(args) {
    if (args) {
      if (args.size() == 1 && (args[0] instanceof Closure)) {
        return args[0]
      } else if (args.size() == 2 && (args[1] instanceof Closure)) {
        return args[1]
      }
    }
    null
  }

  private def appendToPath(path, name) {
    if (path == "/") {
      path + name
    } else {
      path + "/" + name
    }
  }

  def exec(closure, ... args) {
    closureExecutor.execute closure, this, args
  }

  def include(lingo) {
    def closure = lingo.template
    exec(closure, *lingo.args)
  }

  def markup(String text) {
    defaultSettings.markedUpTextProcessor.process(text, namedFont(defaultSettings.font))
  }

  def defaults(lingo) {
    defaultSettings.putAll lingo
  }

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

  def each(collection, closure) {
    collection.each {
      exec closure, it
    }
  }

}