package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.xml.transform.TransformerException;

@Component
public class DocumentWatcherRoute extends RouteBuilder {

    private final ApplicationProperties applicationProperties;

    public DocumentWatcherRoute(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void configure() throws Exception {
        final String ORIGINAL_DOCUMENT_CREATED_MESSAGE = "Original Document created with id ${exchange.getProperty("
                + CamelExchangeProperty.ORIGINAL_DOCUMENT_ID + ")}";
        final String XSLT_CONVERSION_SUCCESS_MESSAGE = "Converted document as per XSLT";
        final String XML_TRANSFORMER_EXCEPTION_MESSAGE = "Failed conversion: Conversion error occurred on file ${header.CamelFileName}.";
        final String XSLT_CONVERSION_FAILED_MESSAGE = "Exception: ${exception.message}.";


        from("file:" + applicationProperties.getCamel().getWatchFileDirectory() + "?delete=true")
                .convertBodyTo(String.class)
                .bean(OriginalDocumentInputProcessor.class)
                .log(LoggingLevel.INFO, ORIGINAL_DOCUMENT_CREATED_MESSAGE)
                .doTry()
                    .to("xslt:" + applicationProperties.getCamel().getXsltPath())
                    .log(LoggingLevel.INFO, XSLT_CONVERSION_SUCCESS_MESSAGE)
                .doCatch(TransformerException.class)
                    .log(LoggingLevel.ERROR, XML_TRANSFORMER_EXCEPTION_MESSAGE)
                    .bean(OriginalDocumentInputProcessor.class, "processingError")
                    .stop()
                .doCatch(Exception.class)
                    .log(LoggingLevel.ERROR, XSLT_CONVERSION_FAILED_MESSAGE)
                    .bean(OriginalDocumentInputProcessor.class, "processingError")
                .stop()
                .end()
                .bean(ConvertedDocumentInputProcessor.class);
    }
}
