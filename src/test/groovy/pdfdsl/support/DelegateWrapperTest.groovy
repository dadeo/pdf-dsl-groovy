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
