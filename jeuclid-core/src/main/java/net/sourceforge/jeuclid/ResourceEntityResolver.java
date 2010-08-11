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

package net.sourceforge.jeuclid;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.ThreadSafe;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Entity Resolver for standard MathML entities.
 * <p>
 * This class contains support for resolving all entities which are in the
 * default MathML namespaces. It currently has support for
 * <ul>
 * <li>MathML 1.0.1</li>
 * <li>OpenOffice MathML 1.0.1</li>
 * <li>MathML 2.0</li>
 * </ul>
 * 
 * @version $Revision$
 */
@ThreadSafe
public class ResourceEntityResolver implements EntityResolver {

    /**
     * The system ID for mathML.
     */
    public static final String MML2_SYSTEMID = "http://www.w3.org/TR/MathML2/dtd/mathml2.dtd";

    /**
     * The public ID for mathML.
     */
    public static final String MML2_PUBLICID = "-//W3C//DTD MathML 2.0//EN";

    private static final String MML1_SYSTEMID_PATH = "http://www.w3.org/Math/DTD/mathml1";

    private static final Map<String, String> PUBLIC_ID_TO_INTERNAL = new HashMap<String, String>();

    private static final Map<String, String> PUBLIC_ID_TO_SYSYEM = new HashMap<String, String>();

    /**
     * Default constructor.
     */
    public ResourceEntityResolver() {
        // Empty on purpose.
    }

    /** {@inheritDoc} */
    public InputSource resolveEntity(final String publicId,
            final String systemId) {
        InputSource retval = null;

        String mappedPath = ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .get(publicId);

        if ((mappedPath == null)
                && (systemId
                        .startsWith(ResourceEntityResolver.MML1_SYSTEMID_PATH))) {
            mappedPath = "/net/sourceforge/jeuclid/mathml.1.0.1"
                    + systemId
                            .substring(ResourceEntityResolver.MML1_SYSTEMID_PATH
                                    .length());
        }
        if (mappedPath != null) {
            retval = this.loadMappedResource(publicId, systemId, mappedPath);
        }
        return retval;
    }

    private InputSource loadMappedResource(final String publicId,
            final String systemId, final String mappedPath) {
        InputSource retval = null;
        final InputStream resourceStream = ResourceEntityResolver.class
                .getResourceAsStream(mappedPath);
        if (resourceStream != null) {
            retval = new InputSource(resourceStream);
            retval.setPublicId(publicId);
            String mappedSystemId = ResourceEntityResolver.PUBLIC_ID_TO_SYSYEM
                    .get(publicId);
            if (mappedSystemId == null) {
                mappedSystemId = systemId;
            }
            retval.setSystemId(mappedSystemId);
        }
        return retval;
    }

    static {
        // CHECKSTYLE:OFF
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//OpenOffice.org//DTD Modified W3C MathML 1.01//EN",
                "/net/sourceforge/jeuclid/openoffice.mathml.1.0.1/math.dtd");

        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                ResourceEntityResolver.MML2_PUBLICID,
                "/net/sourceforge/jeuclid/mathml.2.0/mathml2.dtd");

        ResourceEntityResolver.PUBLIC_ID_TO_SYSYEM.put(
                ResourceEntityResolver.MML2_PUBLICID,
                ResourceEntityResolver.MML2_SYSTEMID);

        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//DTD XHTML 1.1 plus MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/xhtml-math11-f.dtd");
        ResourceEntityResolver.PUBLIC_ID_TO_SYSYEM.put(
                "-//W3C//DTD XHTML 1.1 plus MathML 2.0//EN",
                "http://www.w3.org/Math/DTD/mathml2/xhtml-math11-f.dtd");

        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES MathML 2.0 Qualified Names 1.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/mathml2-qname-1.mod");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Arrow Relations for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isoamsa.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Binary Operators for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isoamsb.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Delimiters for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isoamsc.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Negated Relations for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isoamsn.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Ordinary for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isoamso.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Relations for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isoamsr.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Greek Symbols for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isogrk3.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Math Alphabets: Fraktur for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isomfrk.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Math Alphabets: Open Face for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isomopf.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Math Alphabets: Script for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isomscr.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES General Technical for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso9573-13/isotech.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Box and Line Drawing for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isobox.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Russian Cyrillic for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isocyr1.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Non-Russian Cyrillic for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isocyr2.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Diacritical Marks for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isodia.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Added Latin 1 for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isolat1.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Added Latin 2 for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isolat2.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Numeric and Special Graphic for MathML 2.0//EN",
                        "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isonum.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Publishing for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/iso8879/isopub.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Extra for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/mathml/mmlextra.ent");
        ResourceEntityResolver.PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Aliases for MathML 2.0//EN",
                "/net/sourceforge/jeuclid/mathml.2.0/mathml/mmlalias.ent");
        // CHECKSTYLE:ON

    }
}
