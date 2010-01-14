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

  def each(collection, closure) {
    closure.delegate = this
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    collection.each closure
  }

  def stampWith(DslWriter dslWriter) {
    LastPosition.startY = lingo.at[1].value(dslWriter.getPageSize(lingo.page), lingo)

    if (text) {
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
      dslWriter.stamp(lingo + coordinates + it)
      coordinates = [at: [lingo.at[0], coordinates.at[1] - lingo.fontSize]]
      maxLength = Math.max(maxLength, lingo.getTextLength(it.text))
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
      text.each {
        def mapLingo = lingo + it
        if (it.newline == 'before') {
          writeParagraph()
        }
        if (p) {
          p.add new Phrase(lingo.spaces, mapLingo.font)
          p.add new Phrase(it.value, mapLingo.font)
        } else {
          p = new Paragraph(it.value, mapLingo.font)
          p.leading = (float) (mapLingo.fontSize * mapLingo.leading)
          p.extraParagraphSpace = (float) (mapLingo.fontSize * mapLingo.extraParagraphSpace)
        }
        if (it.newline == 'after') {
          writeParagraph()
        }
      }
      writeParagraph()

      drawBorder2.curry lingo.getWidth(dslWriter), dslWriter
    }
  }

}