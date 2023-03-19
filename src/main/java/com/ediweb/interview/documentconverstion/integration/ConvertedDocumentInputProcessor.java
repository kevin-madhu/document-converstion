package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentProcessingStatus;
import com.ediweb.interview.documentconverstion.service.OriginalDocumentService;
import com.ediweb.interview.documentconverstion.service.ProcessedDocumentService;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConvertedDocumentInputProcessor {
    Logger logger = LoggerFactory.getLogger(ConvertedDocumentInputProcessor.class);

    private final ProcessedDocumentService processedDocumentService;

    private final OriginalDocumentService originalDocumentService;

    public ConvertedDocumentInputProcessor(ProcessedDocumentService processedDocumentService, OriginalDocumentService originalDocumentService) {
        this.processedDocumentService = processedDocumentService;
        this.originalDocumentService = originalDocumentService;
    }

    @Handler
    public void process(Exchange exchange) {
        String documentName = exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString();
        logger.info("Document with name" + documentName + " was uploaded.");

        ProcessedDocumentDTO processedDocumentDTO = new ProcessedDocumentDTO();
        processedDocumentDTO.setDocumentBody(exchange.getMessage().getBody().toString());
        processedDocumentDTO.setOriginalDocumentId((Long) (exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString())));
        processedDocumentService.save(processedDocumentDTO);

        originalDocumentService.setProcessingStatus(processedDocumentDTO.getOriginalDocumentId(), DocumentProcessingStatus.COMPLETED);
    }
}
