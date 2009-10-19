package pdfdsl.support

import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.ColumnText
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.Phrase
import com.lowagie.text.Element


class TableCommand  extends InternalCommand {
  private headers
  private rows

  def headers(map) {
    headers = map.clone()
  }

  def rows(map) {
    rows = map.clone()
  }

  def stampWith(DslWriter dslWriter) {
    def table = new PdfPTable(headers.data.size())
    dslWriter.column(lingo) { ColumnText columnText ->
      headers.data.each { header ->
        def cell = new PdfPCell(new Phrase(header))
        cell.setGrayFill(0.95f)
        cell.setBorderWidthTop(0f)
        cell.setBorderWidthLeft(0f)
        cell.setBorderWidthRight(0f)
        cell.setHorizontalAlignment(justification(headers.justified)) 
        table.addCell(cell)
      }
      rows.data.each { row ->
        row.each { column ->
          table.addCell(column)
        }
      }
      columnText.addElement(table)
    }
  }

  private justification(justified) {
    switch(justified) {
      case Locations.center: return Element.ALIGN_CENTER
      case Locations.right: return Element.ALIGN_RIGHT
      default: return Element.ALIGN_LEFT
    }
  }

}