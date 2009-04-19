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

public abstract class ActivityAdapter extends PraefixAdapter {

    public void setCActiveProperty() {
        if (this.hasParent()) {
            ((Element) this.getElement().getParentNode()).setAttribute(
                    "mathbackground", "#B0C4DE");
        }
        this.getElement().setAttribute("mathcolor", "#FF0000");
    }

    public void removeCActiveProperty() {
        if (this.hasParent()) {
            ((Element) this.getElement().getParentNode())
                    .removeAttribute("mathbackground");
        }
        this.getElement().removeAttribute("mathcolor");
    }

    public void setCLastProperty() {
        this.getElement().setAttribute("mathcolor", "#007777");
    }

    public void removeCLastProperty() {
        this.getElement().removeAttribute("mathcolor");
    }

    public boolean isActiveC() {
        return "#FF0000".equals(this.getElement().getAttribute("mathcolor"));
    }

    public boolean isLastC() {
        return "#007777".equals(this.getElement().getAttribute("mathcolor"));
    }

}
