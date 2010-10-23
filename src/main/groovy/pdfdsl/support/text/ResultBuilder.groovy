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


class ResultBuilder {
  private AttributeParser attributeParser = new AttributeParser()
  private String text
  private tokens = []
  private tags = []
  private int nextStart = 0

  ResultBuilder(text) {
    this.text = text
  }

  void openTag(tagName, attributeString, start, end) {
    extractTextBeforeTagAsToken(start)
    tags << createTagFrom(tagName, attributeString)
    nextStart = end
  }

  void closeTag(start, end) {
    extractTextBeforeTagAsToken(start)
    tags.pop()
    nextStart = end
  }

  List toResults() {
    if (nextStart != text.size()) {
      tokens << [text: text[nextStart..-1], tags: tags]
    }
    tokens
  }

  private Map createTagFrom(tagName, attributeString) {
    final def tag = [tag: tagName]
    def attributes = attributeParser.parseAttributes(attributeString)
    if (attributes) {
      tag.attributes = attributes
    }
    return tag
  }

  private def extractTextBeforeTagAsToken(start) {
    if (start != nextStart) {
      tokens << [text: text[nextStart..start - 1], tags: tags.clone()]
    }
  }
}
