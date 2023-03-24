package com.ediweb.interview.documentconversion.integration.camel.routes;

import com.ediweb.interview.documentconversion.domain.enumeration.DocumentCamelRoute;
import com.ediweb.interview.documentconversion.integration.camel.processors.OriginalDocumentProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StoreOriginalDocumentRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from(DocumentCamelRoute.STORE_DOCUMENT.getEndpoint())
                .id(DocumentCamelRoute.STORE_DOCUMENT.toString())
                .bean(OriginalDocumentProcessor.class);
    }
}
