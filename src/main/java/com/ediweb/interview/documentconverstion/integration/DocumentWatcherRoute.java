package com.ediweb.interview.documentconverstion.integration;

import com.ediweb.interview.documentconverstion.domain.enumeration.CamelExchangeProperty;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DocumentWatcherRoute extends RouteBuilder {

    private final String PATH = "/home/kevin/z";

    private final String XSLT_PATH = "integration/idoc2order.xsl";

    @Override
    public void configure() throws Exception {
        from("file:"+ PATH + "?delete=true")
                .convertBodyTo(String.class)
                .setProperty(CamelExchangeProperty.ORIGINAL_CONTENT.toString(), body())
                .to("xslt:" + XSLT_PATH)
                .to("bean:XSLTProcessor");
    }
}
