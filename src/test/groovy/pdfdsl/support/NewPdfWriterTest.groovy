package pdfdsl.support

import com.lowagie.text.pdf.PdfReader


class NewPdfWriterTest extends GroovyTestCase {
  
  void test_insertPage() {
    def writer = new NewPdfWriter()
    writer.insertPage()
    writer.insertPage()
    def reader = new PdfReader(writer.bytes())
    assertEquals 2, reader.getNumberOfPages()
  }

}