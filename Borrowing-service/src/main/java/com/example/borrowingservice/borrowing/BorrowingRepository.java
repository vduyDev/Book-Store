package com.example.borrowingservice.borrowing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing,String> {

    List<Borrowing> findBorrowingByCustomerId(String id);
}
