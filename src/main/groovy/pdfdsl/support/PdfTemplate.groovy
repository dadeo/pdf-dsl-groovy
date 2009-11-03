package pdfdsl.support


class PdfTemplate {
  private def commands

  PdfTemplate(commands) {
    this.commands = commands
  }
  
  byte[] create() {
    def dslWriter = new NewPdfWriter()
    def lastPage = 1
    commands.sort{it.lingo.page}.each { command ->
      def page = command.lingo.page
      while(lastPage < page) {
        dslWriter.insertPage()
        lastPage += 1
      }
      command.stampWith(dslWriter)
    }
    dslWriter.bytes()
  }

  byte[] stamp(byte[] original) {
    def dslWriter = new ExistingPdfWriter(original)
    commands.each { command ->
      def page = command.lingo.page
      while(dslWriter.pageCount() < page) {
        dslWriter.insertPage()
      }
      command.stampWith(dslWriter)
    }
    dslWriter.bytes()
  }
  
}