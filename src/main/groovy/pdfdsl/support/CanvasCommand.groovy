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


class CanvasCommand extends InternalCommand {
  def closure

  CanvasCommand() {
    defaults = [padding: 0]
  }

  def stampWith(DslWriter dslWriter) {
    lingo.mapIn["at"] = [Locations.left, Locations.top]
    def pageSize = dslWriter.getPageSize(lingo.page)
    lingo.mapIn["width"] = pageSize.width
    lingo.mapIn["height"] = pageSize.height
    dslWriter.column(lingo) {columnText ->
      closure.delegate = columnText
      closure.resolveStrategy = Closure.DELEGATE_FIRST
      closure columnText
    }
  }

}