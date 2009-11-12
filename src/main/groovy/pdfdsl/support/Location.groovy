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


class Location {
  private valueClosure

  Location() {
    this(0)
  }

  Location(Number value) {
    this.valueClosure = {a, b -> value}
  }

  Location(valueClosure) {
    this.valueClosure = valueClosure
  }

  float value(Rectangle rect, MapWrapper mapWrapper) {
    valueClosure(rect, mapWrapper)
  }

  def plus(Location location) {
    new ResultLocation("+", this, location)
  }

  def minus(Location location) {
    new ResultLocation("-", this, location)
  }

  def div(Location location) {
    new ResultLocation("/", this, location)
  }

  def multiply(Location location) {
    new ResultLocation("*", this, location)
  }

  def plus(Number location) {
    plus(new Location({a, b -> location}))
  }

  def minus(Number location) {
    minus(new Location({a, b -> location}))
  }

  def div(Number location) {
    div(new Location({a, b -> location}))
  }

  def multiply(Number location) {
    multiply(new Location({a, b -> location}))
  }

  def negative() {
    new ResultLocation("-", new Location(0), this)
  }
}