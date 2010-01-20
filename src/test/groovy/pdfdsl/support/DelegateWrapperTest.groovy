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

class DelegateWrapperTest extends GroovyTestCase {
  private DelegateWrapper wrapper

  protected void setUp() {
    wrapper = new DelegateWrapper("hello world")
    wrapper.overrides.clap = { "clap clap" }
    wrapper.overrides.clapTimes = { "$it claps" }
  }

  void test_delegates_to_wrapped_instance_method__no_parameters() {
    assertEquals 11, wrapper.size()
  }

  void test_delegates_to_wrapped_instance_method__parameters() {
    assertEquals "lo w", wrapper.substring(3, 7)
  }

  void test_delegates_to_wrapped_instance_property() {
    assertTrue wrapper.bytes instanceof byte[]
  }

  void test_delegates_to_user_defined_actions__no_parameters() {
    assertEquals "clap clap", wrapper.clap()
  }

  void test_delegates_to_user_defined_actions__parameters() {
    assertEquals "3 claps", wrapper.clapTimes(3)
  }

  void test_delegates_to_user_defined_property() {
    assertEquals "clap clap", wrapper.clap
  }

}
