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

class MinimumLocation extends Location {
  private location1
  private location2

  MinimumLocation(location1, location2) {
    this.location1 = forceToLocation(location1)
    this.location2 = forceToLocation(location2)
    this.invoked = false

    valueClosure = {rect, mapWrapper ->
      def value1 = this.location1.value(rect, mapWrapper)
      def value2 = this.location2.value(rect, mapWrapper)
      Math.min(value1, value2)
    }
  }

  String toString() {
    String.valueOf(
        invoked ? result :
          (location1.invoked && location2.invoked) ? Math.min(location1.result, location2.result) : "min($location1, $location2)"
    )
  }

  private forceToLocation(location) {
    location instanceof Location ? location : new Location(location)
  }
}