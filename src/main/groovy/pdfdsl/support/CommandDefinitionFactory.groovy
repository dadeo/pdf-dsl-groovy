package pdfdsl.support


class CommandDefinitionFactory {

  def createDefinitions() {
    [
        write: new CommandDefinition(new WriteCommand()),
        line: new CommandDefinition(new LineCommand()),
        rectangle: new CommandDefinition(new RectangleCommand()),
        table: createTableDefintion(),
        canvas: createCanvasDefinition()
    ]
  }

  private CommandDefinition createCanvasDefinition() {
    def insertTableDefinition = new CommandDefinition(new Object())
    new CommandDefinition(new CanvasCommand(), [insertTable: insertTableDefinition])
  }

  private CommandDefinition createTableDefintion() {
    def headersDefinition = new CommandDefinition(new Object())
    def rowsDefinition = new CommandDefinition(new Object())
    new CommandDefinition(new TableCommand(), [headers: headersDefinition, rows: rowsDefinition])
  }

}