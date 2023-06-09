package com.ediweb.interview.documentconversion.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

public class ProcessedDocumentDTO {
    private Long id;

    @NotNull
    private Long originalDocumentId;

    @NotBlank
    @JsonIgnore
    private String documentBody;

    private ZonedDateTime handlingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalDocumentId() {
        return originalDocumentId;
    }

    public void setOriginalDocumentId(Long originalDocumentId) {
        this.originalDocumentId = originalDocumentId;
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
        if (!(o instanceof ProcessedDocumentDTO)) {
            return false;
        }

        ProcessedDocumentDTO processedDocumentDTO = (ProcessedDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processedDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProcessedDocument{" +
                "id=" + getId() +
                ", documentBody='" + getDocumentBody() + "'" +
                ", handlingDate='" + getHandlingDate() + "'" +
                "}";
    }
}
