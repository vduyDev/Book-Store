package com.example.borrowingservice.borrowingline;
import com.example.common.enums.StatusBorrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BorrowingLineRepository extends JpaRepository<BorrowingLine,Integer> {

     @Query("SELECT b FROM BorrowingLine b WHERE b.borrowing.id = :borrowingId")
     List<BorrowingLine> findByBorrowing(String borrowingId);


}
