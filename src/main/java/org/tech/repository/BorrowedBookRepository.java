package org.tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tech.entity.BorrowedBook;
import org.tech.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

    long countByUserAndReturnedFalse(User user);

    List<BorrowedBook> findByUser(User user);

    List<BorrowedBook> findByUserAndReturned(User user, boolean returned);

    List<BorrowedBook> findByReturned(boolean returned);

    List<BorrowedBook> findByReturnedFalseAndDueDateBefore(LocalDate date);
}
