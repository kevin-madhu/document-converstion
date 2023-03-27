package com.ediweb.interview.documentconversion.integration.camel.consumers;

import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.ediweb.interview.documentconversion.integration.camel.producers.OriginalDocumentEventEmitter;
import com.ediweb.interview.documentconversion.integration.camel.util.XMLDocument;
import com.ediweb.interview.documentconversion.service.OriginalDocumentService;
import com.ediweb.interview.documentconversion.service.ProcessedDocumentService;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentDTO;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentEventDTO;
import com.ediweb.interview.documentconversion.service.dto.ProcessedDocumentDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class OriginalDocumentLinkGenerator {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentLinkGenerator.class);

    private final OriginalDocumentService originalDocumentService;

    private final OriginalDocumentEventEmitter originalDocumentEventEmitter;

    private final ProcessedDocumentService processedDocumentService;

    private final ObjectMapper objectMapper;


    public OriginalDocumentLinkGenerator(OriginalDocumentService originalDocumentService, OriginalDocumentEventEmitter originalDocumentEventEmitter,
                                         ProcessedDocumentService processedDocumentService, ObjectMapper objectMapper) {
        this.originalDocumentService = originalDocumentService;
        this.originalDocumentEventEmitter = originalDocumentEventEmitter;
        this.processedDocumentService = processedDocumentService;
        this.objectMapper = objectMapper;
    }

    @Handler
    public void generateAndUpdateOriginalDocumentLink(Exchange exchange) {
        try {
            OriginalDocumentEventDTO originalDocumentEventDTO = objectMapper.readValue(exchange.getMessage().getBody().toString(), OriginalDocumentEventDTO.class);

            try {
                if( originalDocumentEventDTO.getEvent() == OriginalDocumentEvent.TRANSFORMED_DOCUMENT_STORED) {
                    OriginalDocumentDTO originalDocumentDTO = originalDocumentService.findOne(originalDocumentEventDTO.getId()).orElseThrow(IllegalArgumentException::new);

                    Document document = XMLDocument.toDocument(originalDocumentDTO.getDocumentBody());
                    generateAndAppendLinkToDocument(document, originalDocumentDTO.getId());
                    String modifiedContent = XMLDocument.toString(document);
                    ProcessedDocumentDTO processedDocumentDTO = processedDocumentService.findByOriginalDocumentId(originalDocumentDTO.getId())
                            .orElseThrow(IllegalArgumentException::new);
                    processedDocumentService.updateDocumentBody(processedDocumentDTO.getId(), modifiedContent);
                    originalDocumentEventEmitter.emitEvent(originalDocumentDTO.getId(), OriginalDocumentEvent.DOCUMENT_LINKED);
                }
            } catch (IOException | ParserConfigurationException | TransformerException | SAXException e){
                originalDocumentEventEmitter.emitEvent(originalDocumentEventDTO.getId(), OriginalDocumentEvent.DOCUMENT_PARSE_ERROR);
                throw new RuntimeException(e);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private void generateAndAppendLinkToDocument(Document document, Long originalDocumentId) throws TransformerException {
        Element root = document.getDocumentElement();
        String originalDocumentURI = "/api/original-documents/" + originalDocumentId;

        root.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        Element parentDocument = document.createElement("Original-Document");
        parentDocument.setAttribute("xlink:type", "simple");
        parentDocument.setAttribute("xlink:href", originalDocumentURI);
        parentDocument.setAttribute("xlink:show", "new");
        root.appendChild(parentDocument);
    }
}
