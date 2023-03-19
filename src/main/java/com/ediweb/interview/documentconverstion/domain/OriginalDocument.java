package com.ediweb.interview.documentconverstion.domain;

import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentProcessingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "original_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OriginalDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "originalDocument")
    private ProcessedDocument processedDocument;

    @Column(name = "file_name")
    private String fileName;

    @NotEmpty
    @Column(name = "document_body", nullable = false)
    private String documentBody;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false)
    private DocumentProcessingStatus processingStatus;

    @Column(name = "received_date", nullable = false)
    @CreationTimestamp
    private ZonedDateTime receivedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocumentBody() {
        return documentBody;
    }

    public void setDocumentBody(String documentBody) {
        this.documentBody = documentBody;
    }

    public DocumentProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(DocumentProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }

    public ZonedDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(ZonedDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OriginalDocument)) {
            return false;
        }
        return id != null && id.equals(((OriginalDocument) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + getId() +
                ", fileName='" + getFileName() + "'" +
                ", documentBody='" + getDocumentBody() + "'" +
                ", processingStatus='" + getProcessingStatus() + "'" +
                ", receivedDate='" + getReceivedDate() + "'" +
                "}";
    }
}
