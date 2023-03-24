package com.ediweb.interview.documentconversion.integration.camel.consumers;

import com.ediweb.interview.documentconversion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconversion.domain.enumeration.DocumentCamelRoute;
import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.ediweb.interview.documentconversion.service.OriginalDocumentService;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentDTO;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Handler;
import org.apache.camel.support.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OriginalDocumentTransformer {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentTransformer.class);

    private final FluentProducerTemplate fluentProducerTemplate;

    private final OriginalDocumentService originalDocumentService;

    private final ObjectMapper objectMapper;

    public OriginalDocumentTransformer(FluentProducerTemplate fluentProducerTemplate,
                                       OriginalDocumentService originalDocumentService, ObjectMapper objectMapper) {
        this.fluentProducerTemplate = fluentProducerTemplate;
        this.originalDocumentService = originalDocumentService;
        this.objectMapper = objectMapper;
    }

    @Handler
    public void transformOriginalDocument(Exchange exchange) {
        try {
            OriginalDocumentEventDTO originalDocumentEventDTO = objectMapper.readValue(exchange.getMessage().getBody().toString(), OriginalDocumentEventDTO.class);
            if(originalDocumentEventDTO.getEvent() == OriginalDocumentEvent.DOCUMENT_STORAGE_SUCCESS) {
                OriginalDocumentDTO originalDocumentDTO = originalDocumentService.findOne(originalDocumentEventDTO.getId()).orElseThrow(IllegalArgumentException::new);

                Exchange xsltExchange = new DefaultExchange(fluentProducerTemplate.getCamelContext());
                xsltExchange.setProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString(), originalDocumentDTO.getId());
                xsltExchange.getMessage().setBody(originalDocumentDTO.getDocumentBody());

                fluentProducerTemplate.start();
                fluentProducerTemplate
                        .withExchange(xsltExchange)
                        .toF(DocumentCamelRoute.DOCUMENT_TRANSFORMATION_XSLT.getEndpoint())
                        .send();
                fluentProducerTemplate.stop();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
