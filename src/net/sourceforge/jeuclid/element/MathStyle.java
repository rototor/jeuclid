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

/* $Id: MathStyle.java,v 1.8.2.4 2007/01/31 22:50:26 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElementWithChildren;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class arrange an element lower to an other element.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathStyle extends AbstractMathElementWithChildren {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MathStyle.class);

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mstyle";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathStyle(MathBase base) {
        super(base);
    }

    /**
     * @return Script level
     */
    public String getScriptlevel() {
        return this.getMathAttribute("scriptlevel");
    }

    /**
     * @param scriptlevel
     *            script level
     */
    public void setScriptlevel(String scriptlevel) {
        this.setAttribute("scriptlevel", scriptlevel);
    }

    // TODO: Add these.
    // public String getDisplaystyle();
    // public void setDisplaystyle(String displaystyle);
    // public String getScriptsizemultiplier();
    // public void setScriptsizemultiplier(String scriptsizemultiplier);

    /**
     * @return Minimum of script size
     */
    public String getScriptminsize() {
        return this.getMathAttribute("scriptminsize");
    }

    /**
     * @param scriptminsize
     *            Minimum of script size
     */
    public void setScriptminsize(String scriptminsize) {
        this.setAttribute("scriptminsize", scriptminsize);
    }

    /** {@inheritDoc} */
    protected int getAbsoluteScriptLevel() {
        int theLevel;
        try {
            String attr = this.getScriptlevel();
            if (attr == null) {
                theLevel = this.getInheritedScriptlevel();
            } else {
                attr = attr.trim();
                if (attr.length() == 0) {
                    theLevel = this.getInheritedScriptlevel();
                } else {
                    char firstchar = attr.charAt(0);
                    boolean relative = false;
                    if (firstchar == '+') {
                        relative = true;
                        attr = attr.substring(1);
                    } else if (firstchar == '-') {
                        relative = true;
                    }
                    int iValue = new Integer(attr).intValue();
                    if (relative) {
                        theLevel = this.getInheritedScriptlevel() + iValue;
                    } else {
                        theLevel = iValue;
                    }
                }
            }
        } catch (NumberFormatException e) {
            logger.warn("Error in scriptlevel attribute for mstyle: "
                    + this.getScriptlevel());
            theLevel = this.getInheritedScriptlevel();
        }
        return theLevel;
    }
}
