package pdfdsl.support


class CommandDefinitionTest extends GroovyTestCase {
  private static final COMMAND_OBJECT = new Object()

  void test_no_attribute_mappings() {
    def definition = new CommandDefinition(COMMAND_OBJECT)
    assertEquals([a: 1, b: 2, c: 3, d: 4, COMMAND_OBJECT: COMMAND_OBJECT], definition.build(a: 1, b: 2, c: 3, d: 4))
  }

  void test_attribute_mappings() {
    def definition = new CommandDefinition(COMMAND_OBJECT, [:], [a:'x', d:'z'])
    assertEquals([x: 1, b: 2, c: 3, z: 4, COMMAND_OBJECT: COMMAND_OBJECT], definition.build(a: 1, b: 2, c: 3, d: 4))
  }

}