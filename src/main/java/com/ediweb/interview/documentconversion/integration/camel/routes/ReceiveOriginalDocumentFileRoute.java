package com.ediweb.interview.documentconversion.integration.camel.routes;

import com.ediweb.interview.documentconversion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconversion.domain.enumeration.DocumentCamelRoute;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ReceiveOriginalDocumentFileRoute extends RouteBuilder {
    private final ApplicationProperties applicationProperties;

    public ReceiveOriginalDocumentFileRoute(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void configure() throws Exception {
        from(DocumentCamelRoute.RECEIVE_DOCUMENT_FILE.getEndpoint(applicationProperties.getCamel().getWatchFileDirectory() + "?delete=true"))
                .id(DocumentCamelRoute.RECEIVE_DOCUMENT_FILE.getEndpoint())
                .convertBodyTo(String.class)
                .to(DocumentCamelRoute.STORE_DOCUMENT.getEndpoint());
    }
}
