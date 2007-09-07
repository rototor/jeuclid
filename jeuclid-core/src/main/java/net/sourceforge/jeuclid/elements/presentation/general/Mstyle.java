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

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Display;

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
     */
    public Mstyle() {
        super();
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

    // String attr = this.getScriptlevel();
    // if (attr == null) {
    // attr = "";
    // }
    // attr = attr.trim();
    // if (attr.length() == 0) {
    // theLevel = this.getInheritedScriptlevel();
    // } else {
    // final char firstchar = attr.charAt(0);
    // boolean relative = false;
    // if (firstchar == '+') {
    // relative = true;
    // attr = attr.substring(1);
    // } else if (firstchar == '-') {
    // relative = true;
    // }
    // final int iValue = Integer.parseInt(attr);
    // if (relative) {
    // theLevel = this.getInheritedScriptlevel() + iValue;
    // } else {
    // theLevel = iValue;
    //
    // }
    // }
    // } catch (final NumberFormatException e) {
    // Mstyle.LOGGER.warn("Error in scriptlevel attribute for mstyle: "
    // + this.getScriptlevel());
    // theLevel = this.getInheritedScriptlevel();
    // }
    // return theLevel;
    // }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        return new LayoutContext() {

            public Object getParameter(final Parameter which) {
                Object retVal = Mstyle.this.applyLocalAttributesToContext(
                        context).getParameter(which);
                if (Parameter.DISPLAY.equals(which)) {
                    final String displayStyle = Mstyle.this.getDisplaystyle();
                    if ("true".equalsIgnoreCase(displayStyle)) {
                        retVal = Display.BLOCK;
                    }
                    if ("false".equalsIgnoreCase(displayStyle)) {
                        retVal = Display.INLINE;
                    }
                }
                if (Parameter.SCRIPTSIZE.equals(which)) {
                    String attr = Mstyle.this.getScriptlevel();
                    if (attr == null) {
                        attr = "";
                    }
                    attr = attr.trim();
                    if (attr.length() > 0) {
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
                            retVal = (Integer) retVal + iValue;
                        } else {
                            retVal = iValue;
                        }
                    }
                }
                return retVal;
            }
        };
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

    /** {@inheritDoc} */
    public String getMediummathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getNegativemediummathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getNegativethickmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getNegativethinmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getNegativeverythickmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getNegativeverythinmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getNegativeveryverythickmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getNegativeveryverythinmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getThickmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getThinmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getVerythickmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getVerythinmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getVeryverythickmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public String getVeryverythinmathspace() {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setMediummathspace(final String mediummathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setNegativemediummathspace(
            final String negativemediummathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setNegativethickmathspace(final String negativethickmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setNegativethinmathspace(final String negativethinmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setNegativeverythickmathspace(
            final String negativeverythickmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setNegativeverythinmathspace(
            final String negativeverythinmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setNegativeveryverythickmathspace(
            final String negativeveryverythickmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setNegativeveryverythinmathspace(
            final String negativeveryverythinmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setThickmathspace(final String thickmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setThinmathspace(final String thinmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setVerythickmathspace(final String verythickmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setVerythinmathspace(final String verythinmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setVeryverythickmathspace(final String veryverythickmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

    /** {@inheritDoc} */
    public void setVeryverythinmathspace(final String veryverythinmathspace) {
        throw new UnsupportedOperationException();
        // TODO Auto-generated method stub
    }

}
