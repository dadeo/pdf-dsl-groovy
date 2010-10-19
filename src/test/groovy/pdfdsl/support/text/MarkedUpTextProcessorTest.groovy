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
import com.lowagie.text.Paragraph
import groovy.mock.interceptor.MockFor
import com.lowagie.text.Font


class MarkedUpTextProcessorTest extends TestCase {
  private static final Paragraph PARAGRAPH_1 = new Paragraph()
  private static final Paragraph PARAGRAPH_2 = new Paragraph()
  private static final Font FONT = new Font()

  private MarkedUpTextProcessor processor
  private mockSplitter
  private mockParagraphBuilder

  protected void setUp() {
    mockSplitter = new MockFor(LineSplitter)
    mockParagraphBuilder = new MockFor(ParagraphBuilder)
  }

  private void finalizeSetup() {
    processor = new MarkedUpTextProcessor()
    processor.lineSplitter = mockSplitter.proxyInstance()
    processor.paragraphBuilder = mockParagraphBuilder.proxyInstance()
  }

  void test_process() {
    mockSplitter.demand.split("text") { ["line 1", "line 2"] }

    mockParagraphBuilder.demand.build() { text, font -> assert text == "line 1"; assert FONT.is(font); PARAGRAPH_1 }
    mockParagraphBuilder.demand.build() { text, font -> assert text == "line 2"; assert FONT.is(font); PARAGRAPH_2 }

    finalizeSetup()

    List<Paragraph> paragraphs = processor.process("text", FONT)
    assert paragraphs
    assert paragraphs.size() == 2
    assert paragraphs[0].is(PARAGRAPH_1)
    assert paragraphs[1].is(PARAGRAPH_2)
  }

}