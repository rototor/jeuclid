package net.sourceforge.jeuclid.elements.support;

import java.util.Collections;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * Chainable implementation of a {@link NamespaceContext}.
 *
 * @version $Revision$
 */
public class NamespaceContextAdder implements NamespaceContext {

    private final String namespacePrefix;
    private final String namespaceURI;
    private final NamespaceContext delegateContext;

    /**
     * Create a new NamespaceAdder. The delegate context will be called if the
     * namespace to be checked is not the namespace given in this adder. If the
     * delegate is null, default values will be returned.
     *
     * @param ns
     *            namespace prefix.
     * @param nsuri
     *            namespace URI
     * @param delegate
     *            delegate {@link NamespaceContext}
     */
    public NamespaceContextAdder(@Nonnull final String ns,
            @Nonnull final String nsuri,
            @Nullable final NamespaceContext delegate) {
        this.namespacePrefix = ns;
        this.namespaceURI = nsuri;
        this.delegateContext = delegate;
    }

    /** {@inheritDoc} */
    public String getNamespaceURI(final String prefix) {
        String retVal;
        if (this.namespacePrefix.equals(prefix)) {
            retVal = this.namespaceURI;
        } else if (this.delegateContext == null) {
            retVal = XMLConstants.NULL_NS_URI;
        } else {
            retVal = this.delegateContext.getNamespaceURI(prefix);
        }
        return retVal;
    }

    /** {@inheritDoc} */
    public String getPrefix(final String uri) {
        String retVal;
        if (this.namespaceURI.equals(uri)) {
            retVal = this.namespacePrefix;
        } else if (this.delegateContext == null) {
            retVal = "";
        } else {
            retVal = this.delegateContext.getPrefix(uri);
        }
        return retVal;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Iterator<String> getPrefixes(final String uri) {
        Iterator<String> retVal;
        if (this.namespaceURI.equals(uri)) {
            retVal = Collections.singleton(this.namespacePrefix).iterator();
        } else if (this.delegateContext == null) {
            retVal = Collections.EMPTY_LIST.iterator();
        } else {
            retVal = this.delegateContext.getPrefixes(uri);
        }
        return retVal;
    }

}
