package pdfdsl.support


class CommandDefinitionFactory {

  def createDefinitions() {
    [
        write: new CommandDefinition(new WriteCommand()),
        line: new CommandDefinition(new LineCommand()),
        rectangle: new CommandDefinition(new RectangleCommand()),
        table: createTableDefinition(),
        canvas: createCanvasDefinition(),
        section: createSectionDefinition()
    ]
  }

  private CommandDefinition createCanvasDefinition() {
    def insertTableDefinition = new CommandDefinition(new Object())
    new CommandDefinition(new CanvasCommand(), [insertTable: insertTableDefinition])
  }

  private CommandDefinition createTableDefinition() {
    def headersDefinition = new CommandDefinition(new Object())
    def rowsDefinition = new CommandDefinition(new Object())
    new CommandDefinition(new TableCommand(), [headers: headersDefinition, rows: rowsDefinition])
  }

  private CommandDefinition createSectionDefinition() {
    def lineDefinition = new CommandDefinition(new Object())
    def textDefinition = new CommandDefinition(new Object())
    new CommandDefinition(new SectionCommand(), [line: lineDefinition, text: textDefinition])
  }

}