package com.ediweb.interview.documentconverstion.service.mapper;

import com.ediweb.interview.documentconverstion.domain.ProcessedDocument;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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
