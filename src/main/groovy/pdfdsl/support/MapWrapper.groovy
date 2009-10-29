package pdfdsl.support

import com.lowagie.text.pdf.BaseFont
import java.awt.Color
import com.lowagie.text.Font


class MapWrapper {
  private mapIn

  MapWrapper(map) {
    mapIn = map
  }

  def propertyMissing(String name) {
    mapIn[name]  
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

  def getFont() {
    new Font(baseFont, fontSize)
  }

  def getBaseFont() {
    if(mapIn.font) {
     mapIn.configuredFonts[mapIn.font] 
    } else {
      BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
    }
  }

  float getTextLength() {
    baseFont.getWidthPoint(text, fontSize)
  }

  float getTextLength(t) {
    baseFont.getWidthPoint(t, fontSize)
  }

  float getJustificationOffset() {
    def justification = mapIn["justified"]
    def widthOfText = textLength
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