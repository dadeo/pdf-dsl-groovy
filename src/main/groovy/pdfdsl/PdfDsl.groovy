package pdfdsl

import pdfdsl.support.PdfLingo
import pdfdsl.support.PdfTemplate

class PdfDsl {

  private def defaultSettings = [configuredFonts:[:], page:1, fontSize:12]

  def createTemplate(closure) {
    def commands = []
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure.delegate = new PdfLingo(commands, defaultSettings)
    use(PdfLingo) {
      closure()
    }
    new PdfTemplate(commands)
  }

}