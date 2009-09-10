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
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Joins multiple {@link AttributedCharacterIterator}s into one.
 * 
 * @version $Revision$
 */
public class MultiAttributedCharacterIterator implements
        AttributedCharacterIterator {

    private final List<AttributedCharacterIterator> realIterators = new ArrayList<AttributedCharacterIterator>();

    private int currentList;

    /**
     * Default constructor.
     */
    public MultiAttributedCharacterIterator() {
        // nothing to do;
    }

    // CHECKSTYLE:OFF Clone is disabled.
    @Override
    public Object clone() {
        throw new UnsupportedOperationException();
    }

    // CHECKSTYLE: ON

    /**
     * Adds a new CharacterIterator
     * 
     * @param aci
     *            the new CharacterIterator to add to the list.
     */
    public void appendAttributedCharacterIterator(
            final AttributedCharacterIterator aci) {
        this.realIterators.add(aci);
        this.first();
    }

    private int sumUpToNotIncluding(final int limit) {
        int offset = 0;
        for (int i = 0; i < limit; i++) {
            final AttributedCharacterIterator ci = this.realIterators.get(i);
            offset += ci.getEndIndex() - ci.getBeginIndex();
        }
        return offset;
    }

    private int currentOffset() {
        int offset = this.sumUpToNotIncluding(this.currentList);
        offset -= this.realIterators.get(this.currentList).getBeginIndex();
        return offset;
    }

    /** {@inheritDoc} */
    public Set<Attribute> getAllAttributeKeys() {
        final Set<Attribute> retVal = new HashSet<Attribute>();
        for (final AttributedCharacterIterator ri : this.realIterators) {
            retVal.addAll(ri.getAllAttributeKeys());
        }
        return retVal;
    }

    /** {@inheritDoc} */
    public Object getAttribute(final Attribute attribute) {
        return this.realIterators.get(this.currentList).getAttribute(attribute);
    }

    /** {@inheritDoc} */
    public Map<Attribute, Object> getAttributes() {
        return this.realIterators.get(this.currentList).getAttributes();
    }

    /** {@inheritDoc} */
    public int getRunLimit() {
        return this.realIterators.get(this.currentList).getRunLimit()
                + this.currentOffset();
    }

    /** {@inheritDoc} */
    public int getRunLimit(final Attribute attribute) {
        return this.realIterators.get(this.currentList).getRunLimit(attribute)
                + this.currentOffset();
    }

    /** {@inheritDoc} */
    public int getRunLimit(final Set<? extends Attribute> attributes) {
        return this.realIterators.get(this.currentList).getRunLimit(attributes)
                + this.currentOffset();
    }

    /** {@inheritDoc} */
    public int getRunStart() {
        return this.realIterators.get(this.currentList).getRunStart()
                + this.currentOffset();
    }

    /** {@inheritDoc} */
    public int getRunStart(final Attribute attribute) {
        return this.realIterators.get(this.currentList).getRunStart(attribute)
                + this.currentOffset();
    }

    /** {@inheritDoc} */
    public int getRunStart(final Set<? extends Attribute> attributes) {
        return this.realIterators.get(this.currentList).getRunStart(attributes)
                + this.currentOffset();
    }

    /** {@inheritDoc} */
    public char current() {
        return this.realIterators.get(this.currentList).current();
    }

    /** {@inheritDoc} */
    public char first() {
        this.currentList = 0;
        return this.realIterators.get(this.currentList).first();
    }

    /** {@inheritDoc} */
    public int getBeginIndex() {
        return 0;
    }

    /** {@inheritDoc} */
    public int getEndIndex() {
        return this.sumUpToNotIncluding(this.realIterators.size());
    }

    /** {@inheritDoc} */
    public int getIndex() {
        return this.currentOffset()
                + this.realIterators.get(this.currentList).getIndex();
    }

    /** {@inheritDoc} */
    public char last() {
        this.currentList = this.realIterators.size() - 1;
        return this.realIterators.get(this.currentList).last();
    }

    /** {@inheritDoc} */
    public char next() {
        char c = this.realIterators.get(this.currentList).next();
        while ((c == CharacterIterator.DONE)
                && (this.currentList < this.realIterators.size() - 1)) {
            this.currentList++;
            c = this.realIterators.get(this.currentList).first();
        }
        return c;
    }

    /** {@inheritDoc} */
    public char previous() {
        char c = this.realIterators.get(this.currentList).previous();
        while ((c == CharacterIterator.DONE) && (this.currentList > 0)) {
            this.currentList--;
            c = this.realIterators.get(this.currentList).previous();
        }
        return c;
    }

    /** {@inheritDoc} */
    public char setIndex(final int position) {
        int prev = 0;
        int offset = 0;
        for (int i = 0; i < this.realIterators.size(); i++) {
            final AttributedCharacterIterator ci = this.realIterators.get(i);
            prev = offset;
            final int beginIndex = ci.getBeginIndex();
            offset += ci.getEndIndex() - beginIndex;
            if (((prev <= position) && (offset > position))
                    || ((offset == position) && (i == this.realIterators.size() - 1))) {
                this.currentList = i;
                return ci.setIndex(beginIndex + position - prev);
            }
        }
        throw new IllegalArgumentException();
    }
}
