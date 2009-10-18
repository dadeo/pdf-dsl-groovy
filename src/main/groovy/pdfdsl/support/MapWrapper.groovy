package pdfdsl.support

import com.lowagie.text.pdf.BaseFont


class MapWrapper {
  private mapIn

  MapWrapper(map) {
    mapIn = map
  }

  def getText() {
    mapIn["text"] ?: ""
  }

  def getAt() {
    def at = mapIn["at"]
    def x = (at[0] instanceof Number) ? new Location(at[0]) : at[0]
    def y = (at[1] instanceof Number) ? new Location(at[1]) : at[1]
    [x,y]
  }

  def getHeight() {
    mapIn["height"]
  }

  def getWidth() {
    mapIn["width"]
  }

  def getFontSize() {
    mapIn["fontSize"]
  }

  def getPage() {
    mapIn["page"]
  }

  def baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)

  float getJustificationOffset() {
    def justification = mapIn["justified"]
    def widthOfText = baseFont.getWidthPoint(text, fontSize)
    if(justification == Locations.center) {
      widthOfText / 2
    } else if(justification == Locations.right) {
      widthOfText
    } else 0
  }

}