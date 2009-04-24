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

public class CEqu extends CElement {

    public CEqu(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.EQU;
    }

    public CElement getLinkeSeite() {
        return this.getFirstChild();
    }

    public CElement getRechteSeite() {
        return this.getFirstChild().getNextSibling();
    }

    @Override
    public void normalize() {
    };

    @Override
    public int internalCompare(final CElement o) {
        final CEqu eq2 = (CEqu) o;
        if (this.getLinkeSeite().compareTo(eq2.getLinkeSeite()) != 0) {
            return this.getLinkeSeite().compareTo(eq2.getLinkeSeite());
        } else {
            return this.getRechteSeite().compareTo(eq2.getRechteSeite());
        }
    }
}
