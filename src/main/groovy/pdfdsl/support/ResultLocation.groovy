package pdfdsl.support

import com.lowagie.text.Rectangle


class ResultLocation extends Location {
  private String operation
  private Location location1
  private Location location2

  ResultLocation(String operation, Location location1, Location location2) {
    this.operation = operation
    this.location1 = location1
    this.location2 = location2
  }

  def value(Rectangle rect, MapWrapper mapWrapper) {
    def value1 = location1.value(rect, mapWrapper)
    def value2 = location2.value(rect, mapWrapper)
    switch(operation) {
      case "-":
        return value1 - value2
      case "+":
        return value1 + value2
      case "/":
        return value1 / value2
      case "*":
        return value1 * value2
    }
  }

}