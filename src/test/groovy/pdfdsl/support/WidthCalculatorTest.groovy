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


class WidthCalculatorTest extends GroovyTestCase {
  private calculator

  protected void setUp() {
    calculator = new WidthCalculator()
  }

  void test_calculatedWidths_children_have_no_widths() {
    def lingo = [
        spacing: 50,
        CHILDREN: [
            [:],
            [:],
            [:],
            [:],
        ]
    ]

    def widths = calculator.calculateFor(lingo)

    assertEquals lingo.CHILDREN.size(), widths.size()
    widths.each { width ->
      assertEquals "(((right - left) - 150.0) / 4.0)", width.toString()
    }
  }

  void test_calculatedWidths_child_with_numeric_width() {
    def lingo = [
        spacing: 50,
        CHILDREN: [
            [:],
            [width:100],
            [:],
        ]
    ]

    def widths = calculator.calculateFor(lingo)

    assertEquals lingo.CHILDREN.size(), widths.size()
    assertEquals "(((right - left) - 200.0) / 2.0)", widths[0].toString()
    assertEquals 100, widths[1]
    assertEquals "(((right - left) - 200.0) / 2.0)", widths[2].toString()
  }

  void test_calculatedWidths_child_with_location_width() {
    def lingo = [
        spacing: 50,
        CHILDREN: [
            [:],
            [width:new Location(25)],
            [:],
            [:],
            [width:new Location(75)],
        ]
    ]

    def widths = calculator.calculateFor(lingo)

    assertEquals lingo.CHILDREN.size(), widths.size()
    assertEquals "(((right - left) - (200.0 + (25.0 + 75.0))) / 3.0)", widths[0].toString()
    assertEquals "25.0", widths[1].toString()
    assertEquals "(((right - left) - (200.0 + (25.0 + 75.0))) / 3.0)", widths[2].toString()
    assertEquals "(((right - left) - (200.0 + (25.0 + 75.0))) / 3.0)", widths[3].toString()
    assertEquals "75.0", widths[4].toString()
  }

  void test_calculatedWidths_last_child_width_implied__widths_on_each_column() {
    def lingo = [
        spacing: 50,
        CHILDREN: [
            [width:new Location(25)],
            [width:new Location(75)],
            [:],
        ]
    ]

    def widths = calculator.calculateFor(lingo)

    assertEquals lingo.CHILDREN.size(), widths.size()
    assertEquals "25.0", widths[0].toString()
    assertEquals "75.0", widths[1].toString()
    assertEquals "(((right - left) - (100.0 + (25.0 + 75.0))) / 1.0)", widths[2].toString()
  }

  void test_calculatedWidths_last_child_width_implied() {
    def lingo = [
        spacing: 50,
        widths: [new Location(25), new Location(75)],
        CHILDREN: [
            [:],
            [:],
            [:],
        ]
    ]

    def widths = calculator.calculateFor(lingo)

    assertEquals lingo.CHILDREN.size(), widths.size()
    assertEquals "25.0", widths[0].toString()
    assertEquals "75.0", widths[1].toString()
    assertEquals "(((right - left) - (100.0 + (25.0 + 75.0))) / 1.0)", widths[2].toString()
  }

  void test_calculatedWidths_more_widths_than_columns() {
    def lingo = [
        spacing: 50,
        widths: [new Location(25), new Location(25), new Location(25), new Location(25)],
        CHILDREN: [
            [:],
            [:],
            [:],
        ]
    ]

    try {
      def widths = calculator.calculateFor(lingo)
      fail("too many widths, should fail")
    } catch (e) {
      assertEquals "columns widths[] has more widths than there are columns", e.message
    }

  }

}