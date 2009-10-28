package pdfdsl

import pdfdsl.support.ExistingPdfWriter
import pdfdsl.support.WriteCommand
import pdfdsl.support.NewPdfWriter
import pdfdsl.support.Locations
import pdfdsl.support.Location
import pdfdsl.support.ResultLocation
import pdfdsl.support.SectionCommand
import pdfdsl.support.TableCommand
import pdfdsl.support.LineCommand
import com.lowagie.text.pdf.BaseFont
import pdfdsl.support.RectangleCommand

class PdfDsl {
  private def commands = []
  private def defaults = [configuredFonts:[:], page:1, fontSize:12]

  def 'do'(closure) {
    closure.delegate = this
    use(PdfDsl) {
      closure()
    }
    this
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

  def font(lingo) {
    if(!lingo.id) throw new RuntimeException("Font id required")
    def embedded = lingo?.embedded ? BaseFont.EMBEDDED : BaseFont.NOT_EMBEDDED
    def encoding = lingo?.encoding ?: BaseFont.WINANSI
    if(lingo.name || lingo.file) {
      defaults.configuredFonts[lingo.id] = BaseFont.createFont(lingo.name ?: lingo.file, encoding, embedded)
    } else {
      throw new RuntimeException("Font name or file required")
    }
  }

  def line(lingo) {
    commands << new LineCommand(lingo : defaults + lingo)
  }

  def write(lingo) {
    commands << new WriteCommand(lingo : defaults + lingo)
  }

  def section(lingo, closure) {
    SectionCommand command = new SectionCommand(lingo: defaults + lingo)
    closure.delegate = command
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure()
    commands << command
  }

  def table(lingo, closure) {
    TableCommand command = new TableCommand(lingo: defaults + lingo)
    closure.delegate = command
    closure()
    commands << command
  }

  def rectangle(lingo) {
    commands << new RectangleCommand(lingo: defaults + lingo)  
  }

  def getRight() {
    Locations.right
  }

  def getLeft() {
    Locations.left
  }

  def getCenter() {
    Locations.center
  }

  def getTop() {
    Locations.top
  }

  def getBottom() {
    Locations.bottom
  }

  def getMiddle() {
    Locations.middle
  }

  def getFontSize() {
    Locations.fontSize
  }

  static plus(Integer target, Location location) {
    new ResultLocation("+", new Location(target), location)
  }

  static minus(Integer target, Location location) {
    new ResultLocation("-", new Location(target), location)
  }

  static multiply(Integer target, Location location) {
    new ResultLocation("*", new Location(target), location)
  }

  static div(Integer target, Location location) {
    new ResultLocation("/", new Location(target), location)
  }
}