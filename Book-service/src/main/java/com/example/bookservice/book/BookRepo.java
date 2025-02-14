package com.example.bookservice.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepo extends JpaRepository<Book,Integer> {
    @Query("SELECT b FROM Book b WHERE b.name LIKE  %:name%")
    Page<Book> searchByName(@Param("name") String name, Pageable pageable);

    List<Book> findByIdIn(List<Integer> ids);

}
