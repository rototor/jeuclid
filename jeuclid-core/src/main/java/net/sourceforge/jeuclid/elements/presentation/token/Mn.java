/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
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

/* $Id$ */

package net.sourceforge.jeuclid.elements.presentation.token;

/**
 * This class presents numbers in a equation.
 * 
 * @version $Revision$
 */
public class Mn extends AbstractTokenWithStandardLayout {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mn";

    // private static final String DOT = ".";

    /**
     * Default constructor.
     */
    public Mn() {
        super();
    }

    // /**
    // * @return Width of till point
    // * @param g
    // * Graphics2D context to use.
    // */
    // public int getWidthTillPoint(final Graphics2D g) {
    // int result = 0;
    //
    // if (this.getText() == null && this.getText().length() == 0) {
    // return result;
    // }
    // final FontMetrics metrics = this.getFontMetrics(g);
    // String integer = this.getText();
    // final int dotIndex = integer.indexOf(Mn.DOT);
    // if (dotIndex >= 0) {
    // integer = integer.substring(0, dotIndex);
    // }
    // result = metrics.stringWidth(integer);
    // return result;
    // }

    // /**
    // * @return width of point
    // * @param g
    // * Graphics2D context to use.
    // */
    // public float getPointWidth(final Graphics2D g) {
    // float result = 0;
    //
    // final FontMetrics metrics = this.getFontMetrics(g);
    // result = metrics.stringWidth(Mn.DOT);
    //
    // return result;
    // }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mn.ELEMENT;
    }

}
