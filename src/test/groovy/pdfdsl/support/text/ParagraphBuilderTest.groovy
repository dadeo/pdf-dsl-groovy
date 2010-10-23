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


class ParagraphBuilderTest extends TestCase {
  private ParagraphBuilder builder
  private static final Font FONT = new Font()

  protected void setUp() {
     builder = new ParagraphBuilder()
  }

  void test_build() {
    def tokenizer = new MockFor(TextTokenizer)
    tokenizer.demand.tokenize("paragraph text") { [[text:"chunk 1", tags:[[tag:"a"]]], [text:"chunk 2", tags:[[tag:"b"]]]] }

    int chunkDecoratorInvocations = 0
    def chunkDecorator = new MockFor(ChunkDecorator)
    chunkDecorator.demand.decorate() { chunk, tags -> assert chunk.getContent() == "chunk 1"; assert tags == [[tag:"a"]]; ++chunkDecoratorInvocations }
    chunkDecorator.demand.decorate() { chunk, tags -> assert chunk.getContent() == "chunk 2"; assert tags == [[tag:"b"]]; ++chunkDecoratorInvocations }

    builder.tokenizer = tokenizer.proxyInstance()
    builder.chunkDecorator = chunkDecorator.proxyInstance()

    Paragraph paragraph = builder.build("paragraph text", FONT)

    assert paragraph
    assert paragraph.font.is(FONT)
    assert paragraph.chunks.size() == 2
    assert paragraph.chunks[0].getContent() == "chunk 1"
    assert paragraph.chunks[1].getContent() == "chunk 2"
    assert chunkDecoratorInvocations == 2
  }

}