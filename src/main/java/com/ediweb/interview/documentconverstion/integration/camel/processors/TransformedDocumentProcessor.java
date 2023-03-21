package com.ediweb.interview.documentconverstion.integration.camel.processors;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentLifeCycle;
import com.ediweb.interview.documentconverstion.service.ProcessedDocumentService;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransformedDocumentProcessor {
    Logger logger = LoggerFactory.getLogger(TransformedDocumentProcessor.class);

    private final ProcessedDocumentService processedDocumentService;

    private final FluentProducerTemplate fluentProducerTemplate;

    public TransformedDocumentProcessor(ProcessedDocumentService processedDocumentService, FluentProducerTemplate fluentProducerTemplate) {
        this.processedDocumentService = processedDocumentService;
        this.fluentProducerTemplate = fluentProducerTemplate;
    }

    @Handler
    public void storeTransformedDocument(Exchange exchange) {
        String documentName = exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString();
        Long originalDocumentId = (Long) (exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString()));

        ProcessedDocumentDTO processedDocumentDTO = new ProcessedDocumentDTO();
        processedDocumentDTO.setDocumentBody(exchange.getMessage().getBody().toString());
        processedDocumentDTO.setOriginalDocumentId(originalDocumentId);
        processedDocumentService.save(processedDocumentDTO);
        logger.info("Transformation of Document with name " + documentName + " was uploaded successfully.");
        OriginalDocumentProcessor.sendPhaseUpdateMessage(fluentProducerTemplate, exchange, DocumentLifeCycle.TRANSFORMED_DOCUMENT_STORED);
    }
}
