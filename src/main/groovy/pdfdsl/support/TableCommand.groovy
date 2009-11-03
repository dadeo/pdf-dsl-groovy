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

import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.ColumnText
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.Phrase
import com.lowagie.text.Element


class TableCommand extends InternalCommand {
  private headers
  private rows

  def headers(map) {
    headers = map.clone()
  }

  def rows(map) {
    rows = map.clone()
  }

  def stampWith(DslWriter dslWriter) {
    def table = new PdfPTable(headers.data.size())
    dslWriter.column(lingo) {ColumnText columnText ->

      def headersLingo = new MapWrapper(lingo + headers)
      headers.data.each {header ->
        def cell = new PdfPCell(new Phrase(header, headersLingo.font))
        cell.setGrayFill(0.95f)
        cell.setBorderWidthTop(0f)
        cell.setBorderWidthLeft(0f)
        cell.setBorderWidthRight(0f)
        cell.setHorizontalAlignment(justification(headers.justified))
        table.addCell(cell)
      }

      def rowsLingo = new MapWrapper(lingo + rows)
      rows.data.each {row ->
        row.each {column ->
          table.addCell(new Phrase(column, rowsLingo.font))
        }
      }
      columnText.addElement(table)
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