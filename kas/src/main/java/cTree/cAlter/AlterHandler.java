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

package cTree.cAlter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import cTree.CElement;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.DOMElementMap;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;

public class AlterHandler {
    private volatile static AlterHandler uniqueInstance;

    public HashMap<String, CA_Base> getAlters;

    @SuppressWarnings("unchecked")
    private AlterHandler() {
        this.getAlters = new HashMap<String, CA_Base>();
        final ArrayList<String> strings = new ArrayList<String>();
        // final java.net.URL chURL = AlterHandler.class
        // .getResource("/CA_DivA_Frac.java");
        // System.out.println("Changer: " + chURL);
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(
                    System.getProperty("user.dir") + "/Changers.txt"));

            String line = "";
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
            reader.close();
        } catch (final Exception e) {
            strings.add("CA_PotA_Times");
            strings.add("CA_Times_PotA");
            strings.add("CA_Times_Frac");
            strings.add("CA_Times_MinRaus");
            strings.add("CA_TimesRow_Sort");
            strings.add("CA_DivA_Frac");
            strings.add("CA_DivMin1_InMalMin1");
            strings.add("CA_Frac_InInteger");
            strings.add("CA_Frac_InSumme");
            strings.add("CA_Frac_InProdukt");
            strings.add("CA_Frac_InQuotient");
            strings.add("CA_Frac_InGemZahl");
            strings.add("CA_Frac_Kehrbruch");
            strings.add("CA_Frac_MinusKuerzen");
            strings.add("CA_Frac_Kuerzen");
            strings.add("CA_Frac_KuerzenGanz");
            strings.add("CA_Frac_Min1Vorziehen");
            strings.add("CA_GemZ_InFrac");
            strings.add("CA_GemZ_InSum");
            strings.add("CA_GemZ_Norm");
            strings.add("CA_GemZ_1Raus");
            strings.add("CA_Min1_InProdInMinTerm");
            strings.add("CA_Min1_InProdInSum");
            strings.add("CA_Min1_InProdInSumFirst");
            strings.add("CA_MinA_PlusMin1Mal");
            strings.add("CA_MinA_InMin1TimesA");
            strings.add("CA_MinVorziehenSumFencedSum");
            strings.add("CA_PlusRow_Sort");
            strings.add("CA_PlusRow_MinRaus");
            strings.add("CA_PrimeDecomposition");
            strings.add("CA_Extract");
            strings.add("CA_Entklammern");
            strings.add("CA_Verbinden");
        }
        Class c;
        for (final String s : strings) {
            try {
                c = Class.forName("cTree.cAlter." + s);
                final CA_Base a = (CA_Base) c.getConstructor().newInstance();
                a.register(this.getAlters);
            } catch (final Exception e) {
                System.err.println("Error3");
            }
        }
    }

    public static AlterHandler getInstance() {
        if (AlterHandler.uniqueInstance == null) {
            synchronized (DOMElementMap.class) {
                if (AlterHandler.uniqueInstance == null) {
                    AlterHandler.uniqueInstance = new AlterHandler();
                }
            }
        }
        return AlterHandler.uniqueInstance;
    }

    public HashMap<String, C_Changer> getOptions(final ArrayList<CElement> els) {
        final HashMap<String, C_Changer> options = new HashMap<String, C_Changer>();
        final C_Event event = new C_Event(els);
        for (final CA_Base ca : this.getAlters.values()) {
            final C_Changer c = ca.getChanger(event);
            if (c.canDo()) {
                options.put(ca.getText(), c);
            }
        }
        return options;
    }

    public CElement change(final ArrayList<CElement> els,
            final String actionCommand) {
        final CElement cAct = els.get(0);
        if (this.getAlters.containsKey(actionCommand)) {
            cAct.removeCActiveProperty();
            final CD_Event event = new CD_Event(false);
            final CElement el = this.getAlters.get(actionCommand).doIt(event);
            System.out.println("AlterHandler " + el.getCType() + " "
                    + el.getText());
            if (event.isDoDef()) {
                event.setCElement(el);
                DefHandler.getInst().getChanger(event).doIt(
                        new CD_Event(false));
            }
            el.setCActiveProperty();
            return el;
        } else {
            return cAct;
        }
    }

}
