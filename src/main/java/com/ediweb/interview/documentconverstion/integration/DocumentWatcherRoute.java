package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

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
                .setProperty(CamelExchangeProperty.ORIGINAL_CONTENT.toString(), body())
                .to("xslt:" + applicationProperties.getCamel().getXsltPath())
                .to("bean:XSLTProcessor");
    }
}
