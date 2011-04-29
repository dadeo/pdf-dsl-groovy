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

class DataSubstitutor {

  String substitute(String text, Map data) {
    def matcher = text =~ "#\\{(.+?)}"

    StringBuffer sb = new StringBuffer()
    while (matcher.find()) {
      def value = data[matcher.group(1)]
      if (value == null) {
        throw new RuntimeException("unable to substitute null value for ${matcher.group(0)}")
      }
      matcher.appendReplacement sb, value as String
    }
    matcher.appendTail sb

    sb.toString()
  }
}
