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

/* $Id: MathMultiScripts.java,v 1.6.2.6 2007/01/31 22:50:26 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Graphics;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class arange a element lower, and a other elements upper to an element.
 * 
 */

public class MathMultiScripts extends AbstractMathElement {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MathMultiScripts.class);

    private static final double DY = 0.43 / 2;

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mmultiscripts";

    private int msubscriptshift = 0;

    private int msuperscriptshift = 0;

    /**
     * Default constructor.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathMultiScripts(MathBase base) {
        super(base);
    }

    /**
     * Sets subscriptshift property value.
     * 
     * @param subscriptshift
     *            Value of subscriptshift.
     */
    public final void setSubScriptShift(int subscriptshift) {
        this.msubscriptshift = subscriptshift;
    }

    /**
     * Gets value of subscriptshift.
     * 
     * @return Value of subscriptshift property.
     */
    public final int getSubScriptShift() {
        return msubscriptshift;
    }

    /**
     * Sets superscriptshift property value.
     * 
     * @param superscriptshift
     *            Value of superscriptshift.
     */
    public final void setSuperScriptShift(int superscriptshift) {
        this.msuperscriptshift = superscriptshift;
    }

    /**
     * Gets value of superscriptshift.
     * 
     * @return Value of superscriptshift property.
     */
    public final int getSuperScriptShift() {
        return msuperscriptshift;
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
    public final void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);
        int prPos = -1;
        for (int i = 1; i < this.getMathElementCount(); i++) {
            if (getMathElement(i) instanceof MathPreScripts) {
                prPos = i;
                break;
            }
        }
        AbstractMathElement baseElement = null;
        baseElement = getMathElement(0);
        AbstractMathElement childElement = null;
        if (getMathElementCount() > 2) {
            for (int i = 1; i < getMathElementCount(); i++) {
                if (!(getMathElement(i) instanceof MathPreScripts)) {
                    childElement = getMathElement(i);
                    break;
                }
            }
        }
        int middleshift = 0;
        if (childElement != null) {
            middleshift = (int) (baseElement.getHeight(g) * DY);
        }
        int e1DescentHeight = 0;
        if (baseElement != null) {
            e1DescentHeight = baseElement.getDescentHeight(g);
        }
        if (e1DescentHeight == 0) {
            e1DescentHeight = getFontMetrics(g).getDescent();
        }
        int e1AscentHeight = 0;
        if (baseElement != null) {
            e1AscentHeight = baseElement.getAscentHeight(g);
        }
        if (e1AscentHeight == 0) {
            e1AscentHeight = getFontMetrics(g).getAscent();
        }

        int posY1 = posY + e1DescentHeight + calculateMaxElementAscentHeight(g)
                - middleshift;
        int posY2 = posY - e1AscentHeight + middleshift
                - calculateMaxElementDescentHeight(g);

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
    public final int calculateWidth(Graphics g) { // done
        int width = 0;
        if (this.getMathElementCount() > 0) {
            width += this.getMathElement(0).getWidth(g);
            int prPos = -1;
            for (int i = 0; i < this.getMathElementCount(); i++) {
                if (getMathElement(i) instanceof MathPreScripts) {
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
                    width += Math.max(this.getMathElement(i).getWidth(g), this
                            .getMathElement(i + 1).getWidth(g));
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
     *            Graphics context to use.
     */
    public final int calculateMaxElementAscentHeight(Graphics g) {

        int prPos = -1;
        int descenHeight = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (getMathElement(i) instanceof MathPreScripts) {
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
                descenHeight = Math.max(descenHeight, getMathElement(i)
                        .getAscentHeight(g));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 1; i < this.getMathElementCount() - p; i = i + 2) {
                descenHeight = Math.max(descenHeight, getMathElement(i)
                        .getAscentHeight(g));
            }
        } else {
            for (int i = 1; i < this.getMathElementCount(); i = i + 2) {
                descenHeight = Math.max(descenHeight, getMathElement(i)
                        .getAscentHeight(g));
            }
        }
        return descenHeight;
    }

    /** {@inheritDoc} */
    public final int calculateAscentHeight(Graphics g) {
        int prPos = -1;
        int e2h = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (getMathElement(i) instanceof MathPreScripts) {
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
                e2h = Math.max(e2h, Math.max(getMathElement(i).getHeight(g)
                        - (int) (getMathElement(0).getHeight(g) * DY), 0));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 2; i < this.getMathElementCount() - p; i = i + 2) {
                e2h = Math.max(e2h, Math.max(getMathElement(i).getHeight(g)
                        - (int) (getMathElement(0).getHeight(g) * DY), 0));
            }
        } else {
            for (int i = 2; i < this.getMathElementCount(); i = i + 2) {
                e2h = Math.max(e2h, Math.max(getMathElement(i).getHeight(g)
                        - (int) (getMathElement(0).getHeight(g) * DY), 0));
            }
        }
        return getMathElement(0).getAscentHeight(g) + e2h;
    }

    /**
     * Return the max height of the sub element.
     * 
     * @return max height of the sub element
     * @param g
     *            Graphics context to use.
     */
    public final int getMaxElementHeight(Graphics g) {
        int childHeight = 0;
        int prPos = -1;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (getMathElement(i) instanceof MathPreScripts) {
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
                childHeight = Math.max(childHeight, getMathElement(i)
                        .getHeight(g));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 2; i < this.getMathElementCount() - p; i = i + 2) {
                childHeight = Math.max(childHeight, getMathElement(i)
                        .getHeight(g));
            }
        } else {
            for (int i = 2; i < this.getMathElementCount(); i = i + 2) {
                childHeight = Math.max(childHeight, getMathElement(i)
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
     *            Graphics context to use.
     */

    public final int calculateMaxElementDescentHeight(Graphics g) {
        int prPos = -1;
        int ascentHeight = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (getMathElement(i) instanceof MathPreScripts) {
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
                ascentHeight = Math.max(ascentHeight, getMathElement(i)
                        .getDescentHeight(g));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 2; i < this.getMathElementCount() - p; i = i + 2) {
                ascentHeight = Math.max(ascentHeight, getMathElement(i)
                        .getDescentHeight(g));
            }
        } else {
            for (int i = 2; i < this.getMathElementCount(); i = i + 2) {
                ascentHeight = Math.max(ascentHeight, getMathElement(i)
                        .getDescentHeight(g));
            }
        }
        return ascentHeight;
    }

    /** {@inheritDoc} */
    public final int calculateDescentHeight(Graphics g) {

        int prPos = -1;
        int e2h = 0;
        for (int i = 0; i < this.getMathElementCount(); i++) {
            if (getMathElement(i) instanceof MathPreScripts) {
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
                e2h = Math.max(e2h, Math.max(getMathElement(i).getHeight(g)
                        - (int) (getMathElement(0).getHeight(g) * DY), 0));
            }
            if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                p = 1;
            }
            for (int i = prPos + 1; i < this.getMathElementCount() - p; i = i + 2) {
                e2h = Math.max(e2h, Math.max(getMathElement(i).getHeight(g)
                        - (int) (getMathElement(0).getHeight(g) * DY), 0));
            }
        } else {
            for (int i = 1; i < this.getMathElementCount(); i = i + 2) {
                e2h = Math.max(e2h, Math.max(getMathElement(i).getHeight(g)
                        - (int) (getMathElement(0).getHeight(g) * DY), 0));
            }
        }
        return getMathElement(0).getDescentHeight(g) + e2h;
    }

    /**
     * Write errors in conditions.
     */
    public final void eventAllElementsComplete() {
        super.eventAllElementsComplete();
        if (this.getMathElementCount() == 0) {
            logger.error("Wrong number of parametrs, must be 1 or more");
        } else if (this.getMathElement(0) instanceof MathPreScripts) {
            logger.error("The first argument must be base.");
        }
        boolean isMultMPrescripts = false;
        if (this.getMathElementCount() > 0) {
            int prPos = -1;
            for (int i = 0; i < this.getMathElementCount(); i++) {
                if (getMathElement(i) instanceof MathPreScripts) {
                    if (prPos != -1) {
                        logger
                                .error("The empty element mprescripts must be declared once.");
                        isMultMPrescripts = true;
                        break;
                    }
                    prPos = i;

                }
            }
            if (!isMultMPrescripts) {
                if (prPos == -1 && this.getMathElementCount() % 2 == 0) {
                    logger
                            .error("The total number of the arguments must be odd.\n"
                                    + "Some elements may not be drown. ");
                } else if (prPos != -1) {
                    if (prPos % 2 == 0) {
                        logger
                                .error("The total number of the postcripts elements must be even.\n"
                                        + "Some elements may not be drown.");
                    }
                    if ((this.getMathElementCount() - prPos - 1) % 2 != 0) {
                        logger
                                .error("The total number of the prestcripts elements must be even.\n"
                                        + "Some elements may not be drown.");
                    }

                }
            }
        }
    };

    /** {@inheritDoc} */
    protected int getScriptlevelForChild(AbstractMathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return this.getAbsoluteScriptLevel();
        } else {
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

}