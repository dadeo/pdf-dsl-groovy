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
  private static final TOKEN_EXPRESSION = /<(\/)?(\w+?)( .+?)?>/

  List tokenize(String text) {
    def tagValidator = new TagValidator()
    def resultBuilder = new ResultBuilder(text)
    Matcher matcher = text =~ TOKEN_EXPRESSION

    while (matcher.find()) {
      String terminator = matcher.group(1)
      String tagName = matcher.group(2)
      String attributeString = matcher.group(3)

      if (terminator) {
        tagValidator.closeTag(tagName)
        resultBuilder.closeTag(matcher.start(), matcher.end())
      } else {
        tagValidator.openTag(tagName)
        resultBuilder.openTag(tagName, attributeString, matcher.start(), matcher.end())
      }
    }

    tagValidator.validate()
    resultBuilder.toResults()
  }

}