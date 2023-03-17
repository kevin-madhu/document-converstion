package com.ediweb.interview.documentconverstion.repository;

import com.ediweb.interview.documentconverstion.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
