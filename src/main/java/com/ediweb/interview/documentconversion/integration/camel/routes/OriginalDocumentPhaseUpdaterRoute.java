package com.ediweb.interview.documentconversion.integration.camel.routes;

import com.ediweb.interview.documentconversion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconversion.integration.camel.consumers.OriginalDocumentPhaseUpdater;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;

@Component
public class OriginalDocumentPhaseUpdaterRoute extends RouteBuilder {

    private final ApplicationProperties applicationProperties;

    public OriginalDocumentPhaseUpdaterRoute(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void configure() throws Exception {

        from(kafka(applicationProperties.getKafka().getOriginalDocumentEventsTopic()).brokers(applicationProperties.getKafka().getBrokers()))
                .bean(OriginalDocumentPhaseUpdater.class);
    }
}
