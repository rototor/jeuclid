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

package net.sourceforge.jeuclid.layout;

/**
 * Defines in what stage the current layout it.
 * <p>
 * The Stages are {@link #NONE}, {@link #STAGE1}, and {@link #STAGE2}.
 * 
 * @version $Revision$
 */
public enum LayoutStage {
    /**
     * No layout done yet.
     * <p>
     * The {@link LayoutInfo} will not contain any useful information.
     */
    NONE,
    /**
     * Context-Insensitive Layout.
     * <p>
     * A Node in this stage has been layouted based on its own information and
     * on the information of its children. The {@link LayoutInfo} is useful to
     * do calculations such as stretching of parentheses.
     */
    STAGE1,
    /**
     * Full layout.
     * <p>
     * A node in this stage has been fully layouted, the {@link LayoutInfo} is
     * current and final.
     */
    STAGE2,
}
