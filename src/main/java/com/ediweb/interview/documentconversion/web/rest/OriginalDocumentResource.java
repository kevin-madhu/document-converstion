package com.ediweb.interview.documentconversion.web.rest;

import com.ediweb.interview.documentconversion.domain.OriginalDocument;
import com.ediweb.interview.documentconversion.service.OriginalDocumentService;
import com.ediweb.interview.documentconversion.service.dto.OriginalDocumentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link OriginalDocument}.
 */
@RestController
@RequestMapping("/api")
public class OriginalDocumentResource {

    private final Logger log = LoggerFactory.getLogger(OriginalDocumentResource.class);

    private static final String ENTITY_NAME = "original-document";

    private final OriginalDocumentService originalDocumentService;

    public OriginalDocumentResource(OriginalDocumentService originalDocumentService) {
        this.originalDocumentService = originalDocumentService;
    }

    /**
     * {@code GET  /original-documents} : get all the documents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documents in body.
     */
    @GetMapping("/original-documents")
    public List<OriginalDocumentDTO> getAllOriginalDocuments() {
        log.debug("REST request to get all Original Documents");
        return originalDocumentService.findAll();
    }

    /**
     * {@code GET  /original-documents} : get all the documents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documents in body.
     */
    @GetMapping(value = "/original-documents", produces = MediaType.APPLICATION_XML_VALUE)
    public List<String> getAllOriginalDocumentBodiesAsXML() {
        log.debug("REST request to get all Original Documents");
        return originalDocumentService.findAll()
                .stream()
                .map(OriginalDocumentDTO::getDocumentBody)
                .collect(Collectors.toList());
    }

    /**
     * {@code GET  /documents/:id} : get the "id" document.
     *
     * @param id the id of the documentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/original-documents/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getOriginalDocument(@PathVariable Long id) {
        log.debug("REST request to get Original Document : {}", id);
        Optional<OriginalDocumentDTO> originalDocumentDTO = originalDocumentService.findOne(id);

        return originalDocumentDTO.map(documentDTO -> ResponseEntity.ok(documentDTO.getDocumentBody())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code GET  /documents/:id} : get the "id" document.
     *
     * @param id the id of the documentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/original-documents/{id}/details")
    public ResponseEntity<OriginalDocumentDTO> getOriginalDocumentDetails(@PathVariable Long id) {
        log.debug("REST request to get Original Document : {}", id);
        Optional<OriginalDocumentDTO> originalDocumentDTO = originalDocumentService.findOne(id);
        return ResponseEntity.of(originalDocumentDTO);
    }
}
