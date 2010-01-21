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

class ColumnsCommand extends InternalCommand {
  def closure

  private columns = []

  ColumnsCommand() {
    defaults = [sectionSpacing: 0]
  }

  def column(Closure closure) {
    column [:], closure
  }

  def column(Map lingo, Closure closure) {
    columns << [lingo: this.lingo + lingo, closure:closure]
  }

  def stampWith(DslWriter dslWriter) {
    def lastYs = []
    def lastY = LastPosition.lastY
    columns.each {column ->
      LastPosition.lastY = lastY
      def commands = []
      column.closure.resolveStrategy = Closure.DELEGATE_FIRST
      column.closure.delegate = new PdfLingo(commands, column.lingo.mapIn)
      use(BasicPdfLingo) {
        column.closure()
      }

      commands.eachWithIndex { command, index ->
        if(index == 0) {
          command.lingo.setY(column.lingo.unaltered('at')[1])
        } else {
          command.lingo.setY(LastPosition.lastY.minus(command.lingo.sectionSpacing))
        }
        command.stampWith dslWriter
      }
      lastYs << LastPosition.lastY
    }
    LastPosition.lastY = lastYs.inject(lastYs[0]) {v1, v2 -> Math.min(v1, v2) }
  }

}