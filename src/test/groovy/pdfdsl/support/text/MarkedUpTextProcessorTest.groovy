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

import com.lowagie.text.Font
import com.lowagie.text.Paragraph
import groovy.mock.interceptor.MockFor
import junit.framework.TestCase

class MarkedUpTextProcessorTest extends TestCase {
  private static final Paragraph PARAGRAPH_1 = new Paragraph()
  private static final Paragraph PARAGRAPH_2 = new Paragraph()
  private static final Font FONT = new Font()
  private static final String ORIGINAL_TEXT = "original-text"
  private static final String SUBSTITUTED_TEXT = "substituted-text"
  private static final Map DATA = [:]

  private MarkedUpTextProcessor processor
  private mockSplitter
  private mockParagraphBuilder
  private mockDataSubstitutor

  protected void setUp() {
    mockSplitter = new MockFor(LineSplitter)
    mockParagraphBuilder = new MockFor(ParagraphBuilder)
    mockDataSubstitutor = new MockFor(DataSubstitutor)
  }

  private void finalizeSetup() {
    processor = new MarkedUpTextProcessor()
    processor.lineSplitter = mockSplitter.proxyInstance()
    processor.paragraphBuilder = mockParagraphBuilder.proxyInstance()
    processor.dataSubstitutor = mockDataSubstitutor.proxyInstance()
  }

  void test_process() {
    mockDataSubstitutor.demand.substitute { text, data ->
      assertSame ORIGINAL_TEXT, text
      assertSame DATA, data
      SUBSTITUTED_TEXT
    }
    mockSplitter.demand.split(1) { text ->
      assert text == SUBSTITUTED_TEXT
      ["line 1", "line 2"]
    }

    mockParagraphBuilder.demand.build() { text, font -> assert text == "line 1"; assert FONT.is(font); PARAGRAPH_1 }
    mockParagraphBuilder.demand.build() { text, font -> assert text == "line 2"; assert FONT.is(font); PARAGRAPH_2 }

    finalizeSetup()

    List<Paragraph> paragraphs = processor.process(ORIGINAL_TEXT, FONT, DATA)
    assert paragraphs
    assert paragraphs.size() == 2
    assert paragraphs[0].is(PARAGRAPH_1)
    assert paragraphs[1].is(PARAGRAPH_2)
  }

}