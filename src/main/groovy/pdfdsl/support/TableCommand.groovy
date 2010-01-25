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

import com.lowagie.text.Element
import com.lowagie.text.Phrase
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import java.awt.Color

class TableCommand extends InternalCommand {

  TableCommand() {
    defaults = [borderColor: new Color(216, 216, 216)]
  }

  def getHeaders() {
    lingo.CHILDREN.find { it.COMMAND_NAME == 'headers' }
  }

  def getRows() {
    lingo.CHILDREN.find { it.COMMAND_NAME == 'rows' }
  }

  def stampWith(DslWriter dslWriter) {
    dslWriter.withDirectContent(lingo.page) {contentBytes, pageSize ->
      def table = new PdfPTable(headers.data.size())

      def headersLingo = lingo + headers
      headers.data.each {header ->
        def cell = new PdfPCell(new Phrase(header, headersLingo.font))
        cell.setBackgroundColor(new Color(240, 240, 240))
        cell.setBorderWidthTop(0.05f)
        cell.setBorderWidthLeft(0.05f)
        cell.setBorderWidthRight(0.05f)
        cell.setBorderColor(headersLingo.borderColor)
        cell.setHorizontalAlignment(justification(headers.justified))
        table.addCell(cell)
      }

      def rowsLingo = lingo + rows
      rows.data.each {row ->
        row.each {column ->
          PdfPCell cell = new PdfPCell(new Phrase(column, rowsLingo.font))
          cell.setBorderColor(rowsLingo.borderColor)
          table.addCell(cell)
        }
      }

      def adjustedX = lingo.at[0].value(pageSize, lingo)
      def adjustedY = lingo.at[1].value(pageSize, lingo)

      table.totalWidth = (float) lingo.getWidth(dslWriter)
      table.writeSelectedRows 0, -1, adjustedX, adjustedY, contentBytes

      LastPosition.lastY = (float) (adjustedY - table.totalHeight)
    }
  }

  private justification(justified) {
    switch (justified) {
      case Locations.center: return Element.ALIGN_CENTER
      case Locations.right: return Element.ALIGN_RIGHT
      default: return Element.ALIGN_LEFT
    }
  }

}