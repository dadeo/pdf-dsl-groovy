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

import com.lowagie.text.Paragraph
import com.lowagie.text.Phrase
import com.lowagie.text.pdf.ColumnText

class SectionCommand extends InternalCommand {


  SectionCommand() {
    defaults = [padding: 0]
  }

  def getLines() {
    lingo.CHILDREN.findAll { it.COMMAND_NAME == 'line' }
  }

  def getTexts() {
    lingo.CHILDREN.findAll { it.COMMAND_NAME == 'text' }
  }


  def stampWith(DslWriter dslWriter) {
    LastPosition.startY = lingo.at[1].value(dslWriter.getPageSize(lingo.page), lingo)

    if (texts) {
      processText(dslWriter)
    }

    if (lines) {
      processLines(dslWriter)
    }
  }

  private def processLines(DslWriter dslWriter) {
    def coordinates = [at: lingo.at]
    def maxLength = 0
    lines.each {
      def lineLingo = lingo + coordinates + it
      dslWriter.stamp(lineLingo)
      coordinates = [at: [lineLingo.at[0], coordinates.at[1] - lineLingo.fontSize]]
      maxLength = Math.max(maxLength, lineLingo.getTextLength(it.text))
    }

    drawBorder(maxLength, dslWriter, coordinates.at[1].value(dslWriter.getPageSize(lingo.page), lingo))
  }

  private def drawBorder = {maxLength, DslWriter dslWriter, lastY ->
    if (lingo.borderColor) {
      def dimensionsExtractor = {pageSize ->
        def offset = lingo.getJustificationOffset(maxLength)
        float x = (float) (lingo.at[0].value(pageSize, lingo) - offset - lingo.padding)
        float y = (float) (lingo.at[1].value(pageSize, lingo) + lingo.padding + lingo.fontSize)
        float width = (float) (maxLength + (lingo.padding * 2))
        float height = (float) (lastY - y + lingo.fontSize - (lingo.padding * 2))
        [x, y, width, height]
      }

      drawRectangle dslWriter, dimensionsExtractor
    }
  }

  private def drawBorder2 = {maxLength, DslWriter dslWriter, lastY ->
    if (lingo.borderColor) {
      def dimensionsExtractor = {pageSize ->
        final EXTRA_PADDING = lingo.fontSize * 0
        def offset = lingo.getJustificationOffset(maxLength)
        float x = (float) (lingo.at[0].value(pageSize, lingo) - offset - lingo.padding - EXTRA_PADDING)
        float y = (float) (lingo.at[1].value(pageSize, lingo) + lingo.padding)
        float width = (float) (maxLength + (lingo.padding * 2) + (EXTRA_PADDING * 2))
        float height = (float) (lastY - y - (lingo.padding * 2) - EXTRA_PADDING)
        [x, y, width, height]
      }

      drawRectangle dslWriter, dimensionsExtractor
    }
  }

  private def drawRectangle = {dslWriter, dimensionsExtractor ->
    dslWriter.withDirectContentUnder(lingo.page) {cb, pageSize ->

      def plotRectangle = {
        cb.rectangle(* dimensionsExtractor(pageSize))
      }

      if(lingo.backgroundColor) {
        plotRectangle()
        cb.colorFill = lingo.backgroundColor
        cb.fill()
      }

      plotRectangle()

      cb.colorStroke = lingo.borderColor
      cb.stroke()
    }
  }

  private def processText(DslWriter dslWriter) {
    dslWriter.column(lingo) {ColumnText columnText ->
      def p = null
      def writeParagraph = {
        if (p) {
          columnText.addElement(p)
          p = null
        }
      }

      texts.each {
        def mapLingo = new MapWrapper(lingo + it)
        if (it.newline == 'before') {
          writeParagraph()
        }
        if (p) {
          p.add new Phrase(mapLingo.spaces, mapLingo.font)
          p.add new Phrase(it.value, mapLingo.font)
        } else {
          p = new Paragraph(it.value, mapLingo.font)
          p.leading = (float) (mapLingo.fontSize * mapLingo.leading)
          p.extraParagraphSpace = (float) (mapLingo.fontSize * mapLingo.extraParagraphSpace)
        }
        if (mapLingo.newline == 'after') {
          writeParagraph()
        }
      }
      writeParagraph()

      drawBorder2.curry lingo.getWidth(dslWriter), dslWriter
    }
  }

}