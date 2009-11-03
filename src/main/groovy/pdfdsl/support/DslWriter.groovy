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

import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.ColumnText


abstract class DslWriter {

  def stamp(values) {
    values = new MapWrapper(values)

    def over = getDirectContent(values.page)
    over.beginText()

    over.setFontAndSize(values.baseFont, values.fontSize)

    Rectangle pageSize = getPageSize(values.page)
    def adjustedX = values.at[0].value(pageSize, values) - values.justificationOffset
    def adjustedY = values.at[1].value(pageSize, values)
    over.setTextMatrix((float) adjustedX, (float) adjustedY)

    over.showText(values.text)
    over.endText();
  }

  def column(values, closure) {
    values = new MapWrapper(values)

    def columnText = new ColumnText(getDirectContent(values.page))

    Rectangle pageSize = getPageSize(values.page)
    def adjustedX = (float) (values.at[0].value(pageSize, values) - values.justificationOffset)
    def adjustedY = (float) values.at[1].value(pageSize, values)
    columnText.setSimpleColumn(adjustedX, (float) (adjustedY - values.height), (float) (adjustedX + values.width), adjustedY)

    closure(columnText)

    columnText.go()
  }

  def withDirectContent(page, closure) {
    PdfContentByte cb = getDirectContent(page)
    cb.saveState()
    closure(cb, getPageSize(page))
    cb.restoreState()
  }

  abstract byte[] bytes()

  abstract protected PdfContentByte getDirectContent(int page)

  abstract protected Rectangle getPageSize(int page)
}