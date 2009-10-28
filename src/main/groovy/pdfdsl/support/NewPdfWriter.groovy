package pdfdsl.support

import com.lowagie.text.Document
import com.lowagie.text.pdf.PdfWriter
import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.Rectangle
import com.lowagie.text.PageSize


class NewPdfWriter extends DslWriter {
  private def out = new ByteArrayOutputStream()
  private def document = new Document(PageSize.LETTER)
  private def writer = PdfWriter.getInstance(document, out);

  NewPdfWriter() {
    document.open()
  }

  protected PdfContentByte getDirectContent(int page) {
    writer.getDirectContent()
  }

  protected Rectangle getPageSize(int page) {
    document.getPageSize()
  }

  byte[] bytes() {
    document.close()
    out.toByteArray()
  }

  def insertPage() {
    writer.setPageEmpty(false)
    document.newPage()
  }
}