package com.ediweb.interview.documentconversion.service.dto;

import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class OriginalDocumentEventDTO {
    @NotNull
    private Long id;

    private String documentBody;

    private OriginalDocumentEvent event;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDocumentBody() {
        return documentBody;
    }

    public void setDocumentBody(String documentBody) {
        this.documentBody = documentBody;
    }

    public OriginalDocumentEvent getEvent() {
        return event;
    }

    public void setEvent(OriginalDocumentEvent event) {
        this.event = event;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OriginalDocumentEventDTO)) {
            return false;
        }

        OriginalDocumentEventDTO originalDocumentDTO = (OriginalDocumentEventDTO) o;
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
                ", documentBody='" + getDocumentBody() + "'" +
                ", event='" + getEvent() + "'" +
                "}";
    }
}
