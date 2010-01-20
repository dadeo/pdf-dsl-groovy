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

class PdfLingo extends BasicPdfLingo {

  private def commands

  PdfLingo(commands, defaults) {
    super(defaults)
    this.commands = commands
  }

  def page(lingo, closure) {
    closure.resolveStrategy = Closure.DELEGATE_FIRST

    def pageNumber = lingo.number
    lingo.remove "number"
    def newDefaults = defaultSettings + lingo
    newDefaults.page = pageNumber

    closure.delegate = new PdfLingo(commands, newDefaults)
    use(PdfLingo) {
      closure()
    }
  }

  def insert(lingo) {
    commands << new InsertCommand(lingo: defaultSettings + lingo)
  }

  def line(lingo) {
    commands << new LineCommand(lingo: defaultSettings + lingo)
  }

  def write(lingo) {
    commands << new WriteCommand(lingo: defaultSettings + lingo)
  }

  def section(closure) {
    section([:], closure)
  }

  def section(lingo, closure) {
    SectionCommand command = new SectionCommand(lingo: defaultSettings + lingo)
    closure.delegate = command
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    use(BasicPdfLingo) {
      closure()
    }
    commands << command
  }

  def table(lingo, closure) {
    TableCommand command = new TableCommand(lingo: defaultSettings + lingo)
    closure.delegate = command
    closure()
    commands << command
  }

  def rectangle(lingo) {
    commands << new RectangleCommand(lingo: defaultSettings + lingo)
  }

  def canvas(lingo, closure) {
    commands << new CanvasCommand(lingo: defaultSettings + lingo, closure: closure)
  }

//  def column(lingo, closure) {
//    commands << new ColumnCommand(lingo: defaultSettings + lingo, closure: closure)
//  }

  def columns(closure) {
    columns [:], closure
  }

  def columns(lingo, closure) {
    def command = new ColumnsCommand(lingo: defaultSettings + lingo, closure: closure)
    closure.delegate = command
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure()
    commands << command
  }

}