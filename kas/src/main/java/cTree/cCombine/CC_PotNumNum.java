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

package cTree.cCombine;

import cTree.CElement;
import cTree.CNum;

public class CC_PotNumNum extends CC_ {

    // parent basis und exponent können zusammengeholt sein, parent ist nur
    // producer

    @Override
    protected boolean canCombine(final CElement parent, final CElement basis,
            final CElement expo) {
        System.out.println("Repell pot num num?");
        try {
            final int basisZahl = ((CNum) basis).getValue();
            final int expZahl = ((CNum) expo).getValue();
            if (basisZahl != 0 || expZahl != 0) {
                return true;
            } else {
                return false;
            }
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected CElement createCombination(final CElement potenz,
            final CElement basis, final CElement expo) {
        System.out.println("Potenziere Zahlen");
        final int basisZahl = ((CNum) basis).getValue();
        final int expZahl = ((CNum) expo).getValue();
        final int newNr = (int) Math.pow(basisZahl, expZahl);
        final CNum newChild = CNum.createNum(potenz.getElement(), "" + newNr);
        newChild.setCRolle(potenz.getCRolle());
        return newChild;
    }

}
