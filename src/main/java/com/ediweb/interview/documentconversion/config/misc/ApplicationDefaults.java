package com.ediweb.interview.documentconversion.config.misc;


/**
 * <p>ApplicationDefaults interface.</p>
 */
public interface ApplicationDefaults {
    interface Camel {

        String watchFileDirectory = "/opt/edi-documents/Z";
        String xsltPath = "integration/idoc2order.xsl";
    }

    interface Kafka {

        String brokers = "localhost:9092";
        String originalDocumentEventsTopic = "original-document-events";
    }
}
