package com.ediweb.interview.documentconverstion;

import com.ediweb.interview.documentconverstion.config.misc.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
public class DocumentConverstionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentConverstionApplication.class, args);
	}

}
