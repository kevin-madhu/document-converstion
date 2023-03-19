package com.ediweb.interview.documentconverstion.service;

import com.ediweb.interview.documentconverstion.domain.ProcessedDocument;
import com.ediweb.interview.documentconverstion.repository.ProcessedDocumentRepository;
import com.ediweb.interview.documentconverstion.service.dto.ProcessedDocumentDTO;
import com.ediweb.interview.documentconverstion.service.mapper.ProcessedDocumentMapper;
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
}
