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
package pdfdsl

import pdfdsl.support.ClosureExecutor
import pdfdsl.support.CommandDefinitionFactory
import pdfdsl.support.CommandPdfLingo
import pdfdsl.support.PdfTemplate
import pdfdsl.support.text.MarkedUpTextProcessorFactory

class PdfDsl {
  CommandDefinitionFactory definitionFactory = new CommandDefinitionFactory()
  ClosureExecutor closureExecutor = new ClosureExecutor()

  private def defaultSettings = [
      configuredFonts: [:],
      namedFonts: [:],
      namedColors: [:],
      page: 1,
      fontSize: 12,
      spaces: ' ',
      padding: 0,
      leading: 1.25,
      extraParagraphSpace: 0,
      margin: 72,
  ]

  private def validCommands = definitionFactory.createDefinitions()

  def createTemplate(closure) {
    defaultSettings.markedUpTextProcessor = new MarkedUpTextProcessorFactory().create(defaultSettings)

    def commands = []
    def pageCommands = [:]
    closureExecutor.execute closure, new CommandPdfLingo(commands, pageCommands, defaultSettings, validCommands)
    new PdfTemplate(commands, pageCommands, defaultSettings)
  }

}