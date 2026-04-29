package com.example.library_management_system.service;

import com.example.library_management_system.Dto.LoanRequestDTO;
import com.example.library_management_system.Dto.LoanResponseDTO;
import com.example.library_management_system.Model.Loan;

import java.util.List;

public interface LoanService  {

    LoanResponseDTO borrowBook(LoanRequestDTO request);

    LoanResponseDTO returnBook(Long loanId);

    List<LoanResponseDTO> getUserLoans(Long userId);

}
