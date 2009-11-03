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


class SectionCommand extends InternalCommand {

  SectionCommand() {
    defaults = [padding: 0]
  }

  private lines = []

  def line(map) {
    lines << map
  }

  def stampWith(DslWriter dslWriter) {
    def attrs = new MapWrapper(lingo)
    def coordinates = [at: lingo.at]
    def maxLength = 0
    lines.each {
      dslWriter.stamp(lingo + coordinates + it)
      coordinates = [at: [lingo.at[0], coordinates.at[1] - lingo.fontSize]]
      maxLength = Math.max(maxLength, attrs.getTextLength(it.text))
    }

    if (attrs.borderColor) {
      dslWriter.withDirectContent(lingo.page) {cb, pageSize ->

        def offset = 0
        if (attrs["justified"] == Locations.center) {
          offset = maxLength / 2
        } else if (attrs["justified"] == Locations.right) {
          offset = maxLength
        }

        def plotRectangle = {
          def y = attrs.at[1].value(pageSize, attrs) + attrs.fontSize
          cb.rectangle(
              (float) (attrs.at[0].value(pageSize, attrs) - offset - attrs.padding),
              (float) (y + attrs.padding),
              (float) (maxLength + (attrs.padding * 2)),
              (float) (coordinates.at[1] - y + attrs.fontSize - (attrs.padding * 2))
          )
        }

        plotRectangle()

        cb.colorStroke = attrs.borderColor
        cb.stroke()
      }
    }

  }

}