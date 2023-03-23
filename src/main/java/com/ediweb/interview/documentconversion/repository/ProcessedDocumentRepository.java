package com.ediweb.interview.documentconversion.repository;

import com.ediweb.interview.documentconversion.domain.ProcessedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedDocumentRepository extends JpaRepository<ProcessedDocument, Long> {
}
