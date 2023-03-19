package com.ediweb.interview.documentconverstion.repository;

import com.ediweb.interview.documentconverstion.domain.OriginalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginalDocumentRepository extends JpaRepository<OriginalDocument, Long> {
}
