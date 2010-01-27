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


class ColumnsCommandTest extends GroovyTestCase {

  void test_calculatedWidths_children_have_no_widths() {
    ColumnsCommand command = new ColumnsCommand()
    command.lingo = [
        spacing: 50,
        CHILDREN: [
            [:],
            [:],
            [:],
            [:],
        ]
    ]

    def widths = command.calculateWidths()

    assertEquals 4, widths.size()
    widths.each { width ->
      doAssertWidth width
    }
  }

  private doAssertWidth(width) {
    assertTrue width instanceof ResultLocation
    assertEquals "/", width.operation
    assertEquals 4f, width.location2.value(null, null)

    assertTrue width.location1 instanceof ResultLocation
    assertEquals "-", width.location1.operation
    assertEquals 150f, width.location1.location2.value(null, null)

    def location1 = width.location1.location1
    assertTrue location1 instanceof ResultLocation
    assertEquals "-", location1.operation
    assertEquals "right", location1.location1.description
    assertEquals "left", location1.location2.description
  }

}