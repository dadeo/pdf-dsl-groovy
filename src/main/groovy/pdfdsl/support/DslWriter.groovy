package pdfdsl.support

import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.BaseFont


abstract class DslWriter {

  def stamp(values) {
    values = new MapWrapper(values)

    def over = getDirectContent(values.page)
    over.beginText()

    over.setFontAndSize(values.baseFont, values.fontSize)

    Rectangle pageSize = getPageSize(values.page)
    def adjustedX = values.at[0].value(pageSize, values) - values.justificationOffset
    def adjustedY = values.at[1].value(pageSize, values)
    over.setTextMatrix((float)adjustedX, (float)adjustedY)

    over.showText(values.text)
    over.endText();
  }

//  def column(values: MapWrapper)(f: (ColumnText) => Unit) {
//    val columnText: ColumnText = new ColumnText(getDirectContent(values.page))
//
//    val (x, y) = values.at
//    val pageSize: Rectangle = getPageSize(values.page)
//    val adjustedX = x.value(pageSize, values) - values.justificationOffset
//    val adjustedY = y.value(pageSize, values)
//    columnText.setSimpleColumn(adjustedX, adjustedY - values.height, adjustedX + values.width, adjustedY)
//
//    f(columnText)
//
//    columnText.go()
//  }

  abstract byte[] bytes()

  abstract protected PdfContentByte getDirectContent(int page)

  abstract protected Rectangle getPageSize(int page)
}