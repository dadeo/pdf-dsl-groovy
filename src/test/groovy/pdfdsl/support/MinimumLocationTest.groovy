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


class MinimumLocationTest extends GroovyTestCase {
  static final RECTANGLE = new Rectangle(1, 1, 1, 1)
  static final MAP_WRAPPER = new MapWrapper([:])

  void test_value_number_number() {
    assertEquals 300, new MinimumLocation(300, 500).value(null, null)
    assertEquals 300, new MinimumLocation(500, 300).value(null, null)
  }

  void test_value_location_location() {
    assertEquals 200, new MinimumLocation(new Location(200), new Location(600)).value(null, null)
    assertEquals 200, new MinimumLocation(new Location(600), new Location(200)).value(null, null)
  }

  void test_value_number_location() {
    assertEquals 100, new MinimumLocation(100, new Location(200)).value(null, null)
    assertEquals 100, new MinimumLocation(200, new Location(100)).value(null, null)
  }

  void test_value_location_number() {
    assertEquals 300, new MinimumLocation(new Location(300), 400).value(null, null)
    assertEquals 300, new MinimumLocation(new Location(400), 300).value(null, null)
  }

  void test_value_passes_args() {
    def mock1 = mockControl(3)
    def mock2 = mockControl(6)
    assertEquals 3, new MinimumLocation(mock1.proxy, mock2.proxy).value(RECTANGLE, MAP_WRAPPER)
    assert mock1.invoked
    assert mock2.invoked
  }

  private mockControl(result) {
    def control = [invoked: false]
    def mock = [
        value: {v1, v2 ->
          assertSame RECTANGLE, v1
          assertSame MAP_WRAPPER, v2
          control.invoked = true
          result
        }
    ]
    control.proxy = mock
    control
  }
}