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

package net.sourceforge.jeuclid.element;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

/**
 * This class arrange a element over an other element.
 * 
 */
public class MathOver extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mover";

    private boolean m_accent = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathOver(final MathBase base) {
        super(base);
    }

    /**
     * Set accent for mover.
     * 
     * @param accent
     *            Value of accent attribute
     */
    public void setAccent(final boolean accent) {
        this.m_accent = accent;
    }

    /**
     * @return Accent
     */
    public boolean getAccent() {
        return this.m_accent;
    }

    /**
     * Space between base and under in pixels
     */
    private int getOverSpace(final Graphics2D g) {
        return (int) AttributesHelper.convertSizeToPt(
                MathUnderOver.UNDER_OVER_SPACE, this, AttributesHelper.PT);
    };

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
    public void paint(final Graphics2D g, final int posX, int posY) {
        super.paint(g, posX, posY);
        final MathElement e1 = this.getMathElement(0);
        final MathElement e2 = this.getMathElement(1);

        if ((this.getMathElement(0) instanceof MathOperator)
                && Boolean.parseBoolean(((MathOperator) this.getMathElement(0))
                        .getMovablelimits())) {
            final int middleshift = (int) (e1.getHeight(g) * MathSubSup.DEFAULT_SCRIPTSHIFT);
            int e1DescentHeight = e1.getDescentHeight(g);
            if (e1DescentHeight == 0) {
                e1DescentHeight = this.getFontMetrics(g).getDescent();
            }
            int e1AscentHeight = e1.getAscentHeight(g);
            if (e1AscentHeight == 0) {
                e1AscentHeight = this.getFontMetrics(g).getAscent();
            }
            final int posY2 = posY - e1AscentHeight + middleshift
                    - e2.getDescentHeight(g) + 1;

            e1.paint(g, posX, posY);
            e2.paint(g, posX + e1.getWidth(g), posY2);
        } else {
            final int width = this.getWidth(g);
            e1.paint(g, posX + (width - e1.getWidth(g)) / 2, posY);
            posY = posY - e1.getAscentHeight(g) - e2.getDescentHeight(g)
                    - this.getOverSpace(g) - 1;
            if (this.getAccent()) {
                posY = posY - this.getOverSpace(g);
            }
            e2.paint(g, posX + (width - e2.getWidth(g)) / 2, posY);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        if ((this.getMathElement(0) instanceof MathOperator)) {
            if (Boolean.parseBoolean(((MathOperator) this.getMathElement(0)).getMovablelimits())) {
                return this.getMathElement(0).getWidth(g)
                        + this.getMathElement(1).getWidth(g);
            }
        }
        return Math.max(this.getMathElement(0).getWidth(g), this
                .getMathElement(1).getWidth(g));
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        int res;
        if ((this.getMathElement(0) instanceof MathOperator)
                && Boolean.parseBoolean(((MathOperator) this.getMathElement(0))
                        .getMovablelimits())) {
            res = Math.max(this.getMathElement(0).getAscentHeight(g), this
                    .getMathElement(1).getHeight(g)
                    + this.getMiddleShift(g));
        } else {
            res = this.getMathElement(0).getAscentHeight(g)
                    + this.getMathElement(1).getHeight(g)
                    + this.getOverSpace(g);
            if (this.getAccent()) {
                res = res + this.getOverSpace(g);
            }
        }
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        return this.getMathElement(0).getDescentHeight(g);
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final MathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return this.getAbsoluteScriptLevel();
        } else {
            // TODO: Should depend on type and accent
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final MathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return super.isChildBlock(child);
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathOver.ELEMENT;
    }

}
