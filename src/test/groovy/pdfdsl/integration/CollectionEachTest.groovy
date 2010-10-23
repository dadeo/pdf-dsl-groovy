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
package pdfdsl.integration

import junit.framework.TestCase
import pdfdsl.PdfDsl

class CollectionEachTest extends TestCase {

  void test_each_on_collection_works_in_dsl() {
    def data = [columns: [1, 2, 3, 4, 5], sections: [1, 2, 3], lines: [1, 2, 3, 4]]

    def template = new PdfDsl().createTemplate {
      page {
        columns {
          data.columns.each {
            column {
              data.sections.each {
                section {
                  data.lines.each {
                    line()
                  }
                }
              }
            }
          }
        }
      }
    }

    assert template.commands.size() == 1
    assert template.commands[0].COMMAND_NAME == "page"

    final def columnsCommand = template.commands[0].CHILDREN[0]
    assert columnsCommand.COMMAND_NAME == "columns"
    assert columnsCommand.CHILDREN.size() == 5

    final def columnCommand = columnsCommand.CHILDREN[0]
    assert columnCommand.COMMAND_NAME == "column"
    assert columnCommand.CHILDREN.size() == 3

    final def sectionCommand = columnCommand.CHILDREN[0]
    assert sectionCommand.COMMAND_NAME == "section"
    assert sectionCommand.CHILDREN.size() == 4

    final def lineCommand = sectionCommand.CHILDREN[0]
    assert lineCommand.COMMAND_NAME == "line"
  }

}