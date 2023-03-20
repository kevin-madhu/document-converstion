package com.ediweb.interview.documentconverstion.integration.camel.processors;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentLifeCycle;
import com.ediweb.interview.documentconverstion.service.OriginalDocumentService;
import com.ediweb.interview.documentconverstion.service.ProcessedDocumentService;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransformedDocumentProcessor {
    Logger logger = LoggerFactory.getLogger(TransformedDocumentProcessor.class);

    private final ProcessedDocumentService processedDocumentService;

    private final OriginalDocumentService originalDocumentService;

    public TransformedDocumentProcessor(ProcessedDocumentService processedDocumentService, OriginalDocumentService originalDocumentService) {
        this.processedDocumentService = processedDocumentService;
        this.originalDocumentService = originalDocumentService;
    }

    @Handler
    public void storeTransformedDocument(Exchange exchange) {
        String documentName = exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString();
        Long originalDocumentId = (Long) (exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString()));

        logger.info("Document with name " + documentName + " was uploaded.");

        ProcessedDocumentDTO processedDocumentDTO = new ProcessedDocumentDTO();
        processedDocumentDTO.setDocumentBody(exchange.getMessage().getBody().toString());
        processedDocumentDTO.setOriginalDocumentId(originalDocumentId);
        processedDocumentService.save(processedDocumentDTO);

        originalDocumentService.setCurrentPhase(processedDocumentDTO.getOriginalDocumentId(), DocumentLifeCycle.TRANSFORMED_DOCUMENT_STORED);
    }
}
