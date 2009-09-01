/*
 * Copyright 2009 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.support.text;

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.w3c.dom.Node;

/**
 * Helper class for handling Text Content.
 * 
 * @version $Revision$
 */
public final class TextContent {

    private TextContent() {
        // do not instantiate.
    }

    /**
     * Retrieve the text content for a given node according to MathML standards.
     * 
     * @param node
     *            Node
     * @return Text content. Always a valid String, never null.
     */
    public static String getText(final Node node) {
        final String theText = node.getTextContent();
        if (theText == null) {
            return "";
        } else {

            final StringBuilder newText = new StringBuilder();

            // As seen in 2.4.6
            newText.append(theText.trim());

            for (int i = 0; i < newText.length() - 1; i++) {
                if ((newText.charAt(i) <= AbstractJEuclidElement.TRIVIAL_SPACE_MAX)
                        && (newText.charAt(i + 1) <= AbstractJEuclidElement.TRIVIAL_SPACE_MAX)) {
                    newText.deleteCharAt(i);
                    // CHECKSTYLE:OFF
                    // This is intentional
                    i--;
                    // CHECKSTYLE:ON
                }
            }
            return CharConverter.convertEarly(newText.toString());
        }

    }
}
