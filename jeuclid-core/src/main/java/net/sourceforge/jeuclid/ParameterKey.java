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

package net.sourceforge.jeuclid;

/**
 * A class to hold parameters for the rendering process.
 * <p>
 * Each parameter is passed using the String datatype, and then converted back
 * into the internal dataype. The following assumptions are made for the given
 * datatypes:
 * <dl>
 * <dt>float</dt>
 * <dd>must be a valid integer or floating point number, parsable with
 * {@link Float#parseFloat(String)}.</dd>
 * <dt>boolean</dt>
 * <dd>must be "true" or "false", parsable with
 * {@link Boolean#parseBoolean(String)}.</dd>
 * <dt>font list</dt>
 * <dd>is a comma separated lists of font families which should be used when
 * a font of this type is selected. When selecting the actual font, the list
 * is followed in the order given, and the first font which is installed on
 * the system and contains the needed character is used. It is therefore
 * suggested to add "complete" unicode fonts to the end of the list to support
 * all characters.</dd>
 * <dt>colors</dt>
 * <dd>are passed using the standard html colornames or the #rrggbb or
 * #rrggbbaa notation.</dd>
 * </dl>
 * 
 * @author Erik Putrycz, Max Berger
 * @version $Revision$
 * @deprecated
 */
public enum ParameterKey {
    /**
     * Font size (float) used for the output. Defaults to 12.0pt.
     */
    FontSize,
    /**
     * Debug mode (boolean). If true, elements will have borders drawn around
     * them.
     */
    DebugMode,
    /**
     * Anti-Alias mode (boolean) for rendering.
     */
    AntiAlias,
    /**
     * Default foreground color (String). See 3.2.2.2
     */
    ForegroundColor,
    /**
     * Default background color (String). See 3.2.2.2
     */
    BackgroundColor,
    /**
     * Comma separated list of font families for sans-serif.
     * 
     * @see ParameterKey
     */
    FontsSanserif,
    /**
     * Comma separated list of font families for serif.
     * 
     * @see ParameterKey
     */
    FontsSerif,
    /**
     * Comma separated list of font families for monospaced.
     * 
     * @see ParameterKey
     */
    FontsMonospaced,
    /**
     * Comma separated list of font families for script.
     * 
     * @see ParameterKey
     */
    FontsScript,
    /**
     * Comma separated list of font families for fraktur.
     * 
     * @see ParameterKey
     */
    FontsFraktur,
    /**
     * Comma separated list of font families for double-struck.
     * 
     * @see ParameterKey
     */
    FontsDoublestruck,
}
