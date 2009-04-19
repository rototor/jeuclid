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

package cTree;

import org.w3c.dom.Element;

public class CFences extends CElement {

    public CFences(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.FENCES;
    }

    public static CFences createFenced(final CElement inhalt) {
        final CFences fences = (CFences) CElementHelper.createAll(inhalt
                .getElement(), "mfenced", "mfenced", CRolle.UNKNOWN, null);
        fences.appendPraefixAndChild(inhalt);
        inhalt.setCRolle(CRolle.GEKLAMMERT);
        return fences;
    }

    public static CElement condCreateFenced(final CElement inhalt,
            final CMessage didIt) {
        if (inhalt instanceof CFences || inhalt instanceof CNum
                || inhalt instanceof CIdent) {
            didIt.setMessage(false);
            return inhalt;
        } else {
            final CFences fences = (CFences) CElementHelper
                    .createAll(inhalt.getElement(), "mfenced", "mfenced",
                            CRolle.UNKNOWN, null);
            fences.appendPraefixAndChild(inhalt);
            inhalt.setCRolle(CRolle.GEKLAMMERT);
            didIt.setMessage(true);
            return fences;
        }
    }

    public CElement getInnen() {
        return this.getFirstChild();
    }

    public boolean isFencedMin1() {
        if (this.getInnen() != null && (this.getInnen() instanceof CMinTerm)) {
            final CMinTerm innen = (CMinTerm) this.getInnen();
            if (innen.getValue() != null && innen.getValue() instanceof CNum) {
                final CNum wert = (CNum) innen.getValue();
                if (wert.getValue() == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean hatGleichenBetrag(final CElement cE2) {
        if (cE2 instanceof CFences) {
            return this.getInnen().hatGleichenBetrag(
                    ((CFences) cE2).getInnen());
        } else if (cE2 instanceof CMinTerm) {
            return this.hatGleichenBetrag(((CMinTerm) cE2).getValue());
        }
        return false;
    }

    @Override
    public void normalize() {
        if (this.hasChildC()) {
            // falls das Child eine +Row ist, das als einziges
            // eine Malrow enthält kann die Plusrow verschwinden
            final CElement firstRow = this.getFirstChild();
            if ((firstRow instanceof CPlusRow)
                    && (firstRow.getExtPraefix() == null)
                    && firstRow.hasChildC()) {
                final CElement secondRow = firstRow.getFirstChild();
                if ((secondRow instanceof CTimesRow) && !secondRow.hasNextC()) {
                    this.replaceChild(secondRow, firstRow, true, false);
                }
            }
            firstRow.normalizeTreeAndSiblings();
        }
    }
}
