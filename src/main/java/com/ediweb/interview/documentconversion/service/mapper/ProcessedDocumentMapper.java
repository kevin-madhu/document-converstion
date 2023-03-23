package com.ediweb.interview.documentconversion.service.mapper;

import com.ediweb.interview.documentconversion.domain.ProcessedDocument;
import com.ediweb.interview.documentconversion.service.dto.ProcessedDocumentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link ProcessedDocument} and its DTO {@link ProcessedDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessedDocumentMapper extends EntityMapper<ProcessedDocumentDTO, ProcessedDocument> {
    @Mapping(target = "originalDocumentId", source = "originalDocument.id")
    ProcessedDocumentDTO toDto(ProcessedDocument s);

    @Mapping(target = "originalDocument.id", source = "originalDocumentId")
    ProcessedDocument toEntity(ProcessedDocumentDTO d);
}
