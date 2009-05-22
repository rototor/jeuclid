/*
 * Copyright 2009 Erhard Kuenzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cTree.cExtract;

import cTree.CElement;
import cTree.CTimesRow;
import cViewer.TransferObject;
import cViewer.ViewerFactory;

public class CE_2StrichPunkt extends CE_1 {

    @Override
    public CE_1 getExt(final CE_Event event) {
        CE_1 extracter = new CE_1();
        for (final CElement cEl : event.getSelection()) {
            if (!(cEl instanceof CTimesRow)) {
                return extracter;
            }
        }
        final String[] strArray = new String[3];
        strArray[0] = "Vorzeichen";
        strArray[1] = "erster Faktor";
        strArray[2] = "letzter Faktor";
        final TransferObject to = new TransferObject(strArray);
        ViewerFactory.getInst().getComboDialog(to);
        final String s = to.getResult();
        if ("Vorzeichen".equals(s)) {
            extracter = new CE_2StrichPunktVZ();
        } else if ("erster Faktor".equals(s)) {
            extracter = new CE_2StrichPunkt1();
        } else if ("letzter Faktor".equals(s)) {
            extracter = new CE_2StrichPunktL();
        }
        return extracter;
    }
}
