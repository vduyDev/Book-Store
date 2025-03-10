package com.example.bookservice.clients;


import com.example.common.configure.FeignClientConfig;
import com.example.common.DTO.BorrowingLineDTO;
import com.example.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "borrowing-service", configuration = FeignClientConfig.class)
public interface BorrowingClient {

    @GetMapping("/borrowinglines/get-by-borrowing/{borrowingId}")
    ApiResponse<List<BorrowingLineDTO>> getBorrowingLinesByBorrowingId(@PathVariable String borrowingId);
}
