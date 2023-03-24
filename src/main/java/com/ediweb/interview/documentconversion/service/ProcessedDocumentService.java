package com.ediweb.interview.documentconversion.service;

import com.ediweb.interview.documentconversion.service.dto.ProcessedDocumentDTO;
import com.ediweb.interview.documentconversion.domain.ProcessedDocument;
import com.ediweb.interview.documentconversion.repository.ProcessedDocumentRepository;
import com.ediweb.interview.documentconversion.service.mapper.ProcessedDocumentMapper;
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
public class ProcessedDocumentService {

    private final Logger log = LoggerFactory.getLogger(ProcessedDocumentService.class);

    private final ProcessedDocumentRepository documentRepository;

    private final ProcessedDocumentMapper processedDocumentMapper;

    public ProcessedDocumentService(ProcessedDocumentRepository documentRepository, ProcessedDocumentMapper processedDocumentMapper) {
        this.documentRepository = documentRepository;
        this.processedDocumentMapper = processedDocumentMapper;
    }

    /**
     * Save a processed document.
     *
     * @param processedDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcessedDocumentDTO save(ProcessedDocumentDTO processedDocumentDTO) {
        log.debug("Request to save Processed Document : {}", processedDocumentDTO);

        ProcessedDocument processedDocument = processedDocumentMapper.toEntity(processedDocumentDTO);
        processedDocument = documentRepository.save(processedDocument);
        return processedDocumentMapper.toDto(processedDocument);
    }

    /**
     * Get all the documents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessedDocumentDTO> findAll() {
        log.debug("Request to get all Processed Documents");
        return documentRepository.findAll().stream().map(processedDocumentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one processed document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProcessedDocumentDTO> findOne(Long id) {
        log.debug("Request to get Processed Document : {}", id);
        return documentRepository.findById(id).map(processedDocumentMapper::toDto);
    }

    /**
     * Get one processed document by id.
     *
     * @param originalDocumentId the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProcessedDocumentDTO> findByOriginalDocumentId(Long originalDocumentId) {
        log.debug("Request to get Processed Document by OriginalDocument : {}", originalDocumentId);
        return documentRepository.findByOriginalDocumentId(originalDocumentId).map(processedDocumentMapper::toDto);
    }

    /**
     * Get one processed document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ProcessedDocumentDTO> updateDocumentBody(Long id, String newDocumentBody) {
        log.debug("Request to update Processed Document Body : {} {}", id, newDocumentBody);
        return documentRepository
                .findById(id)
                .map(existingDocument -> {
                    existingDocument.setDocumentBody(newDocumentBody);
                    return existingDocument;
                })
                .map(documentRepository::save)
                .map(processedDocumentMapper::toDto);
    }
}
