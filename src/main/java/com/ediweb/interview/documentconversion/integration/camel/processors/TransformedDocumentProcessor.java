package com.ediweb.interview.documentconversion.integration.camel.processors;

import com.ediweb.interview.documentconversion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.ediweb.interview.documentconversion.integration.camel.producers.OriginalDocumentEventEmitter;
import com.ediweb.interview.documentconversion.service.ProcessedDocumentService;
import com.ediweb.interview.documentconversion.service.dto.ProcessedDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
public class TransformedDocumentProcessor {
    private final ProcessedDocumentService processedDocumentService;

    private final OriginalDocumentEventEmitter originalDocumentEventEmitter;

    public TransformedDocumentProcessor(ProcessedDocumentService processedDocumentService, OriginalDocumentEventEmitter originalDocumentEventEmitter) {
        this.processedDocumentService = processedDocumentService;
        this.originalDocumentEventEmitter = originalDocumentEventEmitter;
    }

    @Handler
    public void storeTransformedDocument(Exchange exchange) {
        Long documentId = (Long) exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString());

        ProcessedDocumentDTO processedDocumentDTO = new ProcessedDocumentDTO();
        processedDocumentDTO.setOriginalDocumentId(documentId);
        processedDocumentDTO.setDocumentBody((String) exchange.getMessage().getBody());
        processedDocumentService.save(processedDocumentDTO);
        originalDocumentEventEmitter.emitEvent(processedDocumentDTO.getOriginalDocumentId(), OriginalDocumentEvent.TRANSFORMED_DOCUMENT_STORED);
    }
}
