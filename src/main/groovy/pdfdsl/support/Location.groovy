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
  protected valueClosure
  protected boolean invoked
  protected float result
  protected boolean cache = true
  protected String description

  Location() {
    this(0)
  }

  Location(Location value) {
    if (value.invoked) {
      this.result = value.result
      this.invoked = true
    } else {
      this.valueClosure = value.valueClosure
    }
    this.description = value.description
  }

  Location(Number value) {
    this.result = value.toFloat()
    this.invoked = true
  }

  Location(String description, valueClosure) {
    this.valueClosure = valueClosure
    this.description = description
  }

  Location(String description, valueClosure, cache) {
    this.valueClosure = valueClosure
    this.cache = cache
    this.description = description
  }

  final float value(Rectangle rect, MapWrapper mapWrapper) {
    if (computed()) {
      result
    } else {
      invoked = cache
      result = valueClosure(rect, mapWrapper) as float
    }
  }

  def plus(Location location) {
    if (invoked && location.invoked) {
      plus location.result
    } else {
      new ResultLocation("+", this, location)
    }
  }

  def minus(Location location) {
    if (invoked && location.invoked) {
      minus location.result
    } else {
      new ResultLocation("-", this, location)
    }
  }

  def div(Location location) {
    if (invoked && location.invoked) {
      div location.result
    } else {
      new ResultLocation("/", this, location)
    }
  }

  def multiply(Location location) {
    if (invoked && location.invoked) {
      multiply location.result
    } else {
      new ResultLocation("*", this, location)
    }
  }

  def plus(Number location) {
    if (invoked) {
      new Location(result + location.toFloat())
    } else {
      plus new Location(location)
    }
  }

  def minus(Number location) {
    if (invoked) {
      new Location(result - location.toFloat())
    } else {
      minus new Location(location)
    }
  }

  def div(Number location) {
    if (invoked) {
      new Location(result / location.toFloat())
    } else {
      div new Location(location)
    }
  }

  def multiply(Number location) {
    if (invoked) {
      new Location(result * location.toFloat())
    } else {
      multiply new Location(location)
    }
  }

  def negative() {
    new ResultLocation("-", new Location(0), this)
  }

  String toString() {
    computed() ? String.valueOf(result) : description
  }

  boolean computed() {
    invoked && cache
  }

}