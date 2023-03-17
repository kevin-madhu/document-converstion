package com.ediweb.interview.documentconverstion.service.mapper;

import com.ediweb.interview.documentconverstion.domain.Document;
import com.ediweb.interview.documentconverstion.service.dto.DocumentDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {}
