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

class LineCommand extends InternalCommand {

  LineCommand() {
    defaults = [width: 1]
  }

  def stampWith(DslWriter dslWriter) {
    dslWriter.withDirectContent(lingo.page) {cb, pageSize ->
      cb.lineWidth = lingo.width.value(pageSize, lingo)
      cb.moveTo((float) lingo.at[0].value(pageSize, lingo), (float) lingo.at[1].value(pageSize, lingo))
      cb.lineTo((float) lingo.to[0].value(pageSize, lingo), (float) lingo.to[1].value(pageSize, lingo))
      cb.colorStroke = lingo.color
      cb.stroke()
    }
  }

}