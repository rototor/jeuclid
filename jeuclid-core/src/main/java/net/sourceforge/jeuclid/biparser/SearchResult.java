/*
 * Copyright 2009 - 2010 JEuclid, http://jeuclid.sf.net
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

/* $Id $ */

package net.sourceforge.jeuclid.biparser;

/**
 * Tree search result (used for the cursor).
 * 
 * @version $Revision$
 */
public class SearchResult {

    /**
     * total position index (offset).
     */
    private int totalOffset;

    /**
     * node length.
     */
    private int length;

    /**
     * standard constructor.
     * 
     * @param offset
     *            total position index
     * @param len
     *            node length
     */
    public SearchResult(final int offset, final int len) {
        this.totalOffset = offset;
        this.length = len;
    }

    /**
     * gets total offset.
     * 
     * @return the totalOffset
     */
    public final int getTotalOffset() {
        return this.totalOffset;
    }

    /**
     * sets total offset.
     * 
     * @param offset
     *            the totalOffset to set
     */
    public final void setTotalOffset(final int offset) {
        this.totalOffset = offset;
    }

    /**
     * gets length.
     * 
     * @return the length
     */
    public final int getLength() {
        return this.length;
    }

    /**
     * sets length.
     * 
     * @param len
     *            the length to set
     */
    public final void setLength(final int len) {
        this.length = len;
    }

    /**
     * gets search result's string representation.
     * 
     * @return string representation
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Offset=");
        sb.append(this.totalOffset);
        sb.append(", Length=");
        sb.append(this.length);
        return sb.toString();
    }
}
