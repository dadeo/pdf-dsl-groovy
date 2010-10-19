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


class TextTokenizerTest extends TestCase {
  private TextTokenizer tokenizer

  protected void setUp() {
    tokenizer = new TextTokenizer()
  }

  void test_tokenize_no_tokens() {
    assert tokenizer.tokenize("text with no tokens") == [[text:"text with no tokens", tokens:[]]]
  }

  void test_tokenize_text_and_token() {
    assert tokenizer.tokenize("<a>text with token</a>") == [[text:"text with token", tokens:["a"]]]
  }

  void test_tokenize_text_with_multiple_tokens() {
    assert tokenizer.tokenize("<a><b>text with token</b></a>") == [[text:"text with token", tokens:["a", "b"]]]
  }
  
  void test_tokenize_multiple_text_with_tokens() {
    assert tokenizer.tokenize("text <a>with <b>tokens</b> and</a> otherwise") == [
        [text:"text ", tokens:[]],
        [text:"with ", tokens:["a"]],
        [text:"tokens", tokens:["a", "b"]],
        [text:" and", tokens:["a"]],
        [text:" otherwise", tokens:[]],
    ]
  }

}