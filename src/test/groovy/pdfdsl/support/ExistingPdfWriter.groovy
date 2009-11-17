package pdfdsl.support

import com.lowagie.text.pdf.PdfReader


class ExistingPdfWriterTest  extends GroovyTestCase {
  
  void test_insertPage() {
    def creator = new NewPdfWriter()
    creator.insertPage()
    assertEquals 1, new PdfReader(creator.bytes()).numberOfPages

    def writer = new ExistingPdfWriter(creator.bytes())
    writer.insertPage()
    writer.insertPage()
    assertEquals 3, new PdfReader(writer.bytes()).numberOfPages
  }

}