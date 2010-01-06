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

package net.sourceforge.jeuclid.biparser;


public class SearchResult {
    private int totalOffset;
    private int length;

    public SearchResult() {

    }

    public SearchResult(final int totalOffset, final int length) {
        this.totalOffset = totalOffset;
        this.length = length;
    }
    
    /**
     * @return the totalOffset
     */
    public int getTotalOffset() {
        return totalOffset;
    }

    /**
     * @param totalOffset the totalOffset to set
     */
    public void setTotalOffset(int totalOffset) {
        this.totalOffset = totalOffset;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }
}
