package com.ediweb.interview.documentconverstion.integration.camel.processors;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentLifeCycle;
import com.ediweb.interview.documentconverstion.integration.camel.routes.DocumentLifeCycleRoutes;
import com.ediweb.interview.documentconverstion.service.OriginalDocumentService;
import com.ediweb.interview.documentconverstion.service.dto.OriginalDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OriginalDocumentProcessor {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentProcessor.class);

    private final OriginalDocumentService originalDocumentService;

    private final FluentProducerTemplate fluentProducerTemplate;

    public OriginalDocumentProcessor(OriginalDocumentService originalDocumentService, FluentProducerTemplate fluentProducerTemplate) {
        this.originalDocumentService = originalDocumentService;
        this.fluentProducerTemplate = fluentProducerTemplate;
    }

    @Handler
    public void storeDocument(Exchange exchange) {
        String documentName = exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString();
        OriginalDocumentDTO originalDocumentDTO = new OriginalDocumentDTO();
        originalDocumentDTO.setFileName(documentName);
        originalDocumentDTO.setDocumentBody(exchange.getMessage().getBody().toString());
        originalDocumentDTO.setCurrentPhase(DocumentLifeCycle.DOCUMENT_STORAGE_SUCCESS);
        OriginalDocumentDTO originalDocument = originalDocumentService.save(originalDocumentDTO);
        logger.info("Document with name " + documentName + " was uploaded successfully.");

        try {
            exchange.setProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString(), originalDocument.getId());
            fluentProducerTemplate.start();
            fluentProducerTemplate
                    .withExchange(exchange)
                    .to(DocumentLifeCycle.DOCUMENT_TRANSFORMATION.getEndpoint().orElseThrow())
                    .send();
            fluentProducerTemplate.close();
        } catch (Exception e) {
            e.printStackTrace();
            fluentProducerTemplate.stop();
        }
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

    public static void sendPhaseUpdateMessage(FluentProducerTemplate fluentProducerTemplate, Exchange exchange, DocumentLifeCycle currentPhase) {
        try {
            exchange.setProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_PHASE.toString(), currentPhase);
            fluentProducerTemplate.start();
            fluentProducerTemplate.withExchange(exchange)
                    .to(DocumentLifeCycleRoutes.updateDocumentStatusEndpoint)
                    .send();
            fluentProducerTemplate.close();
        } catch (Exception e) {
            fluentProducerTemplate.stop();
            e.printStackTrace();
        }
    }
}
