package com.ediweb.interview.documentconverstion.integration.camel.routes;

import com.ediweb.interview.documentconverstion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentLifeCycle;
import com.ediweb.interview.documentconverstion.integration.camel.processors.OriginalDocumentProcessor;
import com.ediweb.interview.documentconverstion.integration.camel.processors.OriginalDocumentTransformer;
import com.ediweb.interview.documentconverstion.integration.camel.processors.TransformedDocumentProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DocumentLifeCycleRoutes extends RouteBuilder {
    private final ApplicationProperties applicationProperties;

    public static final String updateDocumentStatusEndpoint = "direct:updateDocumentStatus";

    public DocumentLifeCycleRoutes(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void configure() throws Exception {
        final String documentStorageEndpoint = DocumentLifeCycle.DOCUMENT_STORAGE.getEndpoint().orElseThrow();
        final String documentTransformationEndpoint = DocumentLifeCycle.DOCUMENT_TRANSFORMATION.getEndpoint().orElseThrow();
        final String transformedDocumentStorageEndpoint = DocumentLifeCycle.TRANSFORMED_DOCUMENT_STORAGE.getEndpoint().orElseThrow();

        from("file:" + applicationProperties.getCamel().getWatchFileDirectory() + "?delete=true")
                .id(DocumentLifeCycle.DOCUMENT_COLLECTION.toString())
                .convertBodyTo(String.class)
                .to(documentStorageEndpoint);

        from(documentStorageEndpoint)
                .id(DocumentLifeCycle.DOCUMENT_STORAGE.toString())
                .bean(OriginalDocumentProcessor.class);

        from(documentTransformationEndpoint)
                .id(DocumentLifeCycle.DOCUMENT_TRANSFORMATION.toString())
                .bean(OriginalDocumentTransformer.class);

        from(transformedDocumentStorageEndpoint)
                .id(DocumentLifeCycle.TRANSFORMED_DOCUMENT_STORAGE.toString())
                .bean(TransformedDocumentProcessor.class);

        from(updateDocumentStatusEndpoint)
                .bean(OriginalDocumentProcessor.class, "updateOriginalDocumentPhase");
    }
}
