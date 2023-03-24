package com.ediweb.interview.documentconversion.service.dto;

import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OriginalDocumentDTO {
    private Long id;

    @NotBlank
    private String fileName;

    @NotBlank
    @JsonIgnore
    private String documentBody;

    @NotNull
    private OriginalDocumentEvent currentPhase;

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
        return "OriginalDocumentDTO{" +
                "id=" + getId() +
                ", fileName='" + getFileName() + "'" +
                ", documentBody='" + getDocumentBody() + "'" +
                ", currentPhase='" + getCurrentPhase() + "'" +
                ", receivedDate='" + getReceivedDate() + "'" +
                "}";
    }
}
