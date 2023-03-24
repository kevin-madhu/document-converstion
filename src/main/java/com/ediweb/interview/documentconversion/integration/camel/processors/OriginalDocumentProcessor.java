package com.ediweb.interview.documentconversion.integration.camel.processors;

import com.ediweb.interview.documentconversion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.ediweb.interview.documentconversion.integration.camel.producers.OriginalDocumentEventEmitter;
import com.ediweb.interview.documentconversion.service.OriginalDocumentService;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OriginalDocumentProcessor {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentProcessor.class);

    private final OriginalDocumentService originalDocumentService;

    private final OriginalDocumentEventEmitter originalDocumentEventEmitter;

    public OriginalDocumentProcessor(OriginalDocumentService originalDocumentService, OriginalDocumentEventEmitter originalDocumentEventEmitter) {
        this.originalDocumentService = originalDocumentService;
        this.originalDocumentEventEmitter = originalDocumentEventEmitter;
    }

    @Handler
    public void storeDocument(Exchange exchange) {
        String documentName = exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString();
        OriginalDocumentDTO originalDocumentDTO = new OriginalDocumentDTO();
        originalDocumentDTO.setFileName(documentName);
        originalDocumentDTO.setDocumentBody(exchange.getMessage().getBody().toString());
        originalDocumentDTO.setCurrentPhase(OriginalDocumentEvent.NONE);
        originalDocumentDTO = originalDocumentService.save(originalDocumentDTO);

        originalDocumentEventEmitter.emitEvent(originalDocumentDTO.getId(), OriginalDocumentEvent.DOCUMENT_STORAGE_SUCCESS);
    }
}
