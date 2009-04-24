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

import java.util.ArrayList;

import org.w3c.dom.Element;

public class CSqrt extends CElement {

    public CSqrt(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.SQRT;
    }

    @Override
    public boolean hatGleichenBetrag(final CElement cE2) {
        if (cE2 instanceof CSqrt) {
            return this.getRadikand().hatGleichenBetrag(
                    ((CSqrt) cE2).getRadikand());
        }
        return false;
    }

    @Override
    public boolean istGleichartigesMonom(final CElement e2) {
        if (e2 instanceof CSqrt) {
            return ((CSqrt) e2).getRadikand().istGleichartigesMonom(
                    this.getRadikand());
        } else if (e2 instanceof CMinTerm) {
            return this.hatGleichenBetrag(((CMinTerm) e2).getValue());
        }
        return false;
    }

    @Override
    public void normalize() {
    };

    public CElement getRadikand() {
        return this.getFirstChild();
    }

    public static ArrayList<CSqrt> createRootList(final CElement cE1,
            final CElement cE2) {
        final ArrayList<CSqrt> rootList = new ArrayList<CSqrt>();
        rootList.add((CSqrt) cE1);
        rootList.add((CSqrt) cE2);
        return rootList;
    }

    public static CSqrt createSqrt(final CElement cElement) {
        final CSqrt newSqrt = (CSqrt) CElementHelper.createAll(cElement
                .getElement(), "msqrt", "msqrt", cElement.getCRolle(),
                cElement.getExtPraefix());
        newSqrt.appendChild(cElement.cloneCElement(false));
        newSqrt.getRadikand().setCRolle(CRolle.RADIKANT);
        return newSqrt;
    }

    // jedes Output-listenelement erhält das Vorzeichen */: vor seiner Wurzel,
    // wie man
    // es für das Multiplizieren braucht nur das erste nicht.
    public static ArrayList<CElement> getRadikandList(
            final ArrayList<CSqrt> list) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        for (final CSqrt root : list) {
            result.add(root.getRadikand());
        }
        boolean first = true;
        for (int i = 0; i < result.size(); i++) {
            if (first) {
                first = false;
            } else {
                if (list.get(i).getExtPraefix() != null) {
                    result.get(i).setExtPraefix(
                            (Element) list.get(i).getExtPraefix().cloneNode(
                                    true));
                }
            }
        }
        return result;
    }

    @Override
    public boolean hasNumberValue() {
        return this.getRadikand().hasNumberValue()
                && (this.getRadikand().getNumberValue() >= 0);
    }

    @Override
    public double getNumberValue() {
        return Math.sqrt(this.getRadikand().getNumberValue());
    }

    @Override
    public int internalCompare(final CElement o) {
        final CSqrt f2 = (CSqrt) o;
        return this.getRadikand().compareTo(f2.getRadikand());
    }

}
