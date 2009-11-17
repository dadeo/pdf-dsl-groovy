/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package pdfdsl.support

import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.PageSize

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

  protected PdfContentByte getDirectContentUnder(int page) {
    stamper.getUnderContent(page)
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
    stamper.insertPage(reader.getNumberOfPages() + 1, PageSize.LETTER)
  }
}