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