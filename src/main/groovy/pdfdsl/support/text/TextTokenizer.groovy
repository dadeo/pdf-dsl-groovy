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
  private static final TOKEN_EXPRESSION = /<(.+?)>/

  List tokenize(String text) {
    def result = []
    def tokens = []
    Matcher matcher = text =~ TOKEN_EXPRESSION
    def lastStart = 0
    while(matcher.find()) {
      if(matcher.start() != lastStart) {
        result << [text: text[lastStart..matcher.start() - 1], tokens: tokens.clone()]
      }
      if(matcher.group(1).startsWith("/")) {
        tokens.remove(matcher.group(1)[1..-1])
      } else {
        tokens << matcher.group(1)
      }
      lastStart = matcher.end()
    }
    if(lastStart != text.size()) {
      result << [text:text[lastStart..-1], tokens:tokens]
    }
    result
  }

}