package com.ediweb.interview.documentconversion.service.mapper;

import com.ediweb.interview.documentconversion.domain.OriginalDocument;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentDTO;
import com.ediweb.interview.documentconversion.service.dto.ProcessedDocumentDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link OriginalDocument} and its DTO {@link ProcessedDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface OriginalDocumentMapper extends EntityMapper<OriginalDocumentDTO, OriginalDocument> {}
