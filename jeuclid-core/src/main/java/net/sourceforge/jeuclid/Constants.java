/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid;

import net.jcip.annotations.Immutable;

/**
 * Constants which are shared in multiple classes.
 * 
 * @version $Revision$
 */
@Immutable
public final class Constants {
    /** String for numeric zero. */
    public static final String ZERO = "0";

    /** String constant for true. */
    public static final String TRUE = "true";

    /** String constant for false. */
    public static final String FALSE = "false";

    /** Default font-size (no scaling). */
    public static final float DEFAULT_FONTSIZE = 12.0f;

    /** Default ScriptSize multiplier. */
    public static final float DEFAULT_SCIPTSIZEMULTIPLIER = 0.71f;

    /** Default MIME Type for MathML documents. */
    public static final String MATHML_MIMETYPE = "application/mathml+xml";

    /**
     * Name space for JEuclid specific context extension.
     */
    public static final String NS_CONTEXT = "http://jeuclid.sf.net/ns/context";

    /**
     * Name space for JEuclid specific extensions.
     */
    public static final String NS_JEUCLID_EXT = "http://jeuclid.sf.net/ns/ext";

    /**
     * Default Constructor.
     */
    private Constants() {
        // Empty on purpose
    }

}
