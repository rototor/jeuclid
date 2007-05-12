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
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLApplyElement extends MathMLContentContainer
{
    public MathMLElement          getOperator();
    public void                   setOperator(MathMLElement operator);
    public MathMLElement          getLowLimit();
    public void                   setLowLimit(MathMLElement lowLimit)
                                                         throws DOMException;
    public MathMLElement          getUpLimit();
    public void                   setUpLimit(MathMLElement upLimit)
                                                         throws DOMException;
};
  
