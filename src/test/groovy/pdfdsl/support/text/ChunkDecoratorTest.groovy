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

import junit.framework.TestCase
import com.lowagie.text.Chunk
import com.lowagie.text.Font
import java.awt.Color
import com.lowagie.text.pdf.BaseFont


class ChunkDecoratorTest extends TestCase {
  private static final FAMILY = 1
  private static final SIZE = 2
  private static final STYLE = 3
  private static final COLOR = Color.BLUE

  private static final Font FONT_1 = new Font(FAMILY, SIZE, STYLE, COLOR)
  private static final Font FONT_2 = new Font()

  void test_decorate_named_font() {
    Map<String, Serializable> settings = [namedFonts:[text1:FONT_1, text2:FONT_2]]

    Chunk chunk = new Chunk()

    new ChunkDecorator(settings).decorate(chunk, ["nf:text2", "nf:text1"])

    assert chunk.font.family == FAMILY
    assert chunk.font.size == SIZE
    assert chunk.font.style == STYLE
    assert chunk.font.color != COLOR
  }

  void test_decorate_named_color() {
    Map<String, Serializable> settings = [namedFonts:[text1:FONT_1, text2:FONT_2], namedColors:["red": Color.red]]

    Chunk chunk = new Chunk()

    new ChunkDecorator(settings).decorate(chunk, ["nf:text2", "nc:red", "nf:text1"])

    assert chunk.font.family == FAMILY
    assert chunk.font.size == SIZE
    assert chunk.font.style == STYLE
    assert chunk.font.color == Color.red
  }

  void test_decorate_underline() {
    Map<String, Serializable> settings = [:]

    Chunk chunk = new Chunk()

    new ChunkDecorator(settings).decorate(chunk, ["u"])

    final def underLineAttributes = chunk.attributes?.get(Chunk.UNDERLINE)
    assert underLineAttributes
    assert underLineAttributes[0][1][2] == -2f
  }

}