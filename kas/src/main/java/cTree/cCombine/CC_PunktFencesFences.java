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

import java.util.HashMap;

import cTree.CElement;
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.CPlusTerm;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;
import cViewer.TransferObject;
import cViewer.ViewerFactory;

public class CC_PunktFencesFences extends CC_Base {

    private CC_PunktFencedMinFencedMin cmm;

    private CC_PunktFencedPlusFencedPlus cpp;

    private CC_PunktFencedMinFencedPlus cmp;

    private CC_PunktFencedPlusFencedMin cpm;

    private CC_PunktFencedAnyFencedSum cas;

    private CC_PunktFencedSumFencedAny csa;

    private final String[] strArray;

    private final HashMap<String, C_Changer> changers;

    public CC_PunktFencesFences() {
        super();
        this.strArray = new String[3];
        this.strArray[0] = "Ersten Faktor spalten";
        this.strArray[1] = "Zweiten Faktor spalten";
        this.strArray[2] = "Jedes mit jedem";
        this.changers = new HashMap<String, C_Changer>();
        this.changers.put(this.strArray[0], new CC_PunktFencedAnyFencedSum());
        this.changers.put(this.strArray[1], new CC_PunktFencedSumFencedAny());
        this.changers.put(this.strArray[2], new CC_PunktFencedSumFencedSum());
    }

    @Override
    public C_Changer getChanger(final C_Event e) {
        if (e != null && e.getFirst() != null && e.getFirst().hasNextC()) {
            this.setEvent(e);
            if (this.getSec().hasExtDiv()) {
                return new C_No(e);
            }
            final CElement cE1 = this.getFirst();
            final CElement cE2 = this.getSec();
            System.out.println("Try to combine Fences Fences");
            if (cE1.getFirstChild() instanceof CMinTerm
                    && cE2.getFirstChild() instanceof CMinTerm) {
                System.out.println("Found MinTerms");
                return this.getCmm().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CPlusTerm
                    && cE2.getFirstChild() instanceof CMinTerm) {
                System.out.println("Found PM");
                return this.getCpm().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CPlusTerm
                    && cE2.getFirstChild() instanceof CPlusTerm) {
                System.out.println("Found PP");
                return this.getCpp().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CMinTerm
                    && cE2.getFirstChild() instanceof CPlusTerm) {
                System.out.println("Found MP");
                return this.getCmp().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CPlusRow
                    && cE2.getFirstChild() instanceof CPlusRow) {
                System.out.println("Found SS");
                final TransferObject to = new TransferObject(this.strArray);
                ViewerFactory.getInst().getComboDialog(to);
                final C_Changer c = this.changers.get(to.getResult());
                return c.getChanger(e);
            } else if (cE1.getFirstChild() instanceof CPlusRow) {
                System.out.println("Found SA");
                return this.getCsa().getChanger(e);
            } else if (cE2.getFirstChild() instanceof CPlusRow) {
                System.out.println("Found AS");
                return this.getCas().getChanger(e);
            }

        }
        return new C_No(e);
    }

    protected CC_PunktFencedMinFencedMin getCmm() {
        if (this.cmm == null) {
            this.cmm = new CC_PunktFencedMinFencedMin();
        }
        return this.cmm;
    }

    protected CC_PunktFencedMinFencedPlus getCmp() {
        if (this.cmp == null) {
            this.cmp = new CC_PunktFencedMinFencedPlus();
        }
        return this.cmp;
    }

    protected CC_PunktFencedPlusFencedMin getCpm() {
        if (this.cpm == null) {
            this.cpm = new CC_PunktFencedPlusFencedMin();
        }
        return this.cpm;
    }

    protected CC_PunktFencedPlusFencedPlus getCpp() {
        if (this.cpp == null) {
            this.cpp = new CC_PunktFencedPlusFencedPlus();
        }
        return this.cpp;
    }

    protected CC_PunktFencedAnyFencedSum getCas() {
        if (this.cas == null) {
            this.cas = new CC_PunktFencedAnyFencedSum();
        }
        return this.cas;
    }

    protected CC_PunktFencedSumFencedAny getCsa() {
        if (this.csa == null) {
            this.csa = new CC_PunktFencedSumFencedAny();
        }
        return this.csa;
    }
}
