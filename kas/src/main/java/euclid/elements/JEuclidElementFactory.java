/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package euclid.elements;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


import org.apache.batik.dom.AbstractNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import euclid.elements.content.semantic.Annotation;
import euclid.elements.content.semantic.Semantics;
import euclid.elements.generic.MathImpl;
import euclid.elements.presentation.enlivening.Maction;
import euclid.elements.presentation.general.Menclose;
import euclid.elements.presentation.general.Merror;
import euclid.elements.presentation.general.Mfenced;
import euclid.elements.presentation.general.Mfrac;
import euclid.elements.presentation.general.Mpadded;
import euclid.elements.presentation.general.Mphantom;
import euclid.elements.presentation.general.Mroot;
import euclid.elements.presentation.general.Mrow;
import euclid.elements.presentation.general.Msqrt;
import euclid.elements.presentation.general.Mstyle;
import euclid.elements.presentation.script.Mmultiscripts;
import euclid.elements.presentation.script.Mover;
import euclid.elements.presentation.script.Mprescripts;
import euclid.elements.presentation.script.Msub;
import euclid.elements.presentation.script.Msubsup;
import euclid.elements.presentation.script.Msup;
import euclid.elements.presentation.script.Munder;
import euclid.elements.presentation.script.Munderover;
import euclid.elements.presentation.script.None;
import euclid.elements.presentation.table.Maligngroup;
import euclid.elements.presentation.table.Malignmark;
import euclid.elements.presentation.table.Mlabeledtr;
import euclid.elements.presentation.table.Mtable;
import euclid.elements.presentation.table.Mtd;
import euclid.elements.presentation.table.Mtr;
import euclid.elements.presentation.token.Mglyph;
import euclid.elements.presentation.token.Mi;
import euclid.elements.presentation.token.Mn;
import euclid.elements.presentation.token.Mo;
import euclid.elements.presentation.token.Ms;
import euclid.elements.presentation.token.Mspace;
import euclid.elements.presentation.token.Mtext;

/**
 * Creates MathElements from given element strings.
 * 
 * @version $Revision$
 */
public final class JEuclidElementFactory {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(JEuclidElementFactory.class);

    private static final Map<String, Constructor<?>> IMPL_CLASSES = new HashMap<String, Constructor<?>>();;

    private JEuclidElementFactory() {
        // Empty on purpose
    }

    /**
     * Factory for MathML Elements.
     * 
     * @param localName
     *            name of the element without namespaces.
     * @param ownerDocument
     *            Document this element belongs to.
     * @return A new MathElement for this tag name.
     */
    public static Element elementFromName(final String localName,
            final Document ownerDocument) {

        final Constructor<?> con = JEuclidElementFactory.IMPL_CLASSES
                .get(localName);

        JEuclidElement element = null;
        if (con != null) {
            try {
                element = (JEuclidElement) con.newInstance();
            } catch (final InstantiationException e) {
                element = null;
            } catch (final IllegalAccessException e) {
                element = null;
            } catch (final InvocationTargetException e) {
                element = null;
            }
        }
        if (element == null) {
            // JEuclidElementFactory.LOGGER.info("Unsupported element: "
            // + localName);
            element = new Mrow();
        }
        ((AbstractNode) element).setOwnerDocument(ownerDocument);
        ((AbstractNode) element).setNodeName(localName);
        return element;
    }

    private static void addClass(final Class<?> c) {
        try {
            final Field f = c.getField("ELEMENT");
            final String tag = (String) f.get(null);
            JEuclidElementFactory.IMPL_CLASSES.put(tag, c.getConstructor());
        } catch (final NoSuchFieldException e) {
            JEuclidElementFactory.LOGGER.warn(c.toString(), e);
        } catch (final NoSuchMethodException e) {
            JEuclidElementFactory.LOGGER.warn(c.toString(), e);
        } catch (final IllegalAccessException e) {
            JEuclidElementFactory.LOGGER.warn(c.toString(), e);
        }

    }

    // CHECKSTYLE:OFF
    static {
        // CHECKSTYLE:ON
        JEuclidElementFactory.addClass(MathImpl.class);
        JEuclidElementFactory.addClass(Mfenced.class);
        JEuclidElementFactory.addClass(Mfrac.class);
        JEuclidElementFactory.addClass(Menclose.class);
        JEuclidElementFactory.addClass(Mphantom.class);
        JEuclidElementFactory.addClass(Msup.class);
        JEuclidElementFactory.addClass(Msub.class);
        JEuclidElementFactory.addClass(Mmultiscripts.class);
        JEuclidElementFactory.addClass(Mprescripts.class);
        JEuclidElementFactory.addClass(None.class);
        JEuclidElementFactory.addClass(Msubsup.class);
        JEuclidElementFactory.addClass(Munder.class);
        JEuclidElementFactory.addClass(Mover.class);
        JEuclidElementFactory.addClass(Munderover.class);
        JEuclidElementFactory.addClass(Mspace.class);
        JEuclidElementFactory.addClass(Ms.class);
        JEuclidElementFactory.addClass(Mstyle.class);
        JEuclidElementFactory.addClass(Msqrt.class);
        JEuclidElementFactory.addClass(Mroot.class);
        JEuclidElementFactory.addClass(Mtable.class);
        JEuclidElementFactory.addClass(Mtr.class);
        JEuclidElementFactory.addClass(Mlabeledtr.class);
        JEuclidElementFactory.addClass(Mtd.class);
        JEuclidElementFactory.addClass(Mo.class);
        JEuclidElementFactory.addClass(Mi.class);
        JEuclidElementFactory.addClass(Mn.class);
        JEuclidElementFactory.addClass(Mtext.class);
        JEuclidElementFactory.addClass(Mrow.class);
        JEuclidElementFactory.addClass(Maligngroup.class);
        JEuclidElementFactory.addClass(Malignmark.class);
        JEuclidElementFactory.addClass(Semantics.class);
        JEuclidElementFactory.addClass(Annotation.class);
        JEuclidElementFactory.addClass(Mpadded.class);
        JEuclidElementFactory.addClass(Merror.class);
        JEuclidElementFactory.addClass(Maction.class);
        JEuclidElementFactory.addClass(Mglyph.class);
    }

}
