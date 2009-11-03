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

import com.lowagie.text.pdf.BaseFont


class PdfLingo {

  private def commands
  private def defaultSettings

  PdfLingo(commands, defaults) {
    this.commands = commands
    this.defaultSettings = defaults
  }

  def font(lingo) {
    if (!lingo.id) throw new RuntimeException("Font id required")
    def embedded = lingo?.embedded ? BaseFont.EMBEDDED : BaseFont.NOT_EMBEDDED
    def encoding = lingo?.encoding ?: BaseFont.WINANSI
    if (lingo.name || lingo.file) {
      defaultSettings.configuredFonts[lingo.id] = BaseFont.createFont(lingo.name ?: lingo.file, encoding, embedded)
    } else {
      throw new RuntimeException("Font name or file required")
    }
  }

  def line(lingo) {
    commands << new LineCommand(lingo: defaultSettings + lingo)
  }

  def write(lingo) {
    commands << new WriteCommand(lingo: defaultSettings + lingo)
  }

  def section(lingo, closure) {
    SectionCommand command = new SectionCommand(lingo: defaultSettings + lingo)
    closure.delegate = command
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure()
    commands << command
  }

  def table(lingo, closure) {
    TableCommand command = new TableCommand(lingo: defaultSettings + lingo)
    closure.delegate = command
    closure()
    commands << command
  }

  def rectangle(lingo) {
    commands << new RectangleCommand(lingo: defaultSettings + lingo)
  }

  def getRight() {
    Locations.right
  }

  def getLeft() {
    Locations.left
  }

  def getCenter() {
    Locations.center
  }

  def getTop() {
    Locations.top
  }

  def getBottom() {
    Locations.bottom
  }

  def getMiddle() {
    Locations.middle
  }

  def getFontSize() {
    Locations.fontSize
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

}