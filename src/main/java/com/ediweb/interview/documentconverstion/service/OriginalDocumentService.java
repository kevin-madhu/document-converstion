package com.ediweb.interview.documentconverstion.service;

import com.ediweb.interview.documentconverstion.domain.OriginalDocument;
import com.ediweb.interview.documentconverstion.domain.enumeration.DocumentProcessingStatus;
import com.ediweb.interview.documentconverstion.repository.OriginalDocumentRepository;
import com.ediweb.interview.documentconverstion.service.dto.OriginalDocumentDTO;
import com.ediweb.interview.documentconverstion.service.mapper.OriginalDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OriginalDocumentService {

    private final Logger log = LoggerFactory.getLogger(OriginalDocumentService.class);

    private final OriginalDocumentRepository originalDocumentRepository;

    private final OriginalDocumentMapper originalDocumentMapper;

    public OriginalDocumentService(OriginalDocumentRepository originalDocumentRepository, OriginalDocumentMapper originalDocumentMapper) {
        this.originalDocumentRepository = originalDocumentRepository;
        this.originalDocumentMapper = originalDocumentMapper;
    }

    /**
     * Save an original document.
     *
     * @param originalDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public OriginalDocumentDTO save(OriginalDocumentDTO originalDocumentDTO) {
        log.debug("Request to save Document : {}", originalDocumentDTO);

        OriginalDocument originalDocument = originalDocumentMapper.toEntity(originalDocumentDTO);
        originalDocument = originalDocumentRepository.save(originalDocument);
        return originalDocumentMapper.toDto(originalDocument);
    }

    /**
     * Get all the original documents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OriginalDocumentDTO> findAll() {
        log.debug("Request to get all Original Documents");
        return originalDocumentRepository.findAll().stream().map(originalDocumentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one original document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OriginalDocumentDTO> findOne(Long id) {
        log.debug("Request to get OriginalDocument : {}", id);
        return originalDocumentRepository.findById(id).map(originalDocumentMapper::toDto);
    }

    /**
     * Set processing status of original document by id.
     *
     * @param id the id of the entity.
     * @param processingStatus the processingStatus of the entity.
     */
    public void setProcessingStatus(Long id, DocumentProcessingStatus processingStatus) {
        log.debug("Request to set processing status of OriginalDocument : {} to {}", id, processingStatus);

        OriginalDocument originalDocument = originalDocumentRepository.findById(id).orElseThrow();
        originalDocument.setProcessingStatus(processingStatus);
        originalDocumentRepository.save(originalDocument);
    }
}
