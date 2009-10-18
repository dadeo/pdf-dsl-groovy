package pdfdsl.support

import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfContentByte

class ExistingPdfWriter extends DslWriter {
  private def reader
  private def out = new ByteArrayOutputStream()
  private def stamper

  ExistingPdfWriter(byte[] bytesIn) {
    reader = new PdfReader(bytesIn)
    stamper = new PdfStamper(reader, out);
  }


  protected PdfContentByte getDirectContent(int page) {
    stamper.getOverContent(page)
  }

  protected Rectangle getPageSize(int page) {
    reader.getPageSizeWithRotation(page)
  }

  byte[] bytes() {
    stamper.close()
    out.toByteArray()
  }

  def int pageCount() {
    reader.getNumberOfPages()
  }

  def insertPage() {
    stamper.insertPage(reader.getNumberOfPages + 1, PageSize.LETTER)
  }
}