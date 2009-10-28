package pdfdsl

import java.awt.Color
import com.lowagie.text.pdf.BaseFont


public class VisualTest extends GroovyTestCase {

  void testIt() {
    TestPdfFactory.createPdf("target/HelloWorldRead.pdf")

    def dsl = new PdfDsl().do {
      font id:'f1', file:'/Library/Fonts/Herculanum.ttf', embedded:true
      font id:'f2', name:BaseFont.TIMES_ROMAN

      def y = top - (fontSize * 3) - 2
      line at: [center - 20, y], to: [center + 20, y], width: 2

      line at: [left, top], to: [right, bottom], page: 3
      line at: [left, bottom], to: [right, top], width: 5, page: 3
      line at: [100, top - 200], to: [600, top - 200], width: 8, color: Color.BLUE

      table at: [100, top - 200], page: 1, width: 500, height: 600, {
        headers justified: center, data: ["hello\nworld", "column 0", "column 1", "column 2", "column 3"], font:'f1'

        rows font:'f2', data: [
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"],
            ["c1", "c2", "c3", "c4", "c5"]
        ]
      }

      write text: "hello world 2", at: [25, 700], page: 2
      write text: "hello world 1", at: [26, 700 + fontSize], page: 2
      write text: "hello world 3", at: [25, 700 - fontSize], page: 2
      write text: "bottom-right", justified: right, at: [right, bottom], page: 1
      write text: "bottom-left", justified: left, at: [left, bottom], page: 1
      write text: "top-right", justified: right, at: [right, top - fontSize], page: 1, font:'f2'
      write text: "top-left", at: [left, top - fontSize], page: 1, font:'f2'
      write text: "top-center", at: [(right - left) / 2, top - fontSize], page: 1
      write text: "almost-top-center", at: [center, top - fontSize - fontSize], page: 1
      write text: "top-center-justified", justified: center, at: [center, top - fontSize * 3], page: 1
      write text: "top-right-justified", justified: right, at: [center, top - fontSize * 4], page: 1
      write text: "centered-middle", justified: center, at: [center, middle], page: 1, font:'f1'

      rectangle at: [center, middle], width: 144, height: 72, backgroundColor:Color.lightGray, borderColor:Color.RED

      section page: 1, at: [left + 50, 400], {
        line text: "pinky jones", font:'f1'
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [center, 300], justified: center, font:'f2', {
        line text: "pinky jones"
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }

      section page: 1, at: [right - 50, 200], justified: right, {
        line text: "pinky jones"
        line text: "suite abc"
        line text: "123 main st"
        line text: "des moines, ia 50023"
      }
    }

    new File("target/HelloWorldCreate.pdf").withOutputStream { it << dsl.create() }
    new File("target/HelloWorldUpdate.pdf").withOutputStream { it << dsl.stamp(new File("target/HelloWorldRead.pdf").readBytes()) }
  }

}