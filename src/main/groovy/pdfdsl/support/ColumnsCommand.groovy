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


class ColumnsCommand extends InternalCommand {
  def closure

  private columns = []

  def column(closure) {
    columns << closure
  }

  def stampWith(DslWriter dslWriter) {
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure.delegate = this
    closure()

    def lastYs = []
    columns.each {columnClosure ->
      def commands = []
      columnClosure.resolveStrategy = Closure.DELEGATE_FIRST
      columnClosure.delegate = new PdfLingo(commands, lingo.mapIn)
      use(PdfLingo) {
        columnClosure()
      }
      commands.each { it.stampWith dslWriter }
      lastYs << LastPosition.lastY
    }
    LastPosition.lastY = lastYs.inject(lastYs[0]) {v1, v2 -> Math.min(v1, v2) }
  }

}