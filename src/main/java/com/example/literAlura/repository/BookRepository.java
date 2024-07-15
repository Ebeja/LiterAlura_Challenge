package com.example.literAlura.repository;

import com.example.literAlura.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    @Query("SELECT b FROM BookEntity b LEFT JOIN FETCH b.authors")
    List<BookEntity> findAllWithAuthors();
}

