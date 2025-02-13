package com.example.borrowingservice.borrowingline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingLineRepository extends JpaRepository<BorrowingLine,Integer> {

     @Query("SELECT b FROM BorrowingLine b WHERE b.borrowing.id = :borrowingId")
     List<BorrowingLine> findByBorrowing(String borrowingId);
}
