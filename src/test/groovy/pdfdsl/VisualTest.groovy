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

import java.awt.Color
import com.lowagie.text.pdf.BaseFont


public class VisualTest extends GroovyTestCase {

  void testIt() {
    TestPdfFactory.createPdf("target/original.pdf")

    PdfDsl dsl = new PdfDsl()

    def pdfTemplate1 = dsl.createTemplate {
      font id: 'f1', file: '/Library/Fonts/Herculanum.ttf', embedded: true
      font id: 'f2', name: BaseFont.TIMES_BOLD
      font id: 'f3', name: BaseFont.TIMES_ROMAN

      write text: "100, top-200", at: [100, top-200], page: 1

      table at: [100, top - 200], page: 1, width: 500, height: 600, {
        headers justified: center, data: ["hello\nworld", "column 0", "column 1", "column 2", "column 3"], font: 'f1'

        rows font: 'f2', data: [
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"]
        ]
      }
    }

    def pdfTemplate2 = dsl.createTemplate {
      def y = top - (fontSize * 3) - 2
      line at: [center - 20, y], to: [center + 20, y], width: 2

      line at: [left, top], to: [right, bottom], page: 3
      line at: [left, bottom], to: [right, top], width: 5, page: 3
      line at: [100, top - 200], to: [600, top - 200], width: 8, color: Color.BLUE

      write text: "hello world 2", at: [25, 700], page: 2
      write text: "hello world 1", at: [26, 700 + fontSize], page: 2
      write text: "hello world 3", at: [25, 700 - fontSize], page: 2
      write text: "bottom-right", justified: right, at: [right, bottom], page: 1
      write text: "bottom-left", justified: left, at: [left, bottom], page: 1
      write text: "top-right", justified: right, at: [right, top - fontSize], page: 1, font: 'f3'
      write text: "top-left", at: [left, top - fontSize], page: 1, font: 'f2'
      write text: "top-center", at: [(right - left) / 2, top - fontSize], page: 1
      write text: "almost-top-center", at: [center, top - fontSize - fontSize], page: 1
      write text: "top-center-justified", justified: center, at: [center, top - fontSize * 3], page: 1
      write text: "top-right-justified", justified: right, at: [center, top - fontSize * 4], page: 1
      write text: "centered-middle", justified: center, at: [center, middle], page: 1, font: 'f1'

      rectangle at: [center, middle], width: 144, height: 72, backgroundColor: Color.lightGray, borderColor: Color.RED

      section page: 1, at: [left + 50, 400], borderColor: Color.BLUE, {
        line text: "pinky jones", font: 'f1'
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [center, 300], justified: center, font: 'f3', fontSize: 24, borderColor: Color.RED, padding: 10, {
        line text: "pinky jones"
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [right - 50, 200], justified: right, borderColor: Color.YELLOW, {
        line text: "pinky jones"
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [left + 50, 250], width: 250, height: 150, justified: left, font: 'f3', fontSize:10, {
        text value: "This is my Main Heading", font:'f2', fontSize:12, newline:'after'
        text value: "This is important.", font:'f2'
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

      section page: 1, at: [left + 50, lastY - fontSize], width: 250, height: 150, justified: left, font: 'f3', fontSize:10, borderColor: Color.YELLOW,{
        text value: "This is my Main Heading", font:'f2', fontSize:12, newline:'after'
        text value: "This is important.", font:'f2'
        text value: "This is where all the unimportant text follows.  It looks something like this ... asdkfasd asdf asdf asd fasdf asd f"
      }

    }

    new File("target/create.pdf").withOutputStream {
      it << pdfTemplate1.stamp(pdfTemplate2.create())
    }

    new File("target/createTableOnly.pdf").withOutputStream { it << pdfTemplate1.create() }

    new File("target/update.pdf").withOutputStream {
      it << pdfTemplate1.stamp(pdfTemplate2.stamp(new File("target/original.pdf").readBytes()))
    }
  }

}