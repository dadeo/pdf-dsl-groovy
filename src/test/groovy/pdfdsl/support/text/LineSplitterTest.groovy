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

class LineSplitterTest extends TestCase {
  private LineSplitter splitter

  protected void setUp() {
    splitter = new LineSplitter()
  }


  void test_split_no_line_markup() {
    def text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

    assert splitter.split(text) == [text]
  }

  void test_split_each_line_marked() {
    def text = """
              |Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.|
              |Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.|
          """

    List<String> out = splitter.split(text)

    assert out.size() == 2
    assert out[0] == "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    assert out[1] == "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
  }

  void test_split_with_word_wrap() {
    def text = """
              |Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor
              |incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
              |exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute
              |irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
              |pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
              |deserunt mollit anim id est laborum.|
          """

    List<String> out = splitter.split(text)

    assert out.size() == 1
    assert out[0] == "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
  }

  void test_split_multiple_lines_with_word_wrap() {
    def text = """
              |Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor
              |incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud|
              |exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute
              |irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
              |pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia|
              |deserunt mollit anim id est laborum.|
          """

    List<String> out = splitter.split(text)

    assert out.size() == 3
    assert out[0] == """Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud"""
    assert out[1] == """exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia"""
    assert out[2] == """deserunt mollit anim id est laborum."""
  }

  void test_split_lines_starting_with_spaces() {
    def text = """
              |a|
              |  b|
              |  c|
              |  d|
              |e|
              |  f|
          """

    List<String> out = splitter.split(text)

    assert out == ["a", "  b", "  c", "  d", "e", "  f"]
  }

  void test_split_no_blank_lines() {
    def text = """|a|
              |b|
              |c|"""

    List<String> out = splitter.split(text)

    assert out == ["a", "b", "c"]
  }

  void test_split_line_missing_markup() {
    def text = """|a|
              b
              |c| """

    shouldFail("Text with newline characters ('\\n') must be marked up with left margins characters '|'") {
      splitter.split(text)
    }
  }

  void test_split_with_blank_lines() {
    def text = """|
              |a|

              |b|

              |c|
          """

    List<String> out = splitter.split(text)

    assert out == ["a", "b", "c"]
  }

}