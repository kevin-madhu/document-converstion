package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentProcessingStatus;
import com.ediweb.interview.documentconverstion.service.OriginalDocumentService;
import com.ediweb.interview.documentconverstion.service.dto.OriginalDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OriginalDocumentInputProcessor {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentInputProcessor.class);

    private final OriginalDocumentService originalDocumentService;

    public OriginalDocumentInputProcessor(OriginalDocumentService originalDocumentService) {
        this.originalDocumentService = originalDocumentService;
    }

    @Handler
    public void process(Exchange exchange) {
        String documentName = exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString();
        logger.info("Document with name" + documentName + " was uploaded.");

        OriginalDocumentDTO originalDocumentDTO = new OriginalDocumentDTO();
        originalDocumentDTO.setFileName(documentName);
        originalDocumentDTO.setDocumentBody(exchange.getMessage().getBody().toString());
        originalDocumentDTO.setProcessingStatus(DocumentProcessingStatus.PENDING);
        OriginalDocumentDTO originalDocument = originalDocumentService.save(originalDocumentDTO);

        exchange.setProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString(), originalDocument.getId());
    }
}
