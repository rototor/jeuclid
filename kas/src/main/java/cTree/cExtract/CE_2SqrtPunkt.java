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

import java.util.ArrayList;

import org.w3c.dom.Element;

import cTree.CElement;
import cTree.CFences;
import cTree.CMessage;
import cTree.CRolle;
import cTree.CSqrt;
import cTree.CTimesRow;
import cTree.CType;
import cTree.cDefence.DefHandler;

public class CE_2SqrtPunkt extends CE_1 {

    @Override
    public CElement extract(final CElement parent,
            final ArrayList<CElement> selection, final CElement cE2) {
        // selection.get(0) ist das Produkt, Parent die Wurzel
        System.out.println("Lazy extracting");
        if (!this.canExtract(parent, selection)) {
            return selection.get(0);
        }

        // Praefix sichern
        final CRolle rolle = parent.getCRolle();
        final CTimesRow newArg = this
                .createExtraction(parent, selection, cE2);
        newArg.correctInternalPraefixesAndRolle();
        final CMessage didIt = new CMessage(false);
        final CElement newChild = CFences.condCreateFenced(newArg, didIt);
        newArg.setCRolle(CRolle.GEKLAMMERT);
        ExtractHandler.getInstance().insertOrReplace(parent, newChild,
                selection, true);
        newChild.setCRolle(rolle);
        return DefHandler.getInst().conDefence(newChild.getParent(),
                newChild, newChild.getFirstChild(), didIt.isMessage());
    }

    @Override
    protected CTimesRow createExtraction(final CElement parent,
            final ArrayList<CElement> selection, final CElement defElement) {
        System.out.println("Extract sqrt punkt ");
        // Analyse des bisherigen Produkts
        final CTimesRow oldProduct = (CTimesRow) selection.get(0);
        final CElement oldFirst = selection.get(0).getFirstChild();
        final CElement newFirst = oldFirst.cloneCElement(false);
        final CSqrt firstSqrt = CSqrt.createSqrt(newFirst);
        newFirst.setCRolle(CRolle.RADIKANT);
        final boolean hasDiv = oldFirst.getNextSibling().hasExtDiv();
        final Element newOp = (Element) oldFirst.getNextSibling()
                .getExtPraefix().cloneNode(true);
        final boolean timesRowAgain = (oldFirst.getNextSibling().hasNextC());
        CElement newSecond = null;
        if (timesRowAgain) {
            final ArrayList<CElement> factorList = ((CTimesRow) oldProduct
                    .cloneCElement(false)).getMemberList();
            factorList.remove(0);
            newSecond = CTimesRow.createRow(factorList);
            ((CTimesRow) newSecond).correctInternalPraefixesAndRolle();
            if (hasDiv) {
                ((CTimesRow) newSecond).toggleAllVZButFirst(false);
            }
        } else {
            newSecond = oldFirst.getNextSibling().cloneCElement(false);
        }
        final CSqrt secSqrt = CSqrt.createSqrt(newSecond);
        newSecond.setCRolle(CRolle.RADIKANT);
        secSqrt.setExtPraefix(newOp);
        final CTimesRow newChild = CTimesRow.createRow(CTimesRow.createList(
                firstSqrt, secSqrt));
        newChild.correctInternalPraefixesAndRolle();
        return newChild;
    }

    @Override
    protected boolean canExtract(final CElement parent,
            final ArrayList<CElement> selection) {
        // Man kann nur die ganz linken Elemente extrahieren
        if (selection.size() != 1
                || !selection.get(0).getCType().equals(CType.TIMESROW)) {
            System.out.println("Wir extrahieren nur aus einem Produkt");
            return false;
        }
        return true;
    }
}
