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


class CommandDefinitionFactory {

  def createDefinitions() {
    [
        write: new CommandDefinition(new WriteCommand()),
        line: new CommandDefinition(new LineCommand()),
        rectangle: new CommandDefinition(new RectangleCommand()),
        table: createTableDefinition(),
        canvas: createCanvasDefinition(),
        section: createSectionDefinition(),
        page: createPageDefinition()
    ]
  }

  private CommandDefinition createCanvasDefinition() {
    def insertTableDefinition = new CommandDefinition(new NoOpCommand())
    new CommandDefinition(new CanvasCommand(), [insertTable: insertTableDefinition])
  }

  private CommandDefinition createTableDefinition() {
    def headersDefinition = new CommandDefinition(new NoOpCommand())
    def rowsDefinition = new CommandDefinition(new NoOpCommand())
    new CommandDefinition(new TableCommand(), [headers: headersDefinition, rows: rowsDefinition])
  }

  private CommandDefinition createSectionDefinition() {
    def lineDefinition = new CommandDefinition(new NoOpCommand())
    def textDefinition = new CommandDefinition(new NoOpCommand())
    new CommandDefinition(new SectionCommand(), [line: lineDefinition, text: textDefinition])
  }

  private CommandDefinition createPageDefinition() {
    def insertDefinition = new CommandDefinition(new InsertCommand())
    def columnDefinition = new CommandDefinition(new ColumnCommand(), [section: createSectionDefinition(), insert:insertDefinition])
    def columnsDefinition = new CommandDefinition(new ColumnsCommand(), [column: columnDefinition])
    new CommandDefinition(new NoOpCommand(), [columns: columnsDefinition, section:createSectionDefinition()], [number:'page'])
  }

}