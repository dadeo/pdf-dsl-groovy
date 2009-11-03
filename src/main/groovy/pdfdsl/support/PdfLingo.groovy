package pdfdsl.support

import com.lowagie.text.pdf.BaseFont


class PdfLingo {

  private def commands
  private def defaultSettings

  PdfLingo(commands, defaults) {
    this.commands = commands
    this.defaultSettings = defaults
  }
  
  def font(lingo) {
    if(!lingo.id) throw new RuntimeException("Font id required")
    def embedded = lingo?.embedded ? BaseFont.EMBEDDED : BaseFont.NOT_EMBEDDED
    def encoding = lingo?.encoding ?: BaseFont.WINANSI
    if(lingo.name || lingo.file) {
      defaultSettings.configuredFonts[lingo.id] = BaseFont.createFont(lingo.name ?: lingo.file, encoding, embedded)
    } else {
      throw new RuntimeException("Font name or file required")
    }
  }

  def line(lingo) {
    commands << new LineCommand(lingo : defaultSettings + lingo)
  }

  def write(lingo) {
    commands << new WriteCommand(lingo : defaultSettings + lingo)
  }

  def section(lingo, closure) {
    SectionCommand command = new SectionCommand(lingo: defaultSettings + lingo)
    closure.delegate = command
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure()
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