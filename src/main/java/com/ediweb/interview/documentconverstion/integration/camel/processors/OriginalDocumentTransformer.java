package com.ediweb.interview.documentconverstion.integration.camel.processors;

import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentLifeCycle;
import com.ediweb.interview.documentconverstion.service.dto.OriginalDocumentDTO;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
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
import java.util.Optional;

@Component
public class OriginalDocumentTransformer {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentTransformer.class);
    private final FluentProducerTemplate fluentProducerTemplate;

    private final DocumentLifeCycleSignalProducer documentLifeCycleSignalProducer;

    public OriginalDocumentTransformer(FluentProducerTemplate fluentProducerTemplate, DocumentLifeCycleSignalProducer documentLifeCycleSignalProducer) {
        this.fluentProducerTemplate = fluentProducerTemplate;
        this.documentLifeCycleSignalProducer = documentLifeCycleSignalProducer;
    }

    @Handler
    public void storeDocumentLifeCycle(Exchange exchange) {
        try {
            OriginalDocumentDTO originalDocumentDTO = (OriginalDocumentDTO) exchange.getMessage().getBody();
            Optional<ProcessedDocumentDTO> processedDocumentDTOOptional = transformDocument(originalDocumentDTO);

            if(processedDocumentDTOOptional.isPresent()) {
                ProcessedDocumentDTO processedDocumentDTO = processedDocumentDTOOptional.get();
                logger.info("Document with name " + originalDocumentDTO.getFileName() + " was transformed successfully.");
                documentLifeCycleSignalProducer.sendStoreTransformedDocumentSignal(processedDocumentDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<ProcessedDocumentDTO> transformDocument(OriginalDocumentDTO originalDocumentDTO) {
        try {
            fluentProducerTemplate.start();
            Exchange exchange = fluentProducerTemplate.withBody(originalDocumentDTO.getDocumentBody())
                    .toF(DocumentLifeCycle.DOCUMENT_TRANSFORMATION_XSLT.getEndpoint().orElseThrow())
                    .send();

            Exception transformationException = exchange.getException();
            if(transformationException == null) {
                String xmlDocumentContent = exchange.getMessage().getBody().toString();

                try {
                    Document document = parseXMLDocument(xmlDocumentContent);
                    xmlDocumentContent = modifyXMLDocument(originalDocumentDTO.getId(), document);
                    documentLifeCycleSignalProducer.sendPhaseUpdateSignal(originalDocumentDTO.getId(), DocumentLifeCycle.DOCUMENT_TRANSFORMATION_SUCCESS);

                    ProcessedDocumentDTO processedDocumentDTO = new ProcessedDocumentDTO();
                    processedDocumentDTO.setOriginalDocumentId(originalDocumentDTO.getId());
                    processedDocumentDTO.setDocumentBody(xmlDocumentContent);
                    return Optional.of(processedDocumentDTO);
                } catch (Exception e) {
                    documentLifeCycleSignalProducer.sendPhaseUpdateSignal(originalDocumentDTO.getId(), DocumentLifeCycle.DOCUMENT_TRANSFORMATION_XSLT_ERROR);
                    e.printStackTrace();
                }
            } else {
                throw transformationException;
            }
        } catch (Exception e) {
            documentLifeCycleSignalProducer.sendPhaseUpdateSignal(originalDocumentDTO.getId(), DocumentLifeCycle.DOCUMENT_TRANSFORMATION_ERROR);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private String modifyXMLDocument(Long originalDocumentId, Document document) throws TransformerException {
        String modifiedXMLDocumentContent = null;

        Element root = document.getDocumentElement();
        String originalDocumentURI = "/api/original-documents/" + originalDocumentId;

        root.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        Element parentDocument = document.createElement("Original-Document");
        parentDocument.setAttribute("xlink:type", "simple");
        parentDocument.setAttribute("xlink:href", originalDocumentURI);
        parentDocument.setAttribute("xlink:show", "new");
        root.appendChild(parentDocument);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));

        modifiedXMLDocumentContent = writer.getBuffer().toString();

        return modifiedXMLDocumentContent;
    }

    private Document parseXMLDocument(String xmlDocumentContent) throws ParserConfigurationException, SAXException, IOException {

        Document document = null;
        InputSource xmlDocumentSource = new InputSource( new StringReader(xmlDocumentContent));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        document = documentBuilder.parse(xmlDocumentSource);

        return document;
    }
}
