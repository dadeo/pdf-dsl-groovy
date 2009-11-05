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

import com.lowagie.text.pdf.ColumnText
import com.lowagie.text.Phrase
import com.lowagie.text.Paragraph


class SectionCommand extends InternalCommand {


  SectionCommand() {
    defaults = [padding: 0]
  }

  private lines = []
  private text = []

  def line(map) {
    lines << map
  }

  def text(map) {
    text << map
  }

  def stampWith(DslWriter dslWriter) {
    LastPosition.startY = lingo.at[1].value(dslWriter.getPageSize(lingo.page), lingo)
    
    if (text) {
      processText(dslWriter)
    }

    if(lines) {
      processLines(dslWriter)
    }
  }

  private def processLines(DslWriter dslWriter) {
    def coordinates = [at: lingo.at]
    def maxLength = 0
    lines.each {
      dslWriter.stamp(lingo + coordinates + it)
      coordinates = [at: [lingo.at[0], coordinates.at[1] - lingo.fontSize]]
      maxLength = Math.max(maxLength, lingo.getTextLength(it.text))
    }

    drawBorder(maxLength, dslWriter, coordinates.at[1].value(lingo.pageSize, lingo) )
  }

  private def drawBorder = { maxLength, DslWriter dslWriter, lastY ->
    if (lingo.borderColor) {
      dslWriter.withDirectContent(lingo.page) {cb, pageSize ->

        def offset = 0
        if (lingo["justified"] == Locations.center) {
          offset = maxLength / 2
        } else if (lingo["justified"] == Locations.right) {
          offset = maxLength
        }

        def plotRectangle = {
          def y = lingo.at[1].value(pageSize, lingo) + lingo.fontSize

          cb.rectangle(
              (float) (lingo.at[0].value(pageSize, lingo) - offset - lingo.padding),
              (float) (y + lingo.padding),
              (float) (maxLength + (lingo.padding * 2)),
              (float) (lastY - y + lingo.fontSize - (lingo.padding * 2))
          )
        }

        plotRectangle()

        cb.colorStroke = lingo.borderColor
        cb.stroke()
      }
    }
  }

  private def processText(DslWriter dslWriter) {
    dslWriter.column(lingo) {ColumnText columnText ->
      def p = new Paragraph()
      def spaces = null
      text.each {
        def mapLingo = lingo + it
        if (it.newline == 'before') {
          p.add(new Phrase("\n"))
          spaces = null
        }
        if (spaces) {
          p.add new Phrase(spaces, mapLingo.font)
        }
        p.add new Phrase(it.value, mapLingo.font)
        if (it.newline == 'after') {
          p.add(new Phrase("\n"))
          spaces = null
        } else {
          spaces = lingo.spaces
        }
      }
      columnText.addElement(p)
    }
  }

}