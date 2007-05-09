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

package net.sourceforge.jeuclid.elements.presentation.script;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.JEuclidElement;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMultiScriptsElement;
import org.w3c.dom.mathml.MathMLNodeList;

/**
 * Prescripts and Tensor Indices.
 * 
 * @todo This class has to be rewritten to use getSubMiddleShift and
 *       getSupMiddleShift, and cleaned up.
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */

public class Mmultiscripts extends AbstractScriptElement implements
        MathMLMultiScriptsElement {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mmultiscripts";

    /**
     * Logger for this class
     */
    // currently unused
    // private static final Log LOGGER = LogFactory
    // .getLog(MathMultiScripts.class);
    /**
     * Default constructor.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Mmultiscripts(final MathBase base) {
        super(base);
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting
     * @param posX
     *            The first left position for painting
     * @param posY
     *            The position of the baseline
     */
    @Override
    public final void paint(final Graphics2D g, float posX, final float posY) {
        super.paint(g, posX, posY);
        int prPos = -1;
        for (int i = 1; i < this.getMathElementCount(); i++) {
            if (this.getMathElement(i) instanceof Mprescripts) {
                prPos = i;
                break;
            }
        }
        JEuclidElement baseElement = null;
        baseElement = this.getMathElement(0);
        JEuclidElement childElement = null;
        if (this.getMathElementCount() > 2) {
            for (int i = 1; i < this.getMathElementCount(); i++) {
                if (!(this.getMathElement(i) instanceof Mprescripts)) {
                    childElement = this.getMathElement(i);
                    break;
                }
            }
        }
        float middleshift = 0;
        if (childElement != null) {
            middleshift = baseElement.getHeight(g)
                    * AbstractSubSuper.DEFAULT_SCRIPTSHIFT;
        }
        float e1DescentHeight = 0;
        if (baseElement != null) {
            e1DescentHeight = baseElement.getDescentHeight(g);
        }
        if (e1DescentHeight == 0) {
            e1DescentHeight = this.getFontMetrics(g).getDescent();
        }
        float e1AscentHeight = 0;
        if (baseElement != null) {
            e1AscentHeight = baseElement.getAscentHeight(g);
        }
        if (e1AscentHeight == 0) {
            e1AscentHeight = this.getFontMetrics(g).getAscent();
        }

        final float posY1 = posY + e1DescentHeight
                + this.calculateMaxElementAscentHeight(g) - middleshift;
        final float posY2 = posY - e1AscentHeight + middleshift
                - this.calculateMaxElementDescentHeight(g);

        int width = 0;
        if (prPos != -1) {
            int p = 0;
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 1; i < this.getMathElementCount() - p; i++) {
                if ((i - (prPos + 1)) > 1) {
                    if ((i - (prPos + 1)) % 2 == 0) {
                        width += Math.max(this.getMathElement(i - 2)
                                .getWidth(g), this.getMathElement(i - 1)
                                .getWidth(g));
                    }
                }
                if ((i - (prPos + 1)) % 2 == 0) {
                    this.getMathElement(i).paint(g, posX + width, posY1);
                } else {
                    this.getMathElement(i).paint(g, posX + width, posY2);
                }
            }
            width += Math.max(this.getMathElement(
                    this.getMathElementCount() - 2 - p).getWidth(g), this
                    .getMathElement(this.getMathElementCount() - 1 - p)
                    .getWidth(g));

        }
        if (baseElement != null) {
            baseElement.paint(g, posX + width, posY);
            posX += baseElement.getWidth(g);
        }
        if (prPos == -1) {
            prPos = this.getMathElementCount();
        }
        int p = 0;
        if (prPos % 2 == 0) {
            p = 1;
        }

        for (int i = 1; i < prPos - p; i++) {
            if ((i - 1) > 1) {
                if ((i - 1) % 2 == 0) {
                    width += Math.max(this.getMathElement(i - 2).getWidth(g),
                            this.getMathElement(i - 1).getWidth(g));
                }
            }
            if ((i - 1) % 2 == 0) {
                this.getMathElement(i).paint(g, posX + width, posY1);
            } else {
                this.getMathElement(i).paint(g, posX + width, posY2);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public final float calculateWidth(final Graphics2D g) { // done
        float width = 0;
        if (this.getMathElementCount() > 0) {
            width += this.getMathElement(0).getWidth(g);
            int prPos = -1;
            for (int i = 0; i < this.getMathElementCount(); i++) {
                if (this.getMathElement(i) instanceof Mprescripts) {
                    prPos = i;
                    break;
                }
            }
            if (prPos == -1) {
                prPos = this.getMathElementCount();
            }
            int p = 0;
            if (prPos % 2 == 0) {
                p = 1;
            }
            for (int i = 1; i < prPos - p; i = i + 2) {
                width += Math.max(this.getMathElement(i).getWidth(g), this
                        .getMathElement(i + 1).getWidth(g));
            }
            if (prPos != this.getMathElementCount()) {
                if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                    p = 1;
                } else {
                    p = 0;
                }
                for (int i = prPos + 1; i < this.getMathElementCount() - p; i = i + 2) {
                    width += Math.max(this.getMathElement(i).getWidth(g),
                            this.getMathElement(i + 1).getWidth(g));
                }
            }
        }
        return width + 1;
    }

    /**
     * Return the current height of the upper part of child component from the
     * baseline.
     * 
     * @return Height of the upper part
     * @param g
     *            Graphics2D context to use.
     */
    public final float calculateMaxElementAscentHeight(final Graphics2D g) {

        int prPos = -1;
        float descenHeight = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (this.getMathElement(i) instanceof Mprescripts) {
                prPos = i;
                break;
            }
        }
        if (prPos != -1) {
            int p = 0;
            if (prPos % 2 == 0) {
                p = 1;
            }
            for (int i = 1; i < prPos - p; i = i + 2) {
                descenHeight = Math.max(descenHeight, this.getMathElement(i)
                        .getAscentHeight(g));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 1; i < this.getMathElementCount() - p; i = i + 2) {
                descenHeight = Math.max(descenHeight, this.getMathElement(i)
                        .getAscentHeight(g));
            }
        } else {
            for (int i = 1; i < this.getMathElementCount(); i = i + 2) {
                descenHeight = Math.max(descenHeight, this.getMathElement(i)
                        .getAscentHeight(g));
            }
        }
        return descenHeight;
    }

    /** {@inheritDoc} */
    @Override
    public final float calculateAscentHeight(final Graphics2D g) {
        int prPos = -1;
        float e2h = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (this.getMathElement(i) instanceof Mprescripts) {
                prPos = i;
                break;
            }
        }
        if (prPos != -1) {
            int p = 0;
            if (prPos % 2 == 0) {
                p = 1;
            }
            for (int i = 2; i < prPos - p; i = i + 2) {
                e2h = Math
                        .max(
                                e2h,
                                Math
                                        .max(
                                                this.getMathElement(i)
                                                        .getHeight(g)
                                                        - (this
                                                                .getMathElement(
                                                                        0)
                                                                .getHeight(g) * AbstractSubSuper.DEFAULT_SCRIPTSHIFT),
                                                0));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 2; i < this.getMathElementCount() - p; i = i + 2) {
                e2h = Math
                        .max(
                                e2h,
                                Math
                                        .max(
                                                this.getMathElement(i)
                                                        .getHeight(g)
                                                        - (this
                                                                .getMathElement(
                                                                        0)
                                                                .getHeight(g) * AbstractSubSuper.DEFAULT_SCRIPTSHIFT),
                                                0));
            }
        } else {
            for (int i = 2; i < this.getMathElementCount(); i = i + 2) {
                e2h = Math
                        .max(
                                e2h,
                                Math
                                        .max(
                                                this.getMathElement(i)
                                                        .getHeight(g)
                                                        - (this
                                                                .getMathElement(
                                                                        0)
                                                                .getHeight(g) * AbstractSubSuper.DEFAULT_SCRIPTSHIFT),
                                                0));
            }
        }
        return this.getMathElement(0).getAscentHeight(g) + e2h;
    }

    /**
     * Return the max height of the sub element.
     * 
     * @return max height of the sub element
     * @param g
     *            Graphics2D context to use.
     */
    public final float getMaxElementHeight(final Graphics2D g) {
        float childHeight = 0;
        int prPos = -1;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (this.getMathElement(i) instanceof Mprescripts) {
                prPos = i;
                break;
            }
        }
        if (prPos != -1) {
            int p = 0;
            if (prPos % 2 == 0) {
                p = 1;
            }
            for (int i = 2; i < prPos - p; i = i + 2) {
                childHeight = Math.max(childHeight, this.getMathElement(i)
                        .getHeight(g));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 2; i < this.getMathElementCount() - p; i = i + 2) {
                childHeight = Math.max(childHeight, this.getMathElement(i)
                        .getHeight(g));
            }
        } else {
            for (int i = 2; i < this.getMathElementCount(); i = i + 2) {
                childHeight = Math.max(childHeight, this.getMathElement(i)
                        .getHeight(g));
            }
        }
        return childHeight;
    }

    /**
     * Return the current height of the lower part of child component from the
     * baseline.
     * 
     * @return Height of the lower part
     * @param g
     *            Graphics2D context to use.
     */

    public final float calculateMaxElementDescentHeight(final Graphics2D g) {
        int prPos = -1;
        float ascentHeight = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (this.getMathElement(i) instanceof Mprescripts) {
                prPos = i;
                break;
            }
        }
        if (prPos != -1) {
            int p = 0;
            if (prPos % 2 == 0) {
                p = 1;
            }
            for (int i = 2; i < prPos - p; i = i + 2) {
                ascentHeight = Math.max(ascentHeight, this.getMathElement(i)
                        .getDescentHeight(g));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 2; i < this.getMathElementCount() - p; i = i + 2) {
                ascentHeight = Math.max(ascentHeight, this.getMathElement(i)
                        .getDescentHeight(g));
            }
        } else {
            for (int i = 2; i < this.getMathElementCount(); i = i + 2) {
                ascentHeight = Math.max(ascentHeight, this.getMathElement(i)
                        .getDescentHeight(g));
            }
        }
        return ascentHeight;
    }

    /** {@inheritDoc} */
    @Override
    public final float calculateDescentHeight(final Graphics2D g) {

        int prPos = -1;
        float e2h = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (this.getMathElement(i) instanceof Mprescripts) {
                prPos = i;
                break;
            }
        }
        if (prPos != -1) {
            int p = 0;
            if (prPos % 2 == 0) {
                p = 1;
            }
            for (int i = 1; i < prPos - p; i = i + 2) {
                e2h = Math
                        .max(
                                e2h,
                                Math
                                        .max(
                                                this.getMathElement(i)
                                                        .getHeight(g)
                                                        - (this
                                                                .getMathElement(
                                                                        0)
                                                                .getHeight(g) * AbstractSubSuper.DEFAULT_SCRIPTSHIFT),
                                                0));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 1; i < this.getMathElementCount() - p; i = i + 2) {
                e2h = Math
                        .max(
                                e2h,
                                Math
                                        .max(
                                                this.getMathElement(i)
                                                        .getHeight(g)
                                                        - (this
                                                                .getMathElement(
                                                                        0)
                                                                .getHeight(g) * AbstractSubSuper.DEFAULT_SCRIPTSHIFT),
                                                0));
            }
        } else {
            for (int i = 1; i < this.getMathElementCount(); i = i + 2) {
                e2h = Math
                        .max(
                                e2h,
                                Math
                                        .max(
                                                this.getMathElement(i)
                                                        .getHeight(g)
                                                        - (this
                                                                .getMathElement(
                                                                        0)
                                                                .getHeight(g) * AbstractSubSuper.DEFAULT_SCRIPTSHIFT),
                                                0));
            }
        }
        return this.getMathElement(0).getDescentHeight(g) + e2h;
    }

    // All this function does is error messaging.
    // TODO: Look for a good place to do this.
    // /**
    // * Write errors in conditions.
    // */
    // @Override
    // public final void eventAllElementsComplete() {
    // super.eventAllElementsComplete();
    // if (this.getMathElementCount() == 0) {
    // MathMultiScripts.LOGGER
    // .error("Wrong number of parametrs, must be 1 or more");
    // } else if (this.getMathElement(0) instanceof MathPreScripts) {
    // MathMultiScripts.LOGGER.error("The first argument must be base.");
    // }
    // boolean isMultMPrescripts = false;
    // if (this.getMathElementCount() > 0) {
    // int prPos = -1;
    // for (int i = 0; i < this.getMathElementCount(); i++) {
    // if (this.getMathElement(i) instanceof MathPreScripts) {
    // if (prPos != -1) {
    // MathMultiScripts.LOGGER
    // .error("The empty element mprescripts must be declared once.");
    // isMultMPrescripts = true;
    // break;
    // }
    // prPos = i;
    //
    // }
    // }
    // if (!isMultMPrescripts) {
    // if (prPos == -1 && this.getMathElementCount() % 2 == 0) {
    // MathMultiScripts.LOGGER
    // .error("The total number of the arguments must be odd.\n"
    // + "Some elements may not be drown. ");
    // } else if (prPos != -1) {
    // if (prPos % 2 == 0) {
    // MathMultiScripts.LOGGER
    // .error("The total number of the postcripts elements must be even.\n"
    // + "Some elements may not be drown.");
    // }
    // if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
    // MathMultiScripts.LOGGER
    // .error("The total number of the prestcripts elements must be even.\n"
    // + "Some elements may not be drown.");
    // }
    //
    // }
    // }
    // }
    // };

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final JEuclidElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return this.getAbsoluteScriptLevel();
        } else {
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasChildPrescripts(final JEuclidElement child) {
        return child.isSameNode(this.getBase())
                && (this.getNumprescriptcolumns() > 0);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasChildPostscripts(final JEuclidElement child) {
        return child.isSameNode(this.getBase())
                && (this.getNumscriptcolumns() > 0);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mmultiscripts.ELEMENT;
    }

    /** {@inheritDoc} */
    public JEuclidElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

    private int getPrescriptsIndex() {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        final int len = childList.getLength();
        for (int i = 0; i < len; i++) {
            final Node child = childList.item(i);
            if (child instanceof Mprescripts) {
                return i;
            }
        }
        return len;
    }

    /** {@inheritDoc} */
    public int getNumprescriptcolumns() {
        final int fulllength = this.getChildNodes().getLength();
        final int prescriptstart = this.getPrescriptsIndex() + 1;
        return (fulllength - prescriptstart) / 2;
    }

    /** {@inheritDoc} */
    public int getNumscriptcolumns() {
        return this.getPrescriptsIndex() / 2;
    }

    /** {@inheritDoc} */
    public MathMLElement getPreSubScript(final int colIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement getPreSuperScript(final int colIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLNodeList getPrescripts() {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLNodeList getScripts() {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement getSubScript(final int colIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement getSuperScript(final int colIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement insertPreSubScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement insertPreSuperScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement insertSubScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement insertSuperScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement setPreSubScriptAt(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement setPreSuperScriptAt(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement setSubScriptAt(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement setSuperScriptAt(final int colIndex,
            final MathMLElement newScript) {
        // TODO Auto-generated method stub
        return null;
    }

}
