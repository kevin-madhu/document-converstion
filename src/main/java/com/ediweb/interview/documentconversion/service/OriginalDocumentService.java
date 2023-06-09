package com.ediweb.interview.documentconversion.service;

import com.ediweb.interview.documentconversion.domain.enumeration.OriginalDocumentEvent;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentDTO;
import com.ediweb.interview.documentconversion.domain.OriginalDocument;
import com.ediweb.interview.documentconversion.repository.OriginalDocumentRepository;
import com.ediweb.interview.documentconversion.service.mapper.OriginalDocumentMapper;
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
     * @param currentPhase the processingStatus of the entity.
     */
    public OriginalDocumentDTO setCurrentPhase(Long id, OriginalDocumentEvent currentPhase) {
        log.debug("Request to set current life cycle Phase of OriginalDocument : {} to {}", id, currentPhase);

        OriginalDocument originalDocument = originalDocumentRepository.findById(id).orElseThrow(RuntimeException::new);
        originalDocument.setCurrentPhase(currentPhase);
        originalDocumentRepository.save(originalDocument);
        return originalDocumentMapper.toDto(originalDocument);
    }
}
