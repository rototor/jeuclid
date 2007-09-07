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
import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.support.MathMLNodeListImpl;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMultiScriptsElement;
import org.w3c.dom.mathml.MathMLNodeList;

/**
 * Prescripts and Tensor Indices.
 * 
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

    // /**
    // * Logger for this class
    // */
    // currently unused
    // private static final Log LOGGER = LogFactory
    // .getLog(MathMultiScripts.class);
    private static final int STATE_POSTSUB = 0;

    private static final int STATE_POSTSUPER = 1;

    private static final int STATE_PRESUB = 2;

    private static final int STATE_PRESUPER = 3;

    private final List<JEuclidElement> postsubscripts = new Vector<JEuclidElement>();

    private final List<JEuclidElement> postsuperscripts = new Vector<JEuclidElement>();

    private final List<JEuclidElement> presubscripts = new Vector<JEuclidElement>();

    private final List<JEuclidElement> presuperscripts = new Vector<JEuclidElement>();

    private boolean inRewriteChildren;

    private Graphics2D lastCalculatedFor;

    private float subBaselineShift;

    private float superBaselineShift;

    private float ascentHeight;

    private float descentHeight;

    private float width;

    /**
     * Default constructor.
     */
    public Mmultiscripts() {
        super();
        this.inRewriteChildren = false;
        this.lastCalculatedFor = null;
    }

    /** {@inheritDoc} */
    @Override
    protected void changeHook() {
        super.changeHook();
        if (!this.inRewriteChildren) {
            this.parseChildren();
        }
        this.lastCalculatedFor = null;
    }

    private void parseChildren() {
        this.presubscripts.clear();
        this.presuperscripts.clear();
        this.postsubscripts.clear();
        this.postsuperscripts.clear();
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        int state = Mmultiscripts.STATE_POSTSUB;
        final int len = childList.getLength();
        for (int i = 1; i < len; i++) {
            final Node child = childList.item(i);
            if (child instanceof Mprescripts) {
                state = Mmultiscripts.STATE_PRESUB;
            } else if (child instanceof JEuclidElement) {
                final JEuclidElement jchild = (JEuclidElement) child;
                if (state == Mmultiscripts.STATE_POSTSUB) {
                    this.postsubscripts.add(jchild);
                    state = Mmultiscripts.STATE_POSTSUPER;
                } else if (state == Mmultiscripts.STATE_POSTSUPER) {
                    this.postsuperscripts.add(jchild);
                    state = Mmultiscripts.STATE_POSTSUB;
                } else if (state == Mmultiscripts.STATE_PRESUB) {
                    this.presubscripts.add(jchild);
                    state = Mmultiscripts.STATE_PRESUPER;
                } else {
                    this.presuperscripts.add(jchild);
                    state = Mmultiscripts.STATE_PRESUB;
                }
            }
        }
        if (this.postsuperscripts.size() < this.postsubscripts.size()) {
            this.postsuperscripts.add(new None());
        }
        if (this.presuperscripts.size() < this.presubscripts.size()) {
            this.presuperscripts.add(new None());
        }
    }

    // private void calculateSpecs(final Graphics2D g) {
    // if (g == this.lastCalculatedFor) {
    // return;
    // }
    // this.lastCalculatedFor = g;
    // final JEuclidElement base = this.getBase();
    //
    // this.subBaselineShift = 0.0f;
    // this.superBaselineShift = 0.0f;
    //
    // float maxSupAscent = 0.0f;
    // float maxSubDescent = 0.0f;
    //
    // this.width = base.getWidth(g);
    //
    // for (int i = 0; i < this.postsubscripts.size(); i++) {
    // final JEuclidElement sub = this.postsubscripts.get(i);
    // final JEuclidElement sup = this.postsuperscripts.get(i);
    // final float esubbaselineshift = ScriptSupport
    // .getSubBaselineShift(g, base, sub, sup);
    // final float esupbaselineshift = ScriptSupport
    // .getSuperBaselineShift(g, base, sub, sup);
    // this.subBaselineShift = Math.max(this.subBaselineShift,
    // esubbaselineshift);
    // this.superBaselineShift = Math.max(this.superBaselineShift,
    // esupbaselineshift);
    // maxSupAscent = Math.max(maxSupAscent, sup.getAscentHeight(g));
    // maxSubDescent = Math.max(maxSubDescent, sub.getDescentHeight(g));
    // this.width += Math.max(sub.getWidth(g), sup.getWidth(g));
    // }
    // for (int i = 0; i < this.presubscripts.size(); i++) {
    // final JEuclidElement sub = this.presubscripts.get(i);
    // final JEuclidElement sup = this.presuperscripts.get(i);
    // final float esubbaselineshift = ScriptSupport
    // .getSubBaselineShift(g, base, sub, sup);
    // final float esupbaselineshift = ScriptSupport
    // .getSuperBaselineShift(g, base, sub, sup);
    // this.subBaselineShift = Math.max(this.subBaselineShift,
    // esubbaselineshift);
    // this.superBaselineShift = Math.max(this.superBaselineShift,
    // esupbaselineshift);
    // maxSupAscent = Math.max(maxSupAscent, sup.getAscentHeight(g));
    // maxSubDescent = Math.max(maxSubDescent, sub.getDescentHeight(g));
    // this.width += Math.max(sub.getWidth(g), sup.getWidth(g));
    // }
    //
    // this.ascentHeight = Math.max(base.getAscentHeight(g),
    // this.superBaselineShift + maxSupAscent);
    // this.descentHeight = Math.max(base.getDescentHeight(g),
    // this.subBaselineShift + maxSubDescent);
    // }
    //
    // /**
    // * Paints this element.
    // *
    // * @param g
    // * The graphics context to use for painting
    // * @param posX
    // * The first left position for painting
    // * @param posY
    // * The position of the baseline
    // */
    // @Override
    // public final void paint(final Graphics2D g, final float posX,
    // final float posY) {
    // super.paint(g, posX, posY);
    // this.calculateSpecs(g);
    // final JEuclidElement base = this.getBase();
    //
    // float pos = posX;
    // for (int i = 0; i < this.presubscripts.size(); i++) {
    // final JEuclidElement sub = this.presubscripts.get(i);
    // final JEuclidElement sup = this.presuperscripts.get(i);
    // sub.paint(g, pos, posY + this.subBaselineShift);
    // sup.paint(g, pos, posY - this.superBaselineShift);
    // pos += Math.max(sub.getWidth(g), sup.getWidth(g));
    // }
    // base.paint(g, pos, posY);
    // pos += base.getWidth(g);
    // for (int i = 0; i < this.postsubscripts.size(); i++) {
    // final JEuclidElement sub = this.postsubscripts.get(i);
    // final JEuclidElement sup = this.postsuperscripts.get(i);
    // sub.paint(g, pos, posY + this.subBaselineShift);
    // sup.paint(g, pos, posY - this.superBaselineShift);
    // pos += Math.max(sub.getWidth(g), sup.getWidth(g));
    // }
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public final float calculateWidth(final Graphics2D g) {
    // this.calculateSpecs(g);
    // return this.width;
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public final float calculateAscentHeight(final Graphics2D g) {
    // this.calculateSpecs(g);
    // return this.ascentHeight;
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public final float calculateDescentHeight(final Graphics2D g) {
    // this.calculateSpecs(g);
    // return this.descentHeight;
    //
    // }

    /** {@inheritDoc} */
    @Override
    public boolean hasChildPrescripts(final JEuclidElement child) {
        return child.isSameNode(this.getBase())
                && this.getNumprescriptcolumns() > 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasChildPostscripts(final JEuclidElement child) {
        return child.isSameNode(this.getBase())
                && this.getNumscriptcolumns() > 0;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mmultiscripts.ELEMENT;
    }

    /** {@inheritDoc} */
    public JEuclidElement getBase() {
        final JEuclidElement base = this.getMathElement(0);
        if (base == null) {
            return new None();
        } else {
            return base;
        }
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

    /** {@inheritDoc} */
    public int getNumprescriptcolumns() {
        return this.presubscripts.size();
    }

    /** {@inheritDoc} */
    public int getNumscriptcolumns() {
        return this.postsubscripts.size();
    }

    /** {@inheritDoc} */
    public MathMLElement getPreSubScript(final int colIndex) {
        return this.presubscripts.get(colIndex - 1);
    }

    /** {@inheritDoc} */
    public MathMLElement getPreSuperScript(final int colIndex) {
        return this.presuperscripts.get(colIndex - 1);
    }

    /** {@inheritDoc} */
    public MathMLNodeList getPrescripts() {
        final List<Node> list = new Vector<Node>();
        for (int i = 0; i < this.presubscripts.size(); i++) {
            list.add(this.presubscripts.get(i));
            list.add(this.presuperscripts.get(i));
        }
        return new MathMLNodeListImpl(list);
    }

    /** {@inheritDoc} */
    public MathMLNodeList getScripts() {
        final List<Node> list = new Vector<Node>();
        for (int i = 0; i < this.postsubscripts.size(); i++) {
            list.add(this.postsubscripts.get(i));
            list.add(this.postsuperscripts.get(i));
        }
        return new MathMLNodeListImpl(list);
    }

    /** {@inheritDoc} */
    public MathMLElement getSubScript(final int colIndex) {
        return this.postsubscripts.get(colIndex - 1);
    }

    /** {@inheritDoc} */
    public MathMLElement getSuperScript(final int colIndex) {
        return this.postsuperscripts.get(colIndex - 1);
    }

    private void rewriteChildren() {
        this.inRewriteChildren = true;

        final org.w3c.dom.NodeList childList = this.getChildNodes();
        final int len = childList.getLength();
        // start at 1 since 0 is the base!
        for (int i = 1; i < len; i++) {
            this.removeChild(childList.item(1));
        }
        if (len == 0) {
            this.addMathElement(new None());
        }
        for (int i = 0; i < this.postsubscripts.size(); i++) {
            this.addMathElement(this.postsubscripts.get(i));
            this.addMathElement(this.postsuperscripts.get(i));
        }
        final int numprescripts = this.presubscripts.size();
        if (numprescripts > 0) {
            this.addMathElement(new Mprescripts());
            for (int i = 0; i < numprescripts; i++) {
                this.addMathElement(this.presubscripts.get(i));
                this.addMathElement(this.presuperscripts.get(i));
            }
        }
        this.inRewriteChildren = false;
    }

    /** {@inheritDoc} */
    public MathMLElement insertPreSubScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        final int targetIndex;
        if (colIndex == 0) {
            targetIndex = this.presubscripts.size();
        } else {
            targetIndex = colIndex - 1;
        }
        this.presubscripts.add(targetIndex, (JEuclidElement) newScript);
        this.presuperscripts.add(targetIndex, new None());
        this.rewriteChildren();
        return newScript;
    }

    /** {@inheritDoc} */
    public MathMLElement insertPreSuperScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        final int targetIndex;
        if (colIndex == 0) {
            targetIndex = this.presubscripts.size();
        } else {
            targetIndex = colIndex - 1;
        }
        this.presubscripts.add(targetIndex, new None());
        this.presuperscripts.add(targetIndex, (JEuclidElement) newScript);
        this.rewriteChildren();
        return newScript;
    }

    /** {@inheritDoc} */
    public MathMLElement insertSubScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        final int targetIndex;
        if (colIndex == 0) {
            targetIndex = this.postsubscripts.size();
        } else {
            targetIndex = colIndex - 1;
        }
        this.postsubscripts.add(targetIndex, (JEuclidElement) newScript);
        this.postsuperscripts.add(targetIndex, new None());
        this.rewriteChildren();
        return newScript;
    }

    /** {@inheritDoc} */
    public MathMLElement insertSuperScriptBefore(final int colIndex,
            final MathMLElement newScript) {
        final int targetIndex;
        if (colIndex == 0) {
            targetIndex = this.postsubscripts.size();
        } else {
            targetIndex = colIndex - 1;
        }
        this.postsubscripts.add(targetIndex, new None());
        this.postsuperscripts.add(targetIndex, (JEuclidElement) newScript);
        this.rewriteChildren();
        return newScript;
    }

    /** {@inheritDoc} */
    public MathMLElement setPreSubScriptAt(final int colIndex,
            final MathMLElement newScript) {
        final int targetCol = colIndex - 1;
        if (targetCol == this.presubscripts.size()) {
            return this.insertPreSubScriptBefore(0, newScript);
        } else {
            this.presubscripts.set(targetCol, (JEuclidElement) newScript);
            this.rewriteChildren();
            return newScript;
        }
    }

    /** {@inheritDoc} */
    public MathMLElement setPreSuperScriptAt(final int colIndex,
            final MathMLElement newScript) {
        final int targetCol = colIndex - 1;
        if (targetCol == this.presuperscripts.size()) {
            return this.insertPreSuperScriptBefore(0, newScript);
        } else {
            this.presuperscripts.set(targetCol, (JEuclidElement) newScript);
            this.rewriteChildren();
            return newScript;
        }
    }

    /** {@inheritDoc} */
    public MathMLElement setSubScriptAt(final int colIndex,
            final MathMLElement newScript) {
        final int targetCol = colIndex - 1;
        if (targetCol == this.postsubscripts.size()) {
            return this.insertSubScriptBefore(0, newScript);
        } else {
            this.postsubscripts.set(targetCol, (JEuclidElement) newScript);
            this.rewriteChildren();
            return newScript;
        }
    }

    /** {@inheritDoc} */
    public MathMLElement setSuperScriptAt(final int colIndex,
            final MathMLElement newScript) {
        final int targetCol = colIndex - 1;
        if (targetCol == this.postsuperscripts.size()) {
            return this.insertSuperScriptBefore(0, newScript);
        } else {
            this.postsuperscripts.set(targetCol, (JEuclidElement) newScript);
            this.rewriteChildren();
            return newScript;
        }
    }

}
