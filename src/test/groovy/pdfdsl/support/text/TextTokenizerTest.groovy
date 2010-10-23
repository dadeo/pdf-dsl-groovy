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
import static pdfdsl.util.AssertionHelper.shouldFail

class TextTokenizerTest extends TestCase {
  private TextTokenizer tokenizer

  protected void setUp() {
    tokenizer = new TextTokenizer()
  }

  void test_tokenize_no_tokens() {
    assert tokenizer.tokenize("text with no tokens") == [[text:"text with no tokens", tokens:[]]]
  }

  void test_tokenize_text_and_token() {
    assert tokenizer.tokenize("<a>text with token</a>") == [[text:"text with token", tokens:[[tag:"a"]]]]
  }

  void test_tokenize_text_with_multiple_tokens() {
    assert tokenizer.tokenize("<a><b>text with token</b></a>") == [[text:"text with token", tokens:[[tag:"a"], [tag:"b"]]]]
  }
  
  void test_tokenize_multiple_text_with_tokens() {
    assert tokenizer.tokenize("text <a>with <b>tokens</b> and</a> otherwise") == [
        [text:"text ", tokens:[]],
        [text:"with ", tokens:[[tag:"a"]]],
        [text:"tokens", tokens:[[tag:"a"], [tag:"b"]]],
        [text:" and", tokens:[[tag:"a"]]],
        [text:" otherwise", tokens:[]],
    ]
  }
  
  void test_tokenize_tag_with_single_attribute() {
    assert tokenizer.tokenize("<a b='2'>text</a>") == [[text:"text", tokens:[[tag:"a", attributes:[b:"2"]]]]]
  }

  void test_tokenize_tag_with_multiple_attributes() {
    assert tokenizer.tokenize("<a b='2' c='3'>text</a>") == [[text:"text", tokens:[[tag:"a", attributes:[b:"2", c:"3"]]]]]
  }

  void test_tokenize_tag_attributes_value_wrapped_with_half_quotes() {
    assert tokenizer.tokenize("<a b='2'>text</a>") == [[text:"text", tokens:[[tag:"a", attributes:[b:"2"]]]]]
  }

  void test_tokenize_tag_attributes_value_wrapped_with_quotes() {
    assert tokenizer.tokenize('<a b="2">text</a>') == [[text:"text", tokens:[[tag:"a", attributes:[b:"2"]]]]]
  }

  void test_tokenize_tag_attributes_value_may_contain_half_quote() {
    assert tokenizer.tokenize('<a b="\'2\'">text</a>') == [[text:"text", tokens:[[tag:"a", attributes:[b:"'2'"]]]]]
  }

  void test_tokenize_tag_attributes_value_may_contain_quote() {
    assert tokenizer.tokenize("<a b='\"2\"'>text</a>") == [[text:"text", tokens:[[tag:"a", attributes:[b:'"2"']]]]]
  }

  void test_tokenize_tag_attributes_name_may_be_more_than_one_character() {
    assert tokenizer.tokenize("<a abc='2'>text</a>") == [[text:"text", tokens:[[tag:"a", attributes:[abc:"2"]]]]]
  }

  void test_tokenize_tag_attributes_value_may_be_more_than_one_character() {
    assert tokenizer.tokenize("<a b='hello world'>text</a>") == [[text:"text", tokens:[[tag:"a", attributes:[b:"hello world"]]]]]
  }

  void test_tokenize_tag_name_may_be_more_than_one_character() {
    assert tokenizer.tokenize("<abc a='2'>text</abc>") == [[text:"text", tokens:[[tag:"abc", attributes:[a:"2"]]]]]
  }

  void test_tokenize_throws_exception_when_tag_not_terminated() {
    shouldFail("tag not terminated correctly: 'abc'") {
      tokenizer.tokenize("<abc a='2'>text")
    }
  }

  void test_tokenize_throws_exception_when_multiple_tags_not_terminated() {
    shouldFail("tags not terminated correctly: 'abc', 'xyz'") {
      tokenizer.tokenize("<abc a='2'><xyz>text")
    }
  }

  void test_tokenize_throws_exception_when_nested_tag_not_terminated() {
    shouldFail("close tag 'abc' does not match open tag 'xyz'") {
      tokenizer.tokenize("<abc a='2'><xyz>text</abc>")
    }
  }

  void test_tokenize_throws_exception_when_tag_not_terminated_correctly() {
    shouldFail("close tag 'xyz' does not match open tag 'abc'") {
      tokenizer.tokenize("<abc a='2'>text</xyz>")
    }
  }

}