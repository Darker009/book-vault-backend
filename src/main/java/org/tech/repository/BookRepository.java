package org.tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tech.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
