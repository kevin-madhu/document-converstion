package com.ediweb.interview.documentconversion.integration.camel.consumers;

import com.ediweb.interview.documentconversion.service.OriginalDocumentService;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OriginalDocumentPhaseUpdater {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentPhaseUpdater.class);

    private final OriginalDocumentService originalDocumentService;

    private final ObjectMapper objectMapper;

    public OriginalDocumentPhaseUpdater(OriginalDocumentService originalDocumentService, ObjectMapper objectMapper) {
        this.originalDocumentService = originalDocumentService;
        this.objectMapper = objectMapper;
    }


    @Handler
    public void updateOriginalDocumentPhase(Exchange exchange) {
        try {
            OriginalDocumentEventDTO originalDocumentEventDTO = objectMapper.readValue(exchange.getMessage().getBody().toString(), OriginalDocumentEventDTO.class);
            originalDocumentService.setCurrentPhase(originalDocumentEventDTO.getId(), originalDocumentEventDTO.getEvent());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
