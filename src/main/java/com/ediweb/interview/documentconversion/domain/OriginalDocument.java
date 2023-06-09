package com.ediweb.interview.documentconversion.domain;

import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "original_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OriginalDocument implements Serializable {
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
    @Column(name = "current_phase", nullable = false)
    private OriginalDocumentEvent currentPhase;

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

    public OriginalDocumentEvent getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(OriginalDocumentEvent currentPhase) {
        this.currentPhase = currentPhase;
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
        return "OriginalDocument{" +
                "id=" + getId() +
                ", fileName='" + getFileName() + "'" +
                ", documentBody='" + getDocumentBody() + "'" +
                ", currentPhase='" + getCurrentPhase() + "'" +
                ", receivedDate='" + getReceivedDate() + "'" +
                "}";
    }
}
