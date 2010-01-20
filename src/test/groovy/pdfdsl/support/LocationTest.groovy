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


class LocationTest extends GroovyTestCase {
  static final float RESULT = 8

  void test_constructor_cache_by_default() {
    def invoked = 0

    def valueClosure = {rect, mapWrapper ->
      ++invoked
      if (invoked > 1) fail("location value is not being cached")
      RESULT
    }

    def location = new Location("", valueClosure)

    assertEquals 8, location.value(null, null)
    assertEquals 8, location.value(null, null)
    assertEquals 1, invoked
  }

  void test_constructor_cache_disabled() {
    def invoked = 0

    def valueClosure = {rect, mapWrapper ->
      ++invoked
      RESULT
    }

    def location = new Location("location1", valueClosure, false)

    assertEquals 8, location.value(null, null)
    assertEquals 8, location.value(null, null)
    assertEquals 2, invoked
  }

  void test_constructor_number() {
    def location = new Location(5)
    assertTrue location.invoked
    assertEquals 5f, location.result
  }

  void test_constructor_location_number() {
    def location = new Location(new Location(5))
    assertTrue location.invoked
    assertEquals 5f, location.result
  }

  void test_constructor_location_closure() {
    def closure = {a, b -> }
    def location = new Location(new Location("", closure))
    assertFalse location.invoked
    assertSame closure, location.valueClosure
  }

  void test_plus_location_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })
    def location2 = new Location(3)

    def resultLocation = location1 + location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "+", resultLocation.operation
  }

  void test_plus_location_second_not_already_invoked() {
    def location1 = new Location(5)
    def location2 = new Location("", {a, b -> })

    def resultLocation = location1 + location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "+", resultLocation.operation
  }

  void test_plus_location_both_already_invoked() {
    def location1 = new Location(5)
    def location2 = new Location(3)

    def resultLocation = location1 + location2

    assertEquals 8f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_plus_number_original_already_invoked() {
    def location1 = new Location(5)

    def resultLocation = location1 + 4

    assertEquals 9f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_plus_number_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })

    def resultLocation = location1 + 4

    assertSame location1, resultLocation.location1
    assertEquals 4, resultLocation.location2.result
    assertEquals "+", resultLocation.operation
  }

  void test_minus_location_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })
    def location2 = new Location(3)

    def resultLocation = location1 - location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "-", resultLocation.operation
  }

  void test_minus_location_second_not_already_invoked() {
    def location1 = new Location(5)
    def location2 = new Location("", {a, b -> })

    def resultLocation = location1 - location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "-", resultLocation.operation
  }

  void test_minus_location_both_already_invoked() {
    def location1 = new Location(5)
    def location2 = new Location(3)

    def resultLocation = location1 - location2

    assertEquals 2f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_minus_number_original_already_invoked() {
    def location1 = new Location(7)

    def resultLocation = location1 - 4

    assertEquals 3f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_minus_number_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })

    def resultLocation = location1 - 4

    assertSame location1, resultLocation.location1
    assertEquals 4, resultLocation.location2.result
    assertEquals "-", resultLocation.operation
  }

  void test_multiply_location_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })
    def location2 = new Location(3)

    def resultLocation = location1 * location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "*", resultLocation.operation
  }

  void test_multiply_location_second_not_already_invoked() {
    def location1 = new Location(5)
    def location2 = new Location("", {a, b -> })

    def resultLocation = location1 * location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "*", resultLocation.operation
  }

  void test_multiply_location_both_already_invoked() {
    def location1 = new Location(5)
    def location2 = new Location(3)

    def resultLocation = location1 * location2

    assertEquals 15f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_multiply_number_original_already_invoked() {
    def location1 = new Location(7)

    def resultLocation = location1 * 4

    assertEquals 28f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_multiply_number_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })

    def resultLocation = location1 * 4

    assertSame location1, resultLocation.location1
    assertEquals 4, resultLocation.location2.result
    assertEquals "*", resultLocation.operation
  }

  void test_div_location_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })
    def location2 = new Location(3)

    def resultLocation = location1 / location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "/", resultLocation.operation
  }

  void test_div_location_second_not_already_invoked() {
    def location1 = new Location(5)
    def location2 = new Location("", {a, b -> })

    def resultLocation = location1 / location2

    assertSame location1, resultLocation.location1
    assertSame location2, resultLocation.location2
    assertEquals "/", resultLocation.operation
  }

  void test_div_location_both_already_invoked() {
    def location1 = new Location(15)
    def location2 = new Location(3)

    def resultLocation = location1 / location2

    assertEquals 5f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_div_number_original_already_invoked() {
    def location1 = new Location(28)

    def resultLocation = location1 / 4

    assertEquals 7f, resultLocation.result
    assertTrue resultLocation.invoked
  }

  void test_div_number_original_not_already_invoked() {
    def location1 = new Location("", {a, b -> })

    def resultLocation = location1 / 4

    assertSame location1, resultLocation.location1
    assertEquals 4, resultLocation.location2.result
    assertEquals "/", resultLocation.operation
  }

  void test_toString_not_invoked() {
    def location = new Location("my location", {a, b -> 5})
    assertEquals "my location", location.toString()
  }

  void test_toString_invoked() {
    def location = new Location("my location", {a, b -> 5})
    location.value(null, null)
    assertEquals "5.0", location.toString()
  }
}