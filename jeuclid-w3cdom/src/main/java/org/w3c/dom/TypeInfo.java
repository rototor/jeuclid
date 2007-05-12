package org.w3c.dom;

public interface TypeInfo {
    String getTypeName();

    String getTypeNamespace();

    // DerivationMethods
    int DERIVATION_RESTRICTION    = 0x00000001;
    int DERIVATION_EXTENSION      = 0x00000002;
    int DERIVATION_UNION          = 0x00000004;
    int DERIVATION_LIST           = 0x00000008;

    boolean isDerivedFrom(String typeNamespaceArg, 
                                 String typeNameArg, 
                                 int derivationMethod);

}
