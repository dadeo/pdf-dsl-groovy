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

class InsertCommand extends InternalCommand {

  InsertCommand() {
    defaults = [width: 500, at:[Locations.left, Locations.lastY]]
  }

  def stampWith(DslWriter dslWriter) {
    if (lingo.table) {
      dslWriter.withDirectContent(lingo.page) {cb, pageSize ->
        def startY = lingo.getY(dslWriter)
        lingo.table.totalWidth = lingo.locationValue('width', dslWriter)
        lingo.table.writeSelectedRows 0, -1, lingo.getX(dslWriter), startY, cb
        LastPosition.lastY = startY - lingo.table.totalHeight
      }
    } else if (lingo.list) {
      dslWriter.column(lingo) {ColumnText columnText ->
        lingo.list.items.each {
          it.leading = lingo.leading * lingo.fontSize
        }
        columnText.addElement lingo.list
      }
    }
  }

}