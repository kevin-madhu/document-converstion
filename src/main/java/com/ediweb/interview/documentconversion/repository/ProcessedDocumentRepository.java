package com.ediweb.interview.documentconversion.repository;

import com.ediweb.interview.documentconversion.domain.ProcessedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedDocumentRepository extends JpaRepository<ProcessedDocument, Long> {
    Optional<ProcessedDocument> findByOriginalDocumentId(Long originalDocumentId);
}
