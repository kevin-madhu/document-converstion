package com.ediweb.interview.documentconverstion.repository;

import com.ediweb.interview.documentconverstion.domain.ProcessedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedDocumentRepository extends JpaRepository<ProcessedDocument, Long> {
}
