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

import com.lowagie.text.Document
import com.lowagie.text.PageSize
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.pdf.PdfWriter

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

  protected PdfContentByte getDirectContentUnder(int page) {
    writer.getDirectContentUnder()
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