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

import com.lowagie.text.Rectangle


class ResultLocationTest extends GroovyTestCase {
  private static final ARG1 = new Rectangle(0,0)
  private static final ARG2 = new MapWrapper([:])
  private static final CLOSURE1 = {a, b ->
    assertSame ARG1, a
    assertSame ARG2, b
    15
  }
  private static final CLOSURE2 = {a, b ->
    assertSame ARG1, a
    assertSame ARG2, b
    3
  }

  void test_value_plus() {
    def location = new ResultLocation("+", new Location("", CLOSURE1), new Location("", CLOSURE2))
    assertEquals 18, location.value(ARG1, ARG2)
  }
  
  void test_value_minus() {
    def location = new ResultLocation("-", new Location("", CLOSURE1), new Location("", CLOSURE2))
    assertEquals 12, location.value(ARG1, ARG2)
  }

  void test_value_multiply() {
    def location = new ResultLocation("*", new Location("", CLOSURE1), new Location("", CLOSURE2))
    assertEquals 45, location.value(ARG1, ARG2)
  }

  void test_value_divide() {
    def location = new ResultLocation("/", new Location("", CLOSURE1), new Location("", CLOSURE2))
    assertEquals 5, location.value(ARG1, ARG2)
  }

  void test_value_not_cached_when_location1_is_not_cachable() {
    def count = 0
    def location = new ResultLocation("+", new Location("", {a, b-> ++count; 5}, false), new Location("", CLOSURE2))
    assertEquals 8, location.value(ARG1, ARG2)
    assertEquals 8, location.value(ARG1, ARG2)
    assertEquals 2, count
  }

  void test_value_not_cached_when_location2_is_not_cachable() {
    def count = 0
    def location = new ResultLocation("+", new Location("", CLOSURE2), new Location("", {a, b-> ++count; 5}, false))
    assertEquals 8, location.value(ARG1, ARG2)
    assertEquals 8, location.value(ARG1, ARG2)
    assertEquals 2, count
  }

  void test_value_cached_when_both_locations_are_cachable() {
    def count = 0
    def location = new ResultLocation("+", new Location("", CLOSURE2), new Location("", {a, b-> ++count; 5}))
    assertEquals 8, location.value(ARG1, ARG2)
    assertEquals 8, location.value(ARG1, ARG2)
    assertEquals 1, count
  }

  void test_toString_location1_not_invoked() {
    assertEquals "(center + 5.0)", new ResultLocation("+", Locations.center, new Location(5)).toString()
  }

  void test_toString_location2_not_invoked() {
    assertEquals "(5.0 / center)", new ResultLocation("/", new Location(5), Locations.center).toString()
  }
  
  void test_toString_both_locations_invoked() {
    assertEquals "8.0", new ResultLocation("+", new Location(5), new Location(3)).toString()
  }
}