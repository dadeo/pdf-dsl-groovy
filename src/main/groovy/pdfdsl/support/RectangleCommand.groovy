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

import java.awt.Color

class RectangleCommand extends InternalCommand {

  RectangleCommand() {
    defaults = [borderWidth: 1, borderColor: Color.BLACK]
  }

  def stampWith(DslWriter dslWriter) {
    dslWriter.withDirectContent(lingo.page) {cb, pageSize ->
      def attrs = lingo
      
      cb.lineWidth = attrs.borderWidth ?: 1

      def plotRectangle = {
        def x = (float) attrs.at[0].value(pageSize, attrs)
        def y = (float) attrs.at[1].value(pageSize, attrs)
        def width = (float) attrs.width.value(pageSize, attrs)
        def height = (float) attrs.height.value(pageSize, attrs)
        cb.rectangle(
            (float) x,
            (float) y,
            (float) width,
            (float) -height
        )
      }

      if(attrs.backgroundColor) {
        plotRectangle()
        cb.colorFill = attrs.backgroundColor
        cb.fill()
      }

      plotRectangle()
      cb.colorStroke = attrs.borderColor
      cb.stroke()

    }
  }

}