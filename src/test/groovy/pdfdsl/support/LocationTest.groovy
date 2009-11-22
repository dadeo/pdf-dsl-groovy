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

  void test_cache_by_default() {
    def invoked = 0

    def valueClosure = { rect, mapWrapper ->
      ++invoked
      if(invoked > 1) fail("location value is not being cached")
      RESULT
    }

    def location = new Location(valueClosure)

    assertEquals 8, location.value(null, null)
    assertEquals 8, location.value(null, null)
    assertEquals 1, invoked
  }
  
  void test_cache_disabled() {
    def invoked = 0

    def valueClosure = { rect, mapWrapper ->
      ++invoked
      RESULT
    }

    def location = new Location(valueClosure, false)

    assertEquals 8, location.value(null, null)
    assertEquals 8, location.value(null, null)
    assertEquals 2, invoked
  }

}