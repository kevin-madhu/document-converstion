package com.ediweb.interview.documentconverstion.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "processed_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProcessedDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne
    private OriginalDocument originalDocument;

    @NotEmpty
    @Column(name = "document_body", nullable = false)
    private String documentBody;

    @Column(name = "handling_date", nullable = false)
    @CreationTimestamp
    private ZonedDateTime handlingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OriginalDocument getOriginalDocument() {
        return originalDocument;
    }

    public void setOriginalDocument(OriginalDocument originalDocument) {
        this.originalDocument = originalDocument;
    }

    public String getDocumentBody() {
        return documentBody;
    }

    public void setDocumentBody(String documentBody) {
        this.documentBody = documentBody;
    }

    public ZonedDateTime getHandlingDate() {
        return handlingDate;
    }

    public void setHandlingDate(ZonedDateTime handlingDate) {
        this.handlingDate = handlingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessedDocument)) {
            return false;
        }
        return id != null && id.equals(((ProcessedDocument) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + getId() +
                ", originalDocument='" + getOriginalDocument() + "'" +
                ", documentBody='" + getDocumentBody() + "'" +
                ", handlingDate='" + getHandlingDate() + "'" +
                "}";
    }
}
