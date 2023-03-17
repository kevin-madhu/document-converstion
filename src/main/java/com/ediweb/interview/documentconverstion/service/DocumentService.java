package com.ediweb.interview.documentconverstion.service;

import com.ediweb.interview.documentconverstion.domain.Document;
import com.ediweb.interview.documentconverstion.repository.DocumentRepository;
import com.ediweb.interview.documentconverstion.service.dto.DocumentDTO;
import com.ediweb.interview.documentconverstion.service.mapper.DocumentMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);

        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);
        return documentMapper.toDto(document);
    }
}
