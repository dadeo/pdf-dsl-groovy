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

class Locations {

  static def center = new Location("center", {Rectangle rect, MapWrapper mapWrapper -> (rect.right - rect.left) / 2})
  static def right = new Location("right", {Rectangle rect, MapWrapper mapWrapper -> rect.right })
  static def left = new Location("left", {Rectangle rect, MapWrapper mapWrapper -> rect.left })

  static def top = new Location("top", {Rectangle rect, MapWrapper mapWrapper -> rect.top })
  static def bottom = new Location("bottom", {Rectangle rect, MapWrapper mapWrapper -> rect.bottom })
  static def middle = new Location("middle", {Rectangle rect, MapWrapper mapWrapper -> (rect.top - rect.bottom) / 2})

  static def fontSize = new Location("fontSize", {Rectangle rect, MapWrapper mapWrapper -> mapWrapper.fontSize })

  static def startY = new Location("startY", {Rectangle rect, MapWrapper mapWrapper -> LastPosition.startY })
  static def lastY = new Location("lastY", {Rectangle rect, MapWrapper mapWrapper -> LastPosition.lastY}, false)
}