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

package net.sourceforge.jeuclid.util;

/**
 * A class to hold parameters for the rendering process.
 * 
 * @author Erik Putrycz, Max Berger
 */
public enum ParameterKey {
    /**
     * File type for the output used by converter functions. Must be a valid
     * mime-type.
     */
    OutFileType,
    /**
     * Font size used for the output. Defaults to 12.0pt.
     */
    FontSize,
    /**
     * Debug mode. If true, elements will have borders drawn around them.
     */
    DebugMode,
    /** 
     * Anti-Alias mode for rendering.
     */
    AntiAlias
}
