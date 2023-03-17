package com.ediweb.interview.documentconverstion.service.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.ZonedDateTime;
import java.util.Objects;

public class DocumentDTO {
    private Long id;

    @NotBlank
    private String fileName;

    @NotBlank
    private String inputXML;

    @NotBlank
    private String outputXML;

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

    public String getInputXML() {
        return inputXML;
    }

    public void setInputXML(String inputXML) {
        this.inputXML = inputXML;
    }

    public String getOutputXML() {
        return outputXML;
    }

    public void setOutputXML(String outputXML) {
        this.outputXML = outputXML;
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
        if (!(o instanceof DocumentDTO)) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
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
                ", inputXML='" + getInputXML() + "'" +
                ", outputXML='" + getOutputXML() + "'" +
                ", receivedDate='" + getReceivedDate() + "'" +
                "}";
    }
}
