package com.ediweb.interview.documentconverstion.integration.camel.processors;

import com.ediweb.interview.documentconverstion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentLifeCycle;
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

@Component
public class OriginalDocumentTransformer {
    Logger logger = LoggerFactory.getLogger(OriginalDocumentTransformer.class);
    private final ApplicationProperties applicationProperties;
    private final FluentProducerTemplate fluentProducerTemplate;

    public OriginalDocumentTransformer(ApplicationProperties applicationProperties, FluentProducerTemplate fluentProducerTemplate) {
        this.applicationProperties = applicationProperties;
        this.fluentProducerTemplate = fluentProducerTemplate;
    }

    @Handler
    public void transformDocument(Exchange exchange) {
        exchange = fluentProducerTemplate.withExchange(exchange).toF("xslt:%s", applicationProperties.getCamel().getXsltPath()).send();
        String xmlDocumentContent = exchange.getMessage().getBody().toString();
        Long originalDocumentId = (Long) (exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString()));
        xmlDocumentContent = modifyXMLDocument(xmlDocumentContent, originalDocumentId);
        exchange.getMessage().setBody(xmlDocumentContent);
        fluentProducerTemplate.withExchange(exchange).to(DocumentLifeCycle.TRANSFORMED_DOCUMENT_STORAGE.getEndpoint().orElseThrow()).send();
        fluentProducerTemplate.stop();
    }

    private String modifyXMLDocument(String xmlDocumentContent, Long originalDocumentId) {
        String modifiedXMLDocumentContent = null;

        try {
            Document document = parseXMLDocument(xmlDocumentContent);
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
