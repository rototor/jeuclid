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

/**
 Contains a subset of the DOM implementation, as needed by JEuclid.

 <p>There where two options for the internal DOM model:</p>
 <ul>
 <li>Using an exsting implementation, such as Xerces</li>
 <li>Creating an own DOM implementation</li>
 </ul>
 <p>As most existing implementations add to the complexity and
 increase the size a lot, the second choice was made</p>
 <p>This has the disadvantage that not all standard DOM features are
 implemented. If you find something that you really need, please submit a
 patch for its implementation.</p>
 */
package net.sourceforge.jeuclid.dom;

