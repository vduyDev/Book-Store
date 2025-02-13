package com.example.borrowingservice.borrowingline;


import com.example.common.DTO.BorrowingLineDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BorrowingLineService {

    private final BorrowingLineRepository borrowingLineRepository;

    public List<BorrowingLineDTO> getBorrowingLines(String borrowingId) {
         List<BorrowingLine> borrowingLine = borrowingLineRepository.findByBorrowing(borrowingId);
         List<BorrowingLineDTO> borrowingLineDTOS = borrowingLine.stream()
                 .map(BorrowingLineMapper::toBorrowingLineDTO)
                 .toList();
        return borrowingLineDTOS;
    }
}
