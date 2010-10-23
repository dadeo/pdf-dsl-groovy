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

import java.util.regex.Matcher


class TextTokenizer {
  private static final TOKEN_EXPRESSION = /<(\/?\w+?)( .+?)?>/
  private static final ATTRIBUTE_EXPRESSION = /(\w+)=(['"])(.+?)\2/

  List tokenize(String text) {
    def tagValidator = new TagValidator()
    def result = []
    def tokens = []
    Matcher matcher = text =~ TOKEN_EXPRESSION
    def lastStart = 0
    while(matcher.find()) {
      if(matcher.start() != lastStart) {
        result << [text: text[lastStart..matcher.start() - 1], tokens: tokens.clone()]
      }
      if(matcher.group(1).startsWith("/")) {
        tagValidator.closeTag(matcher.group(1)[1..-1])
        tokens.pop()
      } else {
        tagValidator.openTag(matcher.group(1))
        final def tag = [tag: matcher.group(1)]
        if(matcher.groupCount() == 2) {
          def attributes = parseAttributes(matcher.group(2))
          if(attributes) {
            tag.attributes = attributes
          }
        }
        tokens << tag
      }
      lastStart = matcher.end()
    }
    if(lastStart != text.size()) {
      result << [text:text[lastStart..-1], tokens:tokens]
    }
    tagValidator.validate()
    result
  }

  private parseAttributes(attributeString) {
    def attributes = [:]
    Matcher matcher = attributeString =~ ATTRIBUTE_EXPRESSION
    while(matcher.find()) {
      attributes[matcher.group(1)] = matcher.group(3)
    }
    attributes
  }
}