package pdfdsl.support

import com.lowagie.text.pdf.BaseFont
import java.awt.Color


class MapWrapper {
  private mapIn

  MapWrapper(map) {
    mapIn = map
  }

  def getColor() {
    mapIn["color"] ?: Color.BLACK
  }

  def getText() {
    mapIn["text"] ?: ""
  }

  def getAt() {
    forceCoordinatesToLocation(mapIn["at"])
  }

  def getTo() {
    forceCoordinatesToLocation(mapIn["to"])
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

  private List forceCoordinatesToLocation(location) {
    def x = (location[0] instanceof Number) ? new Location(location[0]) : location[0]
    def y = (location[1] instanceof Number) ? new Location(location[1]) : location[1]
    [x, y]
  }

}