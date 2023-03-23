package com.ediweb.interview.documentconversion.repository;

import com.ediweb.interview.documentconversion.domain.OriginalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginalDocumentRepository extends JpaRepository<OriginalDocument, Long> {
}
