package com.ediweb.interview.documentconverstion.integration.camel.processors;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentLifeCycle;
import com.ediweb.interview.documentconverstion.integration.camel.routes.DocumentLifeCycleRoutes;
import com.ediweb.interview.documentconverstion.service.dto.OriginalDocumentDTO;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.springframework.stereotype.Component;

@Component
public class DocumentLifeCycleSignalProducer {

    private final FluentProducerTemplate fluentProducerTemplate;

    public DocumentLifeCycleSignalProducer(FluentProducerTemplate fluentProducerTemplate) {
        this.fluentProducerTemplate = fluentProducerTemplate;
    }

    public void sendTransformationSignal(OriginalDocumentDTO originalDocumentDTO) {
        Exchange exchange = new DefaultExchange(fluentProducerTemplate.getCamelContext());
        exchange.getMessage().setBody(originalDocumentDTO);

        try {
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

    public void sendStoreTransformedDocumentSignal(ProcessedDocumentDTO processedDocumentDTO) {
        Exchange exchange = new DefaultExchange(fluentProducerTemplate.getCamelContext());
        exchange.getMessage().setBody(processedDocumentDTO);

        try {
            fluentProducerTemplate.start();
            fluentProducerTemplate
                    .withExchange(exchange)
                    .to(DocumentLifeCycle.TRANSFORMED_DOCUMENT_STORAGE.getEndpoint().orElseThrow())
                    .send();
            fluentProducerTemplate.stop();
        } catch (Exception e) {
            sendPhaseUpdateSignal(processedDocumentDTO.getOriginalDocumentId(), DocumentLifeCycle.DOCUMENT_TRANSFORMATION_ERROR);
            e.printStackTrace();
        }
    }

    public void sendPhaseUpdateSignal(Long originalDocumentId, DocumentLifeCycle currentPhase) {
        Exchange exchange = new DefaultExchange(fluentProducerTemplate.getCamelContext());
        exchange.setProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_ID.toString(), originalDocumentId);
        exchange.setProperty(CamelExchangeProperty.ORIGINAL_DOCUMENT_PHASE.toString(), currentPhase);

        try {
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