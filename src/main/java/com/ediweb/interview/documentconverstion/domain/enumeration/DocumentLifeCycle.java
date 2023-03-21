package com.ediweb.interview.documentconverstion.domain.enumeration;

import java.util.Optional;

public enum DocumentLifeCycle {
    DOCUMENT_COLLECTION {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.empty();
        }
    },
    DOCUMENT_STORAGE {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.of("direct:storeOriginalDocument");
        }
    },
    DOCUMENT_STORAGE_SUCCESS {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.empty();
        }
    },
    DOCUMENT_TRANSFORMATION {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.of("direct:transformOriginalDocument");
        }
    },
    DOCUMENT_TRANSFORMATION_XSLT {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.of("direct:transformUsingXSLT");
        }
    },
    DOCUMENT_TRANSFORMATION_XSLT_ERROR {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.empty();
        }
    },
    DOCUMENT_TRANSFORMATION_ERROR {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.empty();
        }
    },
    DOCUMENT_TRANSFORMATION_SUCCESS {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.empty();
        }
    },
    TRANSFORMED_DOCUMENT_STORAGE {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.of("direct:storeTransformedDocument");
        }
    },
    TRANSFORMED_DOCUMENT_STORED {
        @Override
        public Optional<String> getEndpoint() {
            return Optional.empty();
        }
    };

    abstract public Optional<String> getEndpoint();
}
