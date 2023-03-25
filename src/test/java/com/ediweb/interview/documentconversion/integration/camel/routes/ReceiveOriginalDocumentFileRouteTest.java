package com.ediweb.interview.documentconversion.integration.camel.routes;

import com.ediweb.interview.documentconversion.config.misc.ApplicationProperties;
import com.ediweb.interview.documentconversion.domain.enumeration.DocumentCamelRoute;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.apache.camel.builder.AdviceWith.adviceWith;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class ReceiveOriginalDocumentFileRouteTest extends CamelTestSupport {
    @Spy
    ApplicationProperties applicationProperties = new ApplicationProperties();

    Path testDocumentsPath = FileSystems.getDefault().getPath("src", "test", "resources", "documents");

    Path testWatchFilePath = testDocumentsPath.resolve("z");

    private final String mockReceiveEndpoint = "direct:receiveDocumentMock";

    private final String mockStoreEndpoint = "mock:storeDocument";

    @EndpointInject(value = mockStoreEndpoint)
    private MockEndpoint storeDocumentMock;

    @InjectMocks
    public ReceiveOriginalDocumentFileRoute receiveOriginalDocumentFileRoute;

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        applicationProperties.getCamel().setWatchFileDirectory(testWatchFilePath.toFile().getAbsolutePath());
        return receiveOriginalDocumentFileRoute;
    }

    @Test
    public void testReceiveOriginalDocumentFileRoute_WhenFileIsUploaded_ShouldReceiveMessage() throws Exception {
        //Arrange
        RouteDefinition route = context.getRouteDefinition(DocumentCamelRoute.RECEIVE_DOCUMENT_FILE.toString());
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToUri("direct:.*")
                        .replace()
                        .to(storeDocumentMock);
            }
        });
        context.start();

        //Act
        File testFile = File.createTempFile("test-file", ".xml", testWatchFilePath.toFile());

        //Assert
        storeDocumentMock.expectedMessageCount(1);
        storeDocumentMock.assertIsSatisfied();

        //Cleanup
        testFile.delete();
    }

    @Test
    public void testReceiveOriginalDocumentFileRoute_WhenFileIsUploaded_ShouldRemoveFileByDefaultAfterReading() throws Exception {
        //Arrange
        RouteDefinition route = context.getRouteDefinition(DocumentCamelRoute.RECEIVE_DOCUMENT_FILE.toString());
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToUri("direct:.*")
                        .replace()
                        .to(storeDocumentMock);
            }
        });
        context.start();

        //Act
        File file1 = File.createTempFile("test-file", ".xml", testWatchFilePath.toFile());

        //Assert
        storeDocumentMock.expectedMessageCount(1);
        storeDocumentMock.assertIsSatisfied();
        context.close(); //The message should be completely processed for the file to deleted
        assertFalse(file1.exists());

        //Cleanup
        file1.delete();
    }

    @Test
    public void testReceiveOriginalDocumentFileRoute_WhenFileIsUploaded_ShouldSendFileBody() throws Exception {
        //Arrange
        GenericFile<String> testFile = new GenericFile<>();
        testFile.setBody("Test file content");

        RouteDefinition route = context.getRouteDefinition(DocumentCamelRoute.RECEIVE_DOCUMENT_FILE.toString());
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(mockReceiveEndpoint);
                weaveByToUri("direct:.*")
                        .replace()
                        .to(storeDocumentMock);
            }
        });
        context.start();

        //Act
        Exchange exchange = new DefaultExchange(context);
        exchange.getMessage().setBody(testFile);
        template.send(mockReceiveEndpoint, exchange);

        //Assert
        storeDocumentMock.expectedMessageCount(1);
        storeDocumentMock.expectedMessagesMatches(
                messageExchange -> messageExchange.getMessage().getBody().equals(testFile.getBody())
        );
        storeDocumentMock.assertIsSatisfied();
    }
}
