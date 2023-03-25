package com.ediweb.interview.documentconversion.integration.camel.routes;

import com.ediweb.interview.documentconversion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconversion.domain.enumeration.DocumentCamelRoute;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.file;

@Component
public class ReceiveOriginalDocumentFileRoute extends RouteBuilder {
    private final ApplicationProperties applicationProperties;

    public ReceiveOriginalDocumentFileRoute(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void configure() throws Exception {
        from(
                file(applicationProperties.getCamel().getWatchFileDirectory())
                        .delete(applicationProperties.getCamel().getRemoveFileAfterRead())
        )
                .id(DocumentCamelRoute.RECEIVE_DOCUMENT_FILE.toString())
                .convertBodyTo(String.class)
                .to(DocumentCamelRoute.STORE_DOCUMENT.getEndpoint());
    }
}
