package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
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

        from("file:" + applicationProperties.getCamel().getWatchFileDirectory() + "?delete=true")
                .convertBodyTo(String.class)
                .bean(OriginalDocumentInputProcessor.class)
                .log("log: Original Document created with id ${body}")
                .doTry()
                    .to("xslt:" + applicationProperties.getCamel().getXsltPath())
                    .log("log: Converted document as per XSLT")
                .doCatch(TransformerException.class)
                    .log("Failed conversion: Conversion error occurred on file ${header.CamelFileName}. Removing.")
                    .bean(OriginalDocumentInputProcessor.class, "processingError")
                    .stop()
                .doCatch(Exception.class)
                    .log("Exception: ${exception.message}. Removing.")
                    .bean(OriginalDocumentInputProcessor.class, "processingError")
                .stop()
                .end()
                .bean(ConvertedDocumentInputProcessor.class);
    }
}
