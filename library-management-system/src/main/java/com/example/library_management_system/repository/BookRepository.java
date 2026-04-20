package com.example.library_management_system.repository;

import com.example.library_management_system.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findBookByTitleContainsIgnoreCase(String title);

    List<Book> findBookByAvailableAmountGreaterThan(int amount);

    @Query("SELECT b from Book b where b.availableAmount >0 ORDER BY b.title")
    List<Book> findBookByAvailableAmountGreaterThanZero();





}
