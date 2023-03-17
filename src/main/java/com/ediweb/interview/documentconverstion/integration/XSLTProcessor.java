package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.service.DocumentService;
import com.ediweb.interview.documentconverstion.service.dto.DocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class XSLTProcessor {
    Logger logger = LoggerFactory.getLogger(XSLTProcessor.class);

    private final DocumentService documentService;

    XSLTProcessor(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Handler
    public void process(Exchange exchange) {
        logger.info(exchange.toString());

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setFileName(exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString());
        documentDTO.setInputXML(exchange.getProperty(CamelExchangeProperty.ORIGINAL_CONTENT.toString()).toString());
        documentDTO.setOutputXML(exchange.getMessage().getBody().toString());
        documentService.save(documentDTO);
    }
}
