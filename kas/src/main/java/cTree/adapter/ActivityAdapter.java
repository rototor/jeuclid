/*
 * Copyright 2009 Erhard Kuenzel
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

package cTree.adapter;

import org.w3c.dom.*;

public abstract class ActivityAdapter extends PraefixAdapter{

	public void setCActiveProperty(){
		if (hasParent()){
			((Element) getElement().getParentNode()).setAttribute("mathbackground", "#B0C4DE");
		}
		getElement().setAttribute("mathcolor", "#FF0000");
	}
	public void removeCActiveProperty(){
		if (hasParent()){
			((Element) getElement().getParentNode()).removeAttribute("mathbackground");
		}
		getElement().removeAttribute("mathcolor");
	}
	public void setCLastProperty(){
		getElement().setAttribute("mathcolor", "#007777");
	}
	public void removeCLastProperty(){
		getElement().removeAttribute("mathcolor");
	}
	
	public boolean isActiveC(){
		return "#FF0000".equals(getElement().getAttribute("mathcolor"));
	}
	
	public boolean isLastC(){
		return "#007777s".equals(getElement().getAttribute("mathcolor"));
	}
	
}
