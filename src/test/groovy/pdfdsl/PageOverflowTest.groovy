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

import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfPTable
import java.awt.Color
import static pdfdsl.util.AssertionHelper.*

public class PageOverflowTest extends GroovyTestCase {

  void testIt() {
    TestPdfFactory.createPdf("target/original.pdf")

    PdfDsl dsl = new PdfDsl()
    def templateCreator = { yPos ->
      dsl.createTemplate {
        defaults margin: 1.inch, font: 'text'
        font id: 'times', name: BaseFont.TIMES_ROMAN
        namedFont id: 'text', font: 'times', size: 11

        page {
          section at:[0, yPos], {
            markup text: """
                |Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt
                |ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris
                |nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit
                |esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt
                |in culpa qui officia deserunt mollit anim id est laborum.
            """
          }
        }
      }
    }

    shouldFail("pdf page contents overflow") {
        templateCreator(79).create()
    }

    new File("target/page-overflow.pdf").withOutputStream {
      it << templateCreator(80).create()
    }
    
    shouldFail("pdf page contents overflow") {
        templateCreator(79).stamp(new File("target/original.pdf").readBytes())
    }

    new File("target/page-overflow-update.pdf").withOutputStream {
      it << templateCreator(80).stamp(new File("target/original.pdf").readBytes())
    }
  }

 }