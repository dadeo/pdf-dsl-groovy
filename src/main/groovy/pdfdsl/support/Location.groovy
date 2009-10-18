package pdfdsl.support

import com.lowagie.text.Rectangle


class Location {
  private valueClosure

  Location() {
    this(0)
  }

  Location(Number value) {
    this.valueClosure = {a,b->value}
  }

  Location(valueClosure) {
    this.valueClosure = valueClosure
  }

  def value(Rectangle rect, MapWrapper mapWrapper) {
    valueClosure rect, mapWrapper
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

  def plus(int location) {
    plus(new Location({a,b->location}))
  }

  def minus(int location) {
    minus(new Location({a,b->location}))
  }

  def div(int location) {
    div(new Location({a,b->location}))
  }

  def multiply(int location) {
    multiply(new Location({a,b->location}))
  }
}