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

public class HeaderTest extends GroovyTestCase {

  void testIt() {
    TestPdfFactory.createPdf("target/original.pdf")

    PdfDsl dsl = new PdfDsl()

    def pdfTemplate = dsl.createTemplate {
      defaults margin:0.75.inch, font: 'text'
      
      font id: 'bold', name: BaseFont.TIMES_BOLD
      font id: 'times', name: BaseFont.TIMES_ROMAN

      namedFont id: 'text', font: 'times', size: 12
      namedFont id: 'bold text', font: 'bold', size: 24

      header {
        section at: [right - 1.25.inch, 10.5.inch], {
          markup text: "Page #{#} of #{##}", font: 'text'
        }
        hline width: 2, before: 2.mm, after: 1.mm
      }

      page number:1, {
        section at: [left, 9.inches], {
          text value: "Hello World!"
        }
      }
      page number:2, {
        section at: [left, 9.inches], {
          text value: "Hello World!!"
        }
      }
      page number:3, {
        section at: [left, 9.inches], {
          text value: "Hello World!!!"
        }
      }
      page number:4, {
        section at: [left, 9.inches], {
          text value: "Hello World!!!!"
        }
      }
    }

    new File("target/create-headers.pdf").withOutputStream { it << pdfTemplate.create() }

    new File("target/stamp-headers.pdf").withOutputStream {
      it << pdfTemplate.stamp(new File("target/original.pdf").readBytes())
    }
  }

}