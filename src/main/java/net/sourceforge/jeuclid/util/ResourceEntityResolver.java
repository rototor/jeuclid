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

/* $Id: ResourceEntityResolver.java,v 1.1.2.2 2007/02/05 08:54:28 maxberger Exp $ */

package net.sourceforge.jeuclid.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Entity Resolver that resources in class path.
 * 
 * @author Max Berger
 * @version $Revision: 1.1.2.2 $ $Date: 2007/02/05 08:54:28 $
 */
public class ResourceEntityResolver implements EntityResolver {

    private static final String MML1_SYSTEMID_PATH = "http://www.w3.org/Math/DTD/mathml1";

    private static final Map<String, String> PUBLIC_ID_TO_INTERNAL = new HashMap<String, String>();

    private static final Map<String, String> PUBLIC_ID_TO_SYSYEM = new HashMap<String, String>();

    /** {@inheritDoc} */
    public InputSource resolveEntity(String publicId, String systemId) {
        InputSource retval = null;

        String mappedPath = (String) PUBLIC_ID_TO_INTERNAL.get(publicId);

        if ((mappedPath == null) && (systemId.startsWith(MML1_SYSTEMID_PATH))) {
            mappedPath = "/mathml.1.0.1"
                    + systemId.substring(MML1_SYSTEMID_PATH.length());
        }
        if (mappedPath != null) {
            InputStream resourceStream = this.getClass().getResourceAsStream(
                    mappedPath);
            if (resourceStream != null) {
                retval = new InputSource(resourceStream);
                retval.setPublicId(publicId);
                String mappedSystemId = (String) PUBLIC_ID_TO_SYSYEM
                        .get(publicId);
                if (mappedSystemId == null)
                    mappedSystemId = systemId;
                retval.setSystemId(mappedSystemId);
            }
        }
        return retval;
    }

    {
        PUBLIC_ID_TO_INTERNAL.put(
                "-//OpenOffice.org//DTD Modified W3C MathML 1.01//EN",
                "/openoffice.mathml.1.0.1/math.dtd");

        PUBLIC_ID_TO_INTERNAL.put("-//W3C//DTD MathML 2.0//EN",
                "/mathml.2.0/mathml2.dtd");

        PUBLIC_ID_TO_SYSYEM.put("-//W3C//DTD MathML 2.0//EN",
                "http://www.w3.org/TR/MathML2/dtd/mathml2.dtd");

        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES MathML 2.0 Qualified Names 1.0//EN",
                "/mathml.2.0/mathml2-qname-1.mod");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Arrow Relations for MathML 2.0//EN",
                        "/mathml.2.0/iso9573-13/isoamsa.ent");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Binary Operators for MathML 2.0//EN",
                        "/mathml.2.0/iso9573-13/isoamsb.ent");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Delimiters for MathML 2.0//EN",
                        "/mathml.2.0/iso9573-13/isoamsc.ent");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Negated Relations for MathML 2.0//EN",
                        "/mathml.2.0/iso9573-13/isoamsn.ent");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Ordinary for MathML 2.0//EN",
                        "/mathml.2.0/iso9573-13/isoamso.ent");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Added Math Symbols: Relations for MathML 2.0//EN",
                        "/mathml.2.0/iso9573-13/isoamsr.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Greek Symbols for MathML 2.0//EN",
                "/mathml.2.0/iso9573-13/isogrk3.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Math Alphabets: Fraktur for MathML 2.0//EN",
                "/mathml.2.0/iso9573-13/isomfrk.ent");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Math Alphabets: Open Face for MathML 2.0//EN",
                        "/mathml.2.0/iso9573-13/isomopf.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Math Alphabets: Script for MathML 2.0//EN",
                "/mathml.2.0/iso9573-13/isomscr.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES General Technical for MathML 2.0//EN",
                "/mathml.2.0/iso9573-13/isotech.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Box and Line Drawing for MathML 2.0//EN",
                "/mathml.2.0/iso8879/isobox.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Russian Cyrillic for MathML 2.0//EN",
                "/mathml.2.0/iso8879/isocyr1.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Non-Russian Cyrillic for MathML 2.0//EN",
                "/mathml.2.0/iso8879/isocyr2.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Diacritical Marks for MathML 2.0//EN",
                "/mathml.2.0/iso8879/isodia.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Added Latin 1 for MathML 2.0//EN",
                "/mathml.2.0/iso8879/isolat1.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Added Latin 2 for MathML 2.0//EN",
                "/mathml.2.0/iso8879/isolat2.ent");
        PUBLIC_ID_TO_INTERNAL
                .put(
                        "-//W3C//ENTITIES Numeric and Special Graphic for MathML 2.0//EN",
                        "/mathml.2.0/iso8879/isonum.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Publishing for MathML 2.0//EN",
                "/mathml.2.0/iso8879/isopub.ent");
        PUBLIC_ID_TO_INTERNAL.put("-//W3C//ENTITIES Extra for MathML 2.0//EN",
                "/mathml.2.0/mathml/mmlextra.ent");
        PUBLIC_ID_TO_INTERNAL.put(
                "-//W3C//ENTITIES Aliases for MathML 2.0//EN",
                "/mathml.2.0/mathml/mmlalias.ent");

    }
}
