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


class LineSplitter {
  private static final LINE_START = /^\s*\|/
  private static final LINE_END = /\|\s*$/

  List<String> split(String text) {
    if(text[0] == '|') {
      def result = []
      String line = null
      def reader = new BufferedReader(new StringReader(text))
      def lineOut = ""
      while((line = reader.readLine()) != null) {
        if(line.trim()) {
          if(line =~ LINE_START) {
            line = line.replaceFirst(LINE_START, "")
          }
          if(line =~ LINE_END) {
            line = line.replaceFirst(LINE_END, "")
            result << (lineOut ? lineOut + " " + line : line)
            lineOut = ""
          } else {
            lineOut += (lineOut ? " " + line : line)
          }
        }
      }
      if(lineOut) {
        result << lineOut
      }
      result
    } else {
      [text]
    }
  }
  
}