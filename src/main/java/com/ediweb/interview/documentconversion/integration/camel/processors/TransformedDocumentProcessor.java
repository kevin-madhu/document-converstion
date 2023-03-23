package com.ediweb.interview.documentconversion.integration.camel.processors;

import com.ediweb.interview.documentconversion.domain.enumeration.DocumentLifeCycle;
import com.ediweb.interview.documentconversion.service.ProcessedDocumentService;
import com.ediweb.interview.documentconversion.service.dto.ProcessedDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransformedDocumentProcessor {
    Logger logger = LoggerFactory.getLogger(TransformedDocumentProcessor.class);

    private final ProcessedDocumentService processedDocumentService;

    private final DocumentLifeCycleSignalProducer documentLifeCycleSignalProducer;

    public TransformedDocumentProcessor(ProcessedDocumentService processedDocumentService, DocumentLifeCycleSignalProducer documentLifeCycleSignalProducer) {
        this.processedDocumentService = processedDocumentService;
        this.documentLifeCycleSignalProducer = documentLifeCycleSignalProducer;
    }

    @Handler
    public void storeTransformedDocument(Exchange exchange) {
        ProcessedDocumentDTO processedDocumentDTO = (ProcessedDocumentDTO) exchange.getMessage().getBody();
        processedDocumentService.save(processedDocumentDTO);
        logger.info("Transformation of Original Document #" + processedDocumentDTO.getOriginalDocumentId() + " was uploaded successfully.");
        documentLifeCycleSignalProducer.sendPhaseUpdateSignal(processedDocumentDTO.getOriginalDocumentId(), DocumentLifeCycle.TRANSFORMED_DOCUMENT_STORED);
    }
}
