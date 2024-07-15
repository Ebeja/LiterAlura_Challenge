package com.example.literAlura.repository;

import com.example.literAlura.model.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    AuthorEntity findByNameIgnoreCase(String name);
}


