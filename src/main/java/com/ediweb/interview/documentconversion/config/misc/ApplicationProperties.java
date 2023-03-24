package com.ediweb.interview.documentconversion.config.misc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to the application.
 *
 * <p> Properties are configured in the application.yml file. </p>
 * <p> This class also load properties in the Spring Environment from the git.properties and META-INF/build-info.properties
 * files if they are found in the classpath.</p>
 */
@ConfigurationProperties(prefix = "document", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final Camel camel = new Camel();

    private final Kafka kafka = new Kafka();

    /**
     * <p>Getter for the field <code>camel</code>.</p>
     *
     * @return a {@link ApplicationProperties.Camel} object.
     */
    public Camel getCamel() {
        return camel;
    }

    public Kafka getKafka() {
        return kafka;
    }

    public static class Camel {

        private String watchFileDirectory = ApplicationDefaults.Camel.watchFileDirectory;

        private String xsltPath = ApplicationDefaults.Camel.xsltPath;

        public String getWatchFileDirectory() {
            return watchFileDirectory;
        }

        public void setWatchFileDirectory(String watchFileDirectory) {
            this.watchFileDirectory = watchFileDirectory;
        }

        public String getXsltPath() {
            return xsltPath;
        }

        public void setXsltPath(String xsltPath) {
            this.xsltPath = xsltPath;
        }
    }

    public static class Kafka {
        private String brokers = ApplicationDefaults.Kafka.brokers;

        private String originalDocumentEventsTopic = ApplicationDefaults.Kafka.originalDocumentEventsTopic;

        public String getBrokers() {
            return brokers;
        }

        public void setBrokers(String brokers) {
            this.brokers = brokers;
        }

        public String getOriginalDocumentEventsTopic() {
            return originalDocumentEventsTopic;
        }

        public void setOriginalDocumentEventsTopic(String originalDocumentEventsTopic) {
            this.originalDocumentEventsTopic = originalDocumentEventsTopic;
        }
    }
}
