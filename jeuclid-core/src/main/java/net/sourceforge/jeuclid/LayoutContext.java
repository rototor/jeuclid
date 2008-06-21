/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

import net.sourceforge.jeuclid.context.Parameter;



/**
 * @version $Revision$
 */
public interface LayoutContext {

    /**
     * Retrieve a layout parameter.
     * 
     * @param which
     *            the parameter to retrieve
     * @return current value for this parameter. Please note: Some parameters
     *         may be null.
     */
    Object getParameter(Parameter which);

}
