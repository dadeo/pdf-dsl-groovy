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

import com.lowagie.text.pdf.BaseFont
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import pdfdsl.PdfDsl

public class ConcurrencyTest extends GroovyTestCase {

  void testIt() {
    def output = Collections.synchronizedSet([] as Set)
    int poolSize = 30;
    int maxPoolSize = 30;
    long keepAliveTime = 10;
    final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(500);
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
    (1..300).each { count ->
      threadPool.execute({
        try {
          def pdfTemplate = new PdfDsl().createTemplate {
            font id: 'helvetica', name: BaseFont.HELVETICA
            namedFont id: 'text', font: 'helvetica', size: 12

            page number: 1, {
              include template: dynamicFormTitle, args: ["Section 1", 1]
              include template: dynamicFormTitle, args: ["Section 2", 1]
            }

          }
          byte[] bytes = pdfTemplate.create()
          output << bytes.size()

        }
        catch (e) {
          println("Error occurred processing $count, message = ${e.message}")
        }
      } as Runnable)
    }

    while (queue.size()) {
      sleep 5
    }
    threadPool.shutdown()

    assertEquals 1, output.size()
  }

  private static dynamicFormTitle = { title, totalPageNumber ->
    section at: [center, lastY], justified: center, {
      line text: title, font: 'helvetica'
    }

    section at: [right, lastY], justified: right, {
      line text: "Page 1 of " + totalPageNumber, font: 'text'
    }
    hline width: 2, before: 2.mm, after: 1.mm

    spacer height: 2.mm
  }

}