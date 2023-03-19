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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

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
        Long originalDocumentId = (Long) (exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString()));
        String xmlDocumentContent = exchange.getMessage().getBody().toString();

        logger.info("Document with name " + documentName + " was uploaded.");

        ProcessedDocumentDTO processedDocumentDTO = new ProcessedDocumentDTO();
        processedDocumentDTO.setDocumentBody(modifyXMLDocument(xmlDocumentContent, originalDocumentId));
        processedDocumentDTO.setOriginalDocumentId(originalDocumentId);
        processedDocumentService.save(processedDocumentDTO);

        originalDocumentService.setProcessingStatus(processedDocumentDTO.getOriginalDocumentId(), DocumentProcessingStatus.COMPLETED);
    }

    private String modifyXMLDocument(String xmlDocumentContent, Long originalDocumentId) {
        String modifiedXMLDocumentContent = null;

        try {
            Document document = parseXMLDocument(xmlDocumentContent);
            Element root = document.getDocumentElement();
            String originalDocumentURI = "/api/original-documents/" + originalDocumentId;
            root.setAttribute("href", originalDocumentURI);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            modifiedXMLDocumentContent = writer.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return modifiedXMLDocumentContent;
    }

    private Document parseXMLDocument(String xmlDocumentContent) {

        Document document = null;
        try {
            InputSource xmlDocumentSource = new InputSource( new StringReader(xmlDocumentContent));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.parse(xmlDocumentSource);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return document;
    }
}
