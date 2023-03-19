package com.ediweb.interview.documentconverstion.service.dto;

import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentProcessingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.Objects;

public class OriginalDocumentDTO {
    private Long id;

    @NotBlank
    private String fileName;

    @NotBlank
    private String documentBody;

    @NotNull
    private DocumentProcessingStatus processingStatus;

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
        if (!(o instanceof OriginalDocumentDTO)) {
            return false;
        }

        OriginalDocumentDTO originalDocumentDTO = (OriginalDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, originalDocumentDTO.id);
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
                ", processingStatus='" + getProcessingStatus() + "'" +
                ", receivedDate='" + getReceivedDate() + "'" +
                "}";
    }
}
