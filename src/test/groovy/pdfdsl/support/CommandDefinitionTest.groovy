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