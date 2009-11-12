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
package pdfdsl

import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.Chapter
import com.lowagie.text.pdf.PdfWriter
import com.lowagie.text.PageSize


class TestPdfFactory {
  static def createPdf(String filename) {
    def document = new Document(PageSize.LETTER);
    PdfWriter.getInstance(document, new FileOutputStream(filename));
    document.open();
    def hello = new Paragraph("(English:) hello, " +
        "(Esperanto:) he, alo, saluton, (Latin:) heu, ave, " +
        "(French:) all\u00f4, (Italian:) ciao, (German:) hallo, he, heda, holla, " +
        "(Portuguese:) al\u00f4, ol\u00e1, hei, psiu, bom d\u00eda, (Dutch:) hallo, dag, " +
        "(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, " +
        "(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, " +
        "(Norwegian:) hei; morn, (Papiamento:) halo; hallo; k\u00ed tal, " +
        "(Faeroese:) hall\u00f3, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
    def universe = new Chapter("To the Universe:", 1);
    def section = null
    document.add(universe);
    def people = new Chapter("To the People:", 2);
    document.add(people);
    def animals = new Chapter("To the Animals:", 3);
    document.add(animals);
    document.close();
  }

}