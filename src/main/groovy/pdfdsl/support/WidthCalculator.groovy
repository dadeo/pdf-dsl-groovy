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


class WidthCalculator {
  def calculateFor(lingo) {
    def calculatedWidths = explicitWidths(lingo)
    def columnsWithoutExplicitWidths = 0
    def totalSpaceAvailable = new ResultLocation("-", Locations.right, Locations.left)
    def totalSpacingNeeded = lingo.spacing * (lingo.CHILDREN.size() - 1)
    def totalExplicitColumnWidths = calculatedWidths.inject(0) {total, width ->
      if(width) {
        if((total instanceof Location) || (width instanceof Location)) {
          new ResultLocation("+", total, width)
        } else {
          total + width
        }
      } else {
        ++columnsWithoutExplicitWidths
        total
      }
    }
    def totalConsumedSpace = totalExplicitColumnWidths ? new ResultLocation("+", totalSpacingNeeded, totalExplicitColumnWidths) : totalSpacingNeeded
    def spaceAvailableForColumns = new ResultLocation("-", totalSpaceAvailable, totalConsumedSpace)
    def implicitWidth = new ResultLocation("/", spaceAvailableForColumns, columnsWithoutExplicitWidths)

    for(int index = 0; index < calculatedWidths.size(); ++index) {
      if(!calculatedWidths[index]){
        calculatedWidths[index] = implicitWidth
      }
    }

    calculatedWidths
  }

  private def explicitWidths(lingo) {
    if(lingo.widths?.size() > lingo.CHILDREN.size()) {
      throw new RuntimeException("columns widths[] has more widths than there are columns")
    }
    def widths = new Object[lingo.CHILDREN.size()]
    for(int i = 0; i < widths.size(); ++i) {
      def childWidth = lingo.CHILDREN[i].width
      widths[i] = childWidth ? childWidth : (lingo.widths?.size() > 0 && lingo.widths[i]) ? lingo.widths[i] : null
    }
    widths
  }
}