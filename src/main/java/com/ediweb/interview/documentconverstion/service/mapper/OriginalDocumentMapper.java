package com.ediweb.interview.documentconverstion.service.mapper;

import com.ediweb.interview.documentconverstion.domain.OriginalDocument;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import com.ediweb.interview.documentconverstion.service.dto.OriginalDocumentDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link OriginalDocument} and its DTO {@link ProcessedDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface OriginalDocumentMapper extends EntityMapper<OriginalDocumentDTO, OriginalDocument> {}
