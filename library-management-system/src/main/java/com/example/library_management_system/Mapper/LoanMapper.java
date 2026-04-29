package com.example.library_management_system.Mapper;

import com.example.library_management_system.Dto.LoanResponseDTO;
import com.example.library_management_system.Model.Loan;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface LoanMapper {
    LoanResponseDTO loanToLoanResponseDTO(Loan loan);
    Loan loanResponseDTOToLoan(LoanResponseDTO loanResponseDTO);
}
