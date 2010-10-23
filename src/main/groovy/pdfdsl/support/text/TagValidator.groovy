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


class TagValidator {
  private tags = []

  void openTag(tag) {
    tags << tag
  }

  void closeTag(tag) {
    if(tag == tags[-1]) {
      tags.pop()
    } else {
      throw new RuntimeException("close tag '$tag' does not match open tag '${tags[-1]}'")
    }
  }

  void validate() {
    if(tags) {
      throw new RuntimeException("tag${tags.size() > 1 ? "s" : ""} not terminated correctly: '${tags.join("', '")}'")
    }
  }
}
