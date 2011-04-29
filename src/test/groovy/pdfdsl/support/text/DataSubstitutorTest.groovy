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

class DataSubstitutorTest extends TestCase {
  private static final Map MAP = [mom: "Jan", dad: "Dan"]
  DataSubstitutor substitutor = new DataSubstitutor()

  void test_substitute_empty_string() {
    assert substitutor.substitute("", MAP) == ""
  }

  void test_no_substitution() {
    assert substitutor.substitute("hi", MAP) == "hi"
  }

  void test_single_subsitution_of_placeholder() {
    assert substitutor.substitute("hi #{mom}", MAP) == "hi Jan"
  }

  void test_multiple_subsitutions_of_same_placeholder() {
    assert substitutor.substitute("hi #{mom}, bye #{mom}", MAP) == "hi Jan, bye Jan"
  }

  void test_multiple_subsitutions_of_different_placeholder() {
    assert substitutor.substitute("hi #{mom}, bye #{dad}", MAP) == "hi Jan, bye Dan"
  }

  void test_single_subsitutions__placeholder_only() {
    assert substitutor.substitute("#{mom}", MAP) == "Jan"
  }

  void test_substitute_null_test() {
    try {
      substitutor.substitute("#{son}", MAP)
      fail("should have failed because key is not found in map")
    }
    catch (e) {
      assert e.message == "unable to substitute null value for #{son}"
    }
  }

}
