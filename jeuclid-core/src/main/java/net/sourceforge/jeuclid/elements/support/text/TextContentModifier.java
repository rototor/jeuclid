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

import java.text.AttributedCharacterIterator;

import net.sourceforge.jeuclid.LayoutContext;

/**
 * 
 * @version $Revision$
 */
public interface TextContentModifier {

    /**
     * modified the given Characters to match what is required for this element.
     * 
     * @param aci
     *            {@link AttributedCharacterIterator} based on the text
     *            contents.
     * @param layoutContext
     *            current layout context.
     * @return a new {@link AttributedCharacterIterator} which is to be used
     *         instead.
     */
    AttributedCharacterIterator modifyTextContent(
            AttributedCharacterIterator aci, LayoutContext layoutContext);

}
