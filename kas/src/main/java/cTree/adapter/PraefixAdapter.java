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

package cTree.adapter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class PraefixAdapter extends ElementAdapter {

    protected Element praefix;

    public Element getExtPraefix() {
        return this.praefix;
    }

    /*
     * gibt "*" "+" "-" "/" oder "" zurück InvisibleTimes wird z.B. zu "*"
     */
    public String getPraefixAsString() {
        if (this.praefix == null) {
            return "";
        } else {
            if (this.praefix.getFirstChild() != null
                    && this.praefix.getFirstChild().getAttributes() != null
                    && this.praefix.getFirstChild().getAttributes()
                            .getNamedItem("name") != null
                    && "InvisibleTimes".equals(this.praefix.getFirstChild()
                            .getAttributes().getNamedItem("name")
                            .getNodeValue())) {
                return "*";
            }
            if (this.praefix.getFirstChild() != null
                    && this.praefix.getFirstChild().getAttributes() != null
                    && this.praefix.getFirstChild().getAttributes()
                            .getNamedItem("name") != null
                    && "InvisiblePlus".equals(this.praefix.getFirstChild()
                            .getAttributes().getNamedItem("name")
                            .getNodeValue())) {
                return "+";
            }
            final String s = this.praefix.getTextContent();
            if ("·".equals(s)) {
                return "*";
            }
            if ("*".equals(s) || "+".equals(s) || "-".equals(s)
                    || ":".equals(s)) {
                return s;
            } else {
                System.out
                        .println("Vorzeichen konnte in cTree.adapter.PraefixAdapter.getPraefixAsString() nicht ermittelt werden.");
                return " ";
            }
        }
    }

    public void setExtPraefix(final Element vz) {
        this.praefix = vz;
    }

    /*
     * falls Praefix vorhanden ist, wird der Wert gesetzt, sonst ein neuer
     * Praefix erzeugt. falls ein parent existiert, wird das praefix davor
     * gesetzt. (Nicht bei Clones).
     */
    public void setPraefix(final String s) {
        final Element parent = (Element) this.getElement().getParentNode();
        if (!"".equals(s)) {
            if (this.praefix == null) {
                final Element newOp = EElementHelper.createOp(this
                        .getElement(), s);
                if (parent != null) {
                    parent.insertBefore(newOp, this.getElement());
                }
                this.setExtPraefix(newOp);
            } else {
                this.praefix.setTextContent(s);
            }
        } else { // s=""
            if (this.praefix != null) {
                if (parent != null) {
                    parent.removeChild(this.praefix);
                }
                this.praefix = null;
            }
        }
    }

    /*
     * falls Praefix vorhanden ist, wird er beim Parent deregistriert. die
     * Referenz des CElements wird null gesetzt. evtl fehlerhaft
     */
    public void removePraefix() {
        final Element el = this.getElement();
        final Node parent = el.getParentNode();
        if (parent != null) {
            parent.removeChild(el.getPreviousSibling());
            this.setExtPraefix(null);
        }

    }

    public void setPlusToEmpty() {
        if (this.getExtPraefix() != null
                && "+".equals(this.getExtPraefix().getTextContent())) {
            final Node parent = this.getElement().getParentNode();
            if (parent != null) {
                parent.removeChild(this.getExtPraefix());
            }
            this.setExtPraefix(null);
        }
    }

    public void setTimesToEmpty() {
        if (EElementHelper.isTimesOp(this.getExtPraefix())) {
            final Node parent = this.getElement().getParentNode();
            if (parent != null) {
                ((Element) this.getElement().getParentNode())
                        .removeChild(this.getExtPraefix());
            }
            this.setExtPraefix(null);
        }
    }

    public void setEmptyTo(final String s) {
        if (this.getExtPraefix() == null) {
            this.setPraefix(s);
        }
    }

    public void setEmptyToPlus() {
        this.setEmptyTo("+");
    }

    public void setEmptyToTimes() {
        this.setEmptyTo("·");
    }

    public void setEmptyToMinus() {
        this.setEmptyTo("-");
    }

    public void setEmptyToDiv() {
        this.setEmptyTo(":");
    }

    public void toggleEmptyAndPlus() {
        if (this.getExtPraefix() == null) {
            this.setEmptyToPlus();
        } else {
            this.setPlusToEmpty();
        }
    }

    // ------- Tester ----------------
    public boolean hasExtPraefix() {
        return (this.praefix != null);
    }

    public boolean hasExtNull() {
        return (this.praefix == null);
    }

    public boolean hasExtPlus() {
        return (this.praefix != null && "+".equals(this.praefix
                .getTextContent()));
    }

    public boolean hasExtEmptyOrPlus() {
        return (this.praefix == null || "+".equals(this.praefix
                .getTextContent()));
    }

    public boolean hasExtEmptyOrTimes() {
        return (this.praefix == null || EElementHelper.isTimesOp(this
                .getExtPraefix()));
    }

    public boolean hasExtTimes() {
        return (this.praefix != null)
                && EElementHelper.isTimesOp(this.getExtPraefix());
    }

    public boolean hasExtMinus() {
        return this.praefix != null
                && "-".equals(this.praefix.getTextContent());
    }

    public boolean hasExtDiv() {
        return this.praefix != null
                && ":".equals(this.praefix.getTextContent());
    }

    public boolean hasMinOrDiv() {
        return this.hasExtMinus() || this.hasExtDiv();
    }

    // -- Ausgabe
    public static void showPraefix(final Element e) {
        System.out.println("*VZ " + e.getNodeName() + " "
                + e.getTextContent());
    }

}
