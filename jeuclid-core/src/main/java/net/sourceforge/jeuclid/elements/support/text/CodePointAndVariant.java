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

/* $Id: CodePointAndVariant.java $ */

package net.sourceforge.jeuclid.elements.support.text;

import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;

/**
 * @version $Revision: 000 $
 */
public class CodePointAndVariant {
    private final int codePoint;

    private final MathVariant variant;

    public CodePointAndVariant(final int icodePoint,
            final MathVariant ivariant) {
        this.codePoint = icodePoint;
        this.variant = ivariant;
    }

    /**
     * @return the codePoint
     */
    public final int getCodePoint() {
        return this.codePoint;
    }

    /**
     * @return the variant
     */
    public final MathVariant getVariant() {
        return this.variant;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.codePoint;
        result = prime * result
                + ((this.variant == null) ? 0 : this.variant.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CodePointAndVariant other = (CodePointAndVariant) obj;
        if (this.codePoint != other.codePoint) {
            return false;
        }
        if (this.variant == null) {
            if (other.variant != null) {
                return false;
            }
        } else if (!this.variant.equals(other.variant)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append('[');
        b.append("0x");
        b.append(Integer.toHexString(this.codePoint));
        b.append(' ');
        b.append(this.variant.toString());
        b.append(']');
        return b.toString();
    }
}
