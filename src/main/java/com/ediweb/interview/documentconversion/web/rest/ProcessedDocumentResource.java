package com.ediweb.interview.documentconversion.web.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ediweb.interview.documentconversion.domain.OriginalDocument;
import com.ediweb.interview.documentconversion.service.ProcessedDocumentService;
import com.ediweb.interview.documentconversion.service.dto.ProcessedDocumentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link OriginalDocument}.
 */
@RestController
@RequestMapping("/api")
public class ProcessedDocumentResource {

    private final Logger log = LoggerFactory.getLogger(ProcessedDocumentResource.class);

    private static final String ENTITY_NAME = "processed-document";

    private final ProcessedDocumentService processedDocumentService;

    public ProcessedDocumentResource(ProcessedDocumentService processedDocumentService) {
        this.processedDocumentService = processedDocumentService;
    }

    /**
     * {@code GET  /documents} : get all the documents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documents in body.
     */
    @GetMapping("/documents")
    public List<ProcessedDocumentDTO> getAllDocuments() {
        log.debug("REST request to get all Documents");
        return processedDocumentService.findAll();
    }


    /**
     * {@code GET  /documents} : get all the documents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documents in body.
     */
    @GetMapping(value = "/documents", produces = MediaType.APPLICATION_XML_VALUE)
    public List<String> getAllDocumentBodiesAsXML() {
        log.debug("REST request to get all Documents");
        return processedDocumentService.findAll()
                .stream()
                .map(ProcessedDocumentDTO::getDocumentBody)
                .collect(Collectors.toList());
    }

    /**
     * {@code GET  /documents/:id} : get the "id" document.
     *
     * @param id the id of the documentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/documents/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getProcessedDocument(@PathVariable Long id) {
        log.debug("REST request to get Processed Document : {}", id);
        Optional<ProcessedDocumentDTO> processedDocumentDTO = processedDocumentService.findOne(id);

        return processedDocumentDTO.map(documentDTO -> ResponseEntity.ok(documentDTO.getDocumentBody())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code GET  /documents/:id} : get the "id" document.
     *
     * @param id the id of the documentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/documents/{id}/details")
    public ResponseEntity<ProcessedDocumentDTO> getProcessedDocumentDetails(@PathVariable Long id) {
        log.debug("REST request to get Processed Document : {}", id);
        Optional<ProcessedDocumentDTO> processedDocumentDTO = processedDocumentService.findOne(id);
        return ResponseEntity.of(processedDocumentDTO);
    }
}
