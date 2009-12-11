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

import java.util.HashMap;

import cTree.CElement;
import cTree.CTimesRow;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;
import cViewer.TransferObject;
import cViewer.ViewerFactory;

public class CE_2StrichPunkt extends CExtractBase {

    private final HashMap<String, CExtractBase> extracters;

    private final String[] strArray;

    public CE_2StrichPunkt() {
        this.extracters = new HashMap<String, CExtractBase>();
        this.strArray = new String[3];
        this.strArray[0] = "Vorzeichen";
        this.strArray[1] = "erster Faktor";
        this.strArray[2] = "letzter Faktor";
        this.extracters.put(this.strArray[0], new CE_2StrichPunktVZ());
        this.extracters.put(this.strArray[1], new CE_2StrichPunkt1());
        this.extracters.put(this.strArray[2], new CE_2StrichPunktL());
    }

    @Override
    public C_Changer getChanger(final C_Event event) {
        final C_Changer extracter = new C_No(event);
        extracter.setEvent(event);
        for (final CElement cEl : event.getSelection()) {
            if (!(cEl instanceof CTimesRow)) {
                return extracter;
            }
        }
        final TransferObject to = new TransferObject(this.strArray);
        ViewerFactory.getInst().getComboDialog(to);
        final CExtractBase ext = this.extracters.get(to.getResult());
        return ext.getChanger(event);
    }
}
