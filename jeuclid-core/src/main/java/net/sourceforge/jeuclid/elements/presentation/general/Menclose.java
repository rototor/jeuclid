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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.AbstractElementWithDelegates;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.token.Mspace;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.mathml.MathMLEncloseElement;

/**
 * Class for supporting "menclose" elements.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class Menclose extends AbstractElementWithDelegates implements
        MathMLEncloseElement {

    /**
     * Char for rendering left part of the long division.
     */
    public static final char LONGDIV_CHAR = ')';

    /**
     * Represents the US long-division notation, to support the notation
     * "longdiv".
     * 
     * @author Max Berger
     */
    public static class Longdiv extends AbstractRoot {
        /**
         * Default constructor.
         * 
         * @param base
         *            MathBase to use.
         */
        public Longdiv(final MathBase base) {
            super(base, Menclose.LONGDIV_CHAR);
        }

        /** {@inheritDoc} */
        @Override
        protected JEuclidElement getActualIndex() {
            return new Mspace(this.getMathBase());
        }

        /** {@inheritDoc} */
        @Override
        protected List<JEuclidElement> getContent() {
            return ElementListSupport.createListOfChildren(this);
        }

        /** {@inheritDoc} */
        public String getTagName() {
            return "";
        }

    }

    /**
     * base class for all row-like notations.
     * 
     * @author Max Berger
     */
    public abstract static class AbstractRowLikeNotation extends
            AbstractRowLike {

        /**
         * Default constructor.
         * 
         * @param base
         *            MathBase to use.
         */
        public AbstractRowLikeNotation(final MathBase base) {
            super(base);
        }

        /** {@inheritDoc} */
        public String getTagName() {
            return "";
        }
    }

    /**
     * base class for all row-like notations.
     * 
     * @author Max Berger
     */
    public static class Updiagonalstrike extends
            Menclose.AbstractRowLikeNotation {

        /**
         * Default constructor.
         * 
         * @param base
         *            MathBase to use.
         */
        public Updiagonalstrike(final MathBase base) {
            super(base);
        }

        /** {@inheritDoc} */
        @Override
        public void paint(final Graphics2D g, final float posX,
                final float posY) {
            super.paint(g, posX, posY);
            final Stroke oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(GraphicsSupport.lineWidth(this)));
            g.draw(new Line2D.Float(posX, posY, posX + this.getWidth(g), posY
                    - this.getHeight(g)));
            g.setStroke(oldStroke);
        }
    }

    /**
     * base class for all row-like notations.
     * 
     * @author Max Berger
     */
    public static class Downdiagonalstrike extends
            Menclose.AbstractRowLikeNotation {

        /**
         * Default constructor.
         * 
         * @param base
         *            MathBase to use.
         */
        public Downdiagonalstrike(final MathBase base) {
            super(base);
        }

        /** {@inheritDoc} */
        @Override
        public void paint(final Graphics2D g, final float posX,
                final float posY) {
            super.paint(g, posX, posY);
            final Stroke oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(GraphicsSupport.lineWidth(this)));
            g.draw(new Line2D.Float(posX, posY - this.getHeight(g), posX
                    + this.getWidth(g), posY));
            g.setStroke(oldStroke);
        }
    }

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "menclose";

    /** The notation attribute. */
    public static final String ATTR_NOTATION = "notation";

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Menclose.class);

    private static final Map<String, Constructor<?>> IMPL_CLASSES = new HashMap<String, Constructor<?>>();;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Menclose(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(Menclose.ATTR_NOTATION, "");
    }

    /**
     * @return notation of menclose element
     */
    public String getNotation() {
        return this.getMathAttribute(Menclose.ATTR_NOTATION);
    }

    /**
     * Sets notation for menclose element.
     * 
     * @param notation
     *            Notation
     */
    public void setNotation(final String notation) {
        this.setAttribute(Menclose.ATTR_NOTATION, notation);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Menclose.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    protected List<JEuclidElement> createDelegates() {
        final String[] notations = this.getNotation().split(" ");
        final Stack<Constructor<?>> notationImpls = new Stack<Constructor<?>>();
        for (final String curNotation : notations) {
            final Constructor<?> con = Menclose.IMPL_CLASSES.get(curNotation
                    .toLowerCase(Locale.ENGLISH));
            if (con != null) {
                notationImpls.push(con);
            } else if (curNotation.length() > 0) {
                Menclose.LOGGER.info("Unsupported notation for menclose: "
                        + curNotation);
            }
        }
        // This is just to make sure that there is at least one delegate, and
        // that each of the standard delegates has exactly one child.
        JEuclidElement lastChild;
        if (this.getMathElementCount() != 1) {
            lastChild = new Mrow(this.getMathBase());
            for (final JEuclidElement child : ElementListSupport
                    .createListOfChildren(this)) {
                lastChild.appendChild(child);
            }
        } else {
            lastChild = this.getMathElement(0);
        }
        while (!notationImpls.isEmpty()) {
            final Constructor<?> con = notationImpls.pop();
            try {
                final JEuclidElement element = (JEuclidElement) con
                        .newInstance(this.getMathBase());
                element.appendChild(lastChild);
                lastChild = element;
            } catch (final InstantiationException e) {
                Menclose.LOGGER.warn(e);
            } catch (final IllegalAccessException e) {
                Menclose.LOGGER.warn(e);
            } catch (final InvocationTargetException e) {
                Menclose.LOGGER.warn(e);
            }
        }
        final List<JEuclidElement> delegates = new Vector<JEuclidElement>(1);
        delegates.add(lastChild);
        return delegates;
    }

    static {
        try {
            Menclose.IMPL_CLASSES.put("radical", Msqrt.class
                    .getConstructor(MathBase.class));
            Menclose.IMPL_CLASSES.put("longdiv", Menclose.Longdiv.class
                    .getConstructor(MathBase.class));
            Menclose.IMPL_CLASSES.put("updiagonalstrike",
                    Menclose.Updiagonalstrike.class
                            .getConstructor(MathBase.class));
            Menclose.IMPL_CLASSES.put("downdiagonalstrike",
                    Menclose.Downdiagonalstrike.class
                            .getConstructor(MathBase.class));
        } catch (NoSuchMethodException e) {
            Menclose.LOGGER.fatal(e);
        }
    }
}
