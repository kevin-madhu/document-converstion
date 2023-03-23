package com.ediweb.interview.documentconversion.integration.camel.processors;

import com.ediweb.interview.documentconversion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconversion.domain.enumeration.DocumentLifeCycle;
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

    private final DocumentLifeCycleSignalProducer documentLifeCycleSignalProducer;

    public OriginalDocumentProcessor(OriginalDocumentService originalDocumentService, DocumentLifeCycleSignalProducer documentLifeCycleSignalProducer) {
        this.originalDocumentService = originalDocumentService;
        this.documentLifeCycleSignalProducer = documentLifeCycleSignalProducer;
    }

    @Handler
    public void storeDocumentLifeCycle(Exchange exchange) {
        OriginalDocumentDTO originalDocumentDTO = storeDocument(exchange);
        logger.info("Document with name " + originalDocumentDTO.getFileName() + " was uploaded successfully.");

        documentLifeCycleSignalProducer.sendTransformationSignal(originalDocumentDTO);
    }

    public OriginalDocumentDTO storeDocument(Exchange exchange) {
        String documentName = exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString();
        OriginalDocumentDTO originalDocumentDTO = new OriginalDocumentDTO();
        originalDocumentDTO.setFileName(documentName);
        originalDocumentDTO.setDocumentBody(exchange.getMessage().getBody().toString());
        originalDocumentDTO.setCurrentPhase(DocumentLifeCycle.DOCUMENT_STORAGE_SUCCESS);

        return originalDocumentService.save(originalDocumentDTO);
    }

    public boolean updateOriginalDocumentPhase(Exchange exchange) {
        try {
            Long documentId = (Long) (exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString()));
            DocumentLifeCycle documentPhase = DocumentLifeCycle.valueOf(exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_PHASE.toString()).toString());

            logger.info("Updating status of original document #" + documentId + " to " + documentPhase + ".");
            OriginalDocumentDTO originalDocumentDTO = originalDocumentService.setCurrentPhase(documentId, documentPhase);

            return(originalDocumentDTO.getCurrentPhase() == documentPhase);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
