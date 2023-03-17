package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class XSLTProcessor {
    Logger logger = LoggerFactory.getLogger(XSLTProcessor.class);

    XSLTProcessor() {}

    @Handler
    public void process(Exchange exchange) {
        logger.info(exchange.getProperty(CamelExchangeProperty.ORIGINAL_CONTENT.toString()).toString());
        logger.info(exchange.getMessage().getBody().toString());
        logger.info(exchange.getMessage().getHeader(CamelExchangeProperty.CamelFileName.toString()).toString());
    }
}
