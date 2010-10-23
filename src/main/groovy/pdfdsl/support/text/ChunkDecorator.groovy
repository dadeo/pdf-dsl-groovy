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
package pdfdsl.support.text

import com.lowagie.text.Chunk
import com.lowagie.text.Font


class ChunkDecorator {
  private Map<String, Serializable> settings

  ChunkDecorator(Map<String, Serializable> settings) {
    this.settings = settings
  }

  void decorate(Chunk chunk, List<String> tokens) {
    def font = chunk.font
    def color = chunk.font.color
    tokens.each {
      if(it.tag == "font") {
        font = settings.namedFonts[it.attributes.name]
      }
      if(it.tag == "color") {
        color = settings.namedColors[it.attributes.name]
      }
      if(it.tag == "u") {
        chunk.setUnderline(0.2f, -2f)
      }
    }
    chunk.font = new Font(font)
    chunk.font.color = color
  }
}