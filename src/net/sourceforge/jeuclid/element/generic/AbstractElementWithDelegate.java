/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

/* $Id: AbstractElementWithDelegate.java,v 1.3.2.3 2006/09/15 20:49:25 maxberger Exp $ */

package net.sourceforge.jeuclid.element.generic;

import java.awt.Graphics;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.dom.ChangeTrackingElement;
import net.sourceforge.jeuclid.element.helpers.AttributeMap;

/**
 * Generic class for all mathobjects that can be represented using other Math
 * objects. These math objects use a delegate for the actual display and
 * calculations.
 * <p>
 * To use this class, overwrite {@link #createDelegate()} to create the delegate
 * object.
 * <p>
 * Call {@link ChangeTrackingElement#setChanged(boolean)} when something has
 * changed that may change the delegate object.
 * 
 * @author Max Berger
 */
public abstract class AbstractElementWithDelegate extends AbstractMathContainer {
	private AbstractMathElement delegate;

	/**
	 * default constructor.
	 * 
	 * @param base
	 *            the MathBase to use.
	 */
	public AbstractElementWithDelegate(MathBase base) {
		super(base);
	}

	/**
	 * Overwrite this function in your implementation.
	 * 
	 * @return a MathObject representing the real contents.
	 */
	abstract protected AbstractMathElement createDelegate();

	private void prepareDelegate() {
		delegate = createDelegate();
		delegate.setFakeParent(this);
		this.setChanged(false);
	}

	/** {@inheritDoc} */
	public void eventInitSpecificAttributes(AttributeMap attributes) {
		super.eventInitSpecificAttributes(attributes);
		prepareDelegate();
		delegate.eventInitSpecificAttributes(attributes);
	}

	/** {@inheritDoc} */
	public int calculateAscentHeight(Graphics g) {
		if (this.isChanged()) {
			prepareDelegate();
		}
		return delegate.calculateAscentHeight(g);
	}

	/** {@inheritDoc} */
	public int calculateDescentHeight(Graphics g) {
		if (this.isChanged()) {
			prepareDelegate();
		}
		return delegate.calculateDescentHeight(g);
	}

	/** {@inheritDoc} */
	public int calculateWidth(Graphics g) {
		if (this.isChanged()) {
			prepareDelegate();
		}
		return delegate.calculateWidth(g);
	}

	/** {@inheritDoc} */
	public void paint(Graphics g, int posX, int posY) {
		if (this.isChanged()) {
			prepareDelegate();
		}
		delegate.paint(g, posX, posY);
	}

	/** {@inheritDoc} */
	protected void setChanged(boolean hasChanged) {
		super.setChanged(hasChanged);
		if (delegate != null) {
			delegate.setChanged(hasChanged);
		}
	}
}
