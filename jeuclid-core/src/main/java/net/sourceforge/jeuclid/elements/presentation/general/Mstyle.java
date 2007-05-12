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

package net.sourceforge.jeuclid.elements.presentation.general;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.JEuclidElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.mathml.MathMLStyleElement;

/**
 * This class arrange an element lower to an other element.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mstyle extends AbstractRowLike implements MathMLStyleElement {

    /** Attribute for scriptminsize. */
    public static final String ATTR_SCRIPTMINSIZE = "scriptminsize";

    /** Attribute for scriptlevel. */
    public static final String ATTR_SCRIPTLEVEL = "scriptlevel";

    /** Attribute for scriptsizemultiplier. */
    public static final String ATTR_SCRIPTSIZEMULTIPLIER = "scriptsizemultiplier";

    /** Attribute for displaystyle. */
    public static final String ATTR_DISPLAYSTYLE = "displaystyle";

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mstyle";

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Mstyle.class);

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Mstyle(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(Mstyle.ATTR_DISPLAYSTYLE, "");
    }

    /**
     * @return Script level
     */
    public String getScriptlevel() {
        return this.getMathAttribute(Mstyle.ATTR_SCRIPTLEVEL);
    }

    /**
     * @param scriptlevel
     *            script level
     */
    public void setScriptlevel(final String scriptlevel) {
        this.setAttribute(Mstyle.ATTR_SCRIPTLEVEL, scriptlevel);
    }

    /**
     * @return Minimum of script size
     */
    public String getScriptminsize() {
        return this.getMathAttribute(Mstyle.ATTR_SCRIPTMINSIZE);
    }

    /**
     * @param scriptminsize
     *            Minimum of script size
     */
    public void setScriptminsize(final String scriptminsize) {
        this.setAttribute(Mstyle.ATTR_SCRIPTMINSIZE, scriptminsize);
    }

    /** {@inheritDoc} */
    @Override
    protected int getAbsoluteScriptLevel() {
        int theLevel;
        try {
            String attr = this.getScriptlevel();
            if (attr == null) {
                attr = "";
            }
            attr = attr.trim();
            if (attr.length() == 0) {
                theLevel = this.getInheritedScriptlevel();
            } else {
                final char firstchar = attr.charAt(0);
                boolean relative = false;
                if (firstchar == '+') {
                    relative = true;
                    attr = attr.substring(1);
                } else if (firstchar == '-') {
                    relative = true;
                }
                final int iValue = Integer.parseInt(attr);
                if (relative) {
                    theLevel = this.getInheritedScriptlevel() + iValue;
                } else {
                    theLevel = iValue;

                }
            }
        } catch (final NumberFormatException e) {
            Mstyle.LOGGER.warn("Error in scriptlevel attribute for mstyle: "
                    + this.getScriptlevel());
            theLevel = this.getInheritedScriptlevel();
        }
        return theLevel;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final JEuclidElement child) {
        final String displayStyle = this.getDisplaystyle();
        if (displayStyle.length() > 0) {
            final boolean dValue = Boolean.parseBoolean(displayStyle);
            return dValue;
        } else {
            return super.isChildBlock(child);
        }

    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mstyle.ELEMENT;
    }

    /** {@inheritDoc} */
    public String getBackground() {
        return this.getMathbackground();
    }

    /** {@inheritDoc} */
    public String getColor() {
        return this.getMathcolor();
    }

    /** {@inheritDoc} */
    public String getDisplaystyle() {
        return this.getMathAttribute(Mstyle.ATTR_DISPLAYSTYLE);
    }

    /** {@inheritDoc} */
    public String getScriptsizemultiplier() {
        return this.getMathAttribute(Mstyle.ATTR_SCRIPTSIZEMULTIPLIER);
    }

    /** {@inheritDoc} */
    public void setBackground(final String background) {
        this.setMathbackground(background);
    }

    /** {@inheritDoc} */
    public void setColor(final String color) {
        this.setMathcolor(color);
    }

    /** {@inheritDoc} */
    public void setDisplaystyle(final String displaystyle) {
        this.setAttribute(Mstyle.ATTR_DISPLAYSTYLE, displaystyle);
    }

    /** {@inheritDoc} */
    public void setScriptsizemultiplier(final String scriptsizemultiplier) {
        this.setAttribute(Mstyle.ATTR_SCRIPTSIZEMULTIPLIER,
                scriptsizemultiplier);
    }

}
