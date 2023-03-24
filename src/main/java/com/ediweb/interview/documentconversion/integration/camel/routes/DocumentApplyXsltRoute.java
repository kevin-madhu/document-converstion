package com.ediweb.interview.documentconversion.integration.camel.routes;

import com.ediweb.interview.documentconversion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconversion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconversion.domain.enumeration.DocumentCamelRoute;
import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.ediweb.interview.documentconversion.integration.camel.processors.TransformedDocumentProcessor;
import com.ediweb.interview.documentconversion.integration.camel.producers.OriginalDocumentEventEmitter;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.xml.transform.TransformerException;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.xslt;

@Component
public class DocumentApplyXsltRoute extends RouteBuilder {
    private final ApplicationProperties applicationProperties;

    private final OriginalDocumentEventEmitter originalDocumentEventEmitter;

    public DocumentApplyXsltRoute(ApplicationProperties applicationProperties, OriginalDocumentEventEmitter originalDocumentEventEmitter) {
        this.applicationProperties = applicationProperties;
        this.originalDocumentEventEmitter = originalDocumentEventEmitter;
    }

    @Override
    public void configure() throws Exception {
        from(DocumentCamelRoute.DOCUMENT_TRANSFORMATION_XSLT.getEndpoint())
                .id(DocumentCamelRoute.DOCUMENT_TRANSFORMATION_XSLT.toString())
                .choice()
                    .when((exchangeProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString())))
                        .doTry()
                            .to(xslt(applicationProperties.getCamel().getXsltPath()))
                            .process(exchange -> {
                                Long documentId = (Long) exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString());
                                originalDocumentEventEmitter.emitEvent(documentId, OriginalDocumentEvent.DOCUMENT_TRANSFORMATION_SUCCESS);
                            })
                            .bean(TransformedDocumentProcessor.class)
                        .doCatch(TransformerException.class)
                            .process(exchange -> {
                                Long documentId = (Long) exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString());
                                originalDocumentEventEmitter.emitEvent(documentId, OriginalDocumentEvent.DOCUMENT_TRANSFORMATION_XSLT_ERROR);
                            })
                        .doCatch(Exception.class)
                            .process(exchange -> {
                                Long documentId = (Long) exchange.getProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString());
                                originalDocumentEventEmitter.emitEvent(documentId, OriginalDocumentEvent.DOCUMENT_TRANSFORMATION_ERROR);
                            })
                        .doFinally()
                            .stop()
                        .end()
                    .endChoice()
                    .otherwise()
                        .throwException(new IllegalArgumentException())
                    .endChoice()
                .end();
    }
}
