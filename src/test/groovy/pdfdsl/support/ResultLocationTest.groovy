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
    def location = new ResultLocation("+", new Location(CLOSURE1), new Location(CLOSURE2))
    assertEquals 18, location.value(ARG1, ARG2)
  }
  
  void test_value_minus() {
    def location = new ResultLocation("-", new Location(CLOSURE1), new Location(CLOSURE2))
    assertEquals 12, location.value(ARG1, ARG2)
  }

  void test_value_multiply() {
    def location = new ResultLocation("*", new Location(CLOSURE1), new Location(CLOSURE2))
    assertEquals 45, location.value(ARG1, ARG2)
  }

  void test_value_divide() {
    def location = new ResultLocation("/", new Location(CLOSURE1), new Location(CLOSURE2))
    assertEquals 5, location.value(ARG1, ARG2)
  }

}