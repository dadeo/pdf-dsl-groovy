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
package pdfdsl

import com.lowagie.text.pdf.BaseFont

import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.Paragraph
import com.lowagie.text.Font
import java.awt.Color
import com.lowagie.text.pdf.PdfPCell

public class MarkupTest extends GroovyTestCase {

  void testIt() {
    TestPdfFactory.createPdf("target/original.pdf")

    PdfDsl dsl = new PdfDsl()

    def pdfTemplate = dsl.createTemplate {
      defaults margin: 1.inch, font: 'text'

      font id: 'bold', name: BaseFont.TIMES_BOLD
      font id: 'times', name: BaseFont.TIMES_ROMAN

      namedFont id: 'text', font: 'times', size: 11
      namedFont id: 'bold text', font: 'bold', size: 11
      namedFont id: 'h1', font: 'bold', size: 18

      namedColor id: 'red', color:Color.red
      
      page {
        section {
          markup paragraphModifier: { Paragraph paragraph -> paragraph.spacingAfter = 3.mm }, text: """|
              |<font name="h1"><color name="red">Hello</color> <u>World</u>!!!</font>|
              |Lorem ipsum <u>dolor sit</u> amet, <font name="bold text">consectetur adipisicing elit</font>, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.|
              |<font name="bold text">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</font>|
          """
        }

        insert table:exec(createTable)
      }
    }

    new File("target/markup-create.pdf").withOutputStream {
      it << pdfTemplate.stamp(pdfTemplate.create())
    }

    new File("target/markup-update.pdf").withOutputStream {
      it << pdfTemplate.stamp(pdfTemplate.stamp(new File("target/original.pdf").readBytes()))
    }
  }

  private createTable = {
    PdfPTable table = new PdfPTable([1.5.inches, 1.inch, 0.5.inch, 0.5.inch] as float[])
    table.addCell(markup("<u><color name='red'>red</color> underlined text</u>"))
    table.addCell("world")
    table.addCell("yo")
    table.addCell("dog")
    table
  }

 }