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

class LocationPdfLingo {

  static getRight(target) {
    Locations.right
  }

  static getLeft(target) {
    Locations.left
  }

  static getCenter(target) {
    Locations.center
  }

  static getTop(target) {
    Locations.top
  }

  static getBottom(target) {
    Locations.bottom
  }

  static getMiddle(target) {
    Locations.middle
  }

  static getFontSize(target) {
    Locations.fontSize
  }

  static getStartY(target) {
    Locations.startY
  }

  static getLastY(target) {
    Locations.lastY
  }

  static plus(Integer target, Location location) {
    new ResultLocation("+", new Location(target), location)
  }

  static minus(Integer target, Location location) {
    new ResultLocation("-", new Location(target), location)
  }

  static multiply(Integer target, Location location) {
    new ResultLocation("*", new Location(target), location)
  }

  static div(Integer target, Location location) {
    new ResultLocation("/", new Location(target), location)
  }

  static min(target, location1, location2) {
    new MinimumLocation(location1, location2)
  }

  static getInch(Number number) {
    getInches number
  }

  static getInches(Number number) {
    (number * 72) as float
  }

}