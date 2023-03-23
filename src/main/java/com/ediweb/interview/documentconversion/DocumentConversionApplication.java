package com.ediweb.interview.documentconversion;

import com.ediweb.interview.documentconversion.config.misc.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
public class DocumentConversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentConversionApplication.class, args);
	}

}
