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
  private lastY   // TODO: not thread safe
  private lastYs = [] // TODO: not thread safe

  ColumnsCommand() {
    defaults = [sectionSpacing: 0]
  }

  def stampWith(DslWriter dslWriter) {
  }

  def preChildExecute(childCommand, int index) {
    def merged = super.preChildExecute(childCommand, index)
    if (index == 0) {
      lastY = LastPosition.lastY
    } else {
      LastPosition.lastY = lastY
    }
    if (lingo.widths) {
      merged.width = lingo.widths[index]
      def offset = lingo.widths[0..<index].inject(new Location(0)) {a, b -> a+b+merged.spacing}
      merged.x = lingo.x + offset
    }
    merged
  }
  
  def postChildExecute(childCommand, int index) {
    lastYs << LastPosition.lastY
  }

  def postChildrenExecute() {
    LastPosition.lastY = lastYs.inject(lastYs[0]) {v1, v2 -> Math.min(v1, v2) }
  }

}