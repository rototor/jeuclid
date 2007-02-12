/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

/* $Id: MathLabeledTableRow.java,v 1.3 2006/08/07 18:27:47 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import net.sourceforge.jeuclid.MathBase;

/**
 * This class represents the maligngroup tag.
 * @author PG
 * @since Jan 19, 2005
 */
public class MathLabeledTableRow extends MathTableRow {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mlabeledtr";

    // TODO: now object is empty.

    public boolean labelIgnored;

    /**
     * Creates a math element.
     *
     * @param base The base for the math element tree.
     */
    public MathLabeledTableRow(MathBase base) {
        super(base);
    }
}
