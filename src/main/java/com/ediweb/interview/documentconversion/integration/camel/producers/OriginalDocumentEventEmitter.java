package com.ediweb.interview.documentconversion.integration.camel.producers;

import com.ediweb.interview.documentconversion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;

@Component
public class OriginalDocumentEventEmitter {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentEventEmitter.class);

    private final FluentProducerTemplate fluentProducerTemplate;

    private final ApplicationProperties applicationProperties;

    public OriginalDocumentEventEmitter(FluentProducerTemplate fluentProducerTemplate, ApplicationProperties applicationProperties) {
        this.fluentProducerTemplate = fluentProducerTemplate;
        this.applicationProperties = applicationProperties;
    }

    public void emitEvent(Long documentId, OriginalDocumentEvent event) {
        logger.info("Emitting event " + event + " for OriginalDocument #" + documentId + ".");

        Exchange exchange = this.createEventMessage(documentId, event);
        sendMessage(exchange);
    }

    private void sendMessage(Exchange exchange) {
        try {
            fluentProducerTemplate.start();
            fluentProducerTemplate
                    .withExchange(exchange)
                    .to(kafka(applicationProperties.getKafka().getOriginalDocumentEventsTopic()).brokers(applicationProperties.getKafka().getBrokers()))
                    .send();
            fluentProducerTemplate.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Exchange createEventMessage(Long documentId, OriginalDocumentEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();

        Exchange exchange = new DefaultExchange(fluentProducerTemplate.getCamelContext());
        OriginalDocumentEventDTO originalDocumentEventDTO = new OriginalDocumentEventDTO();
        originalDocumentEventDTO.setId(documentId);
        originalDocumentEventDTO.setEvent(event);
        try {
            exchange.getMessage().setBody(objectMapper.writeValueAsString(originalDocumentEventDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return exchange;
    }
}
