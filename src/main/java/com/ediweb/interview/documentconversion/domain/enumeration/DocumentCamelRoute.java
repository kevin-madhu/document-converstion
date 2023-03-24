package com.ediweb.interview.documentconversion.domain.enumeration;

public enum DocumentCamelRoute {
    RECEIVE_DOCUMENT_FILE {
        @Override
        public String getEndpoint() { return "file:"; }
    },
    STORE_DOCUMENT {
        @Override
        public String getEndpoint() { return "direct:storeOriginalDocument"; }
    },
    UPDATE_DOCUMENT_PHASE {
        @Override
        public String getEndpoint() { return "direct:updateDocumentPhase"; }
    },
    DOCUMENT_TRANSFORMATION_XSLT {
        @Override
        public String getEndpoint() {  return "direct:transformUsingXSLT"; }
    };

    abstract public String getEndpoint();

    public String getEndpoint(String postfix) {
        return this.getEndpoint() + postfix;
    }
}
