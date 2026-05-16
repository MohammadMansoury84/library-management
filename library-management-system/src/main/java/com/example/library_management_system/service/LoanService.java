package com.example.library_management_system.service;

import com.example.library_management_system.Dto.LoanRequestDTO;
import com.example.library_management_system.Dto.LoanResponseDTO;
import com.example.library_management_system.Dto.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoanService  {

    LoanResponseDTO borrowBook(LoanRequestDTO request);

    LoanResponseDTO returnBook(Long loanId);

    PageResponseDTO<LoanResponseDTO> getUserLoans(Long userId, Pageable pageable);

}
