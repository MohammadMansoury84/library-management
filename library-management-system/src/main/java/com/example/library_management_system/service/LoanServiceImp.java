package com.example.library_management_system.service;


import com.example.library_management_system.Dto.LoanRequestDTO;
import com.example.library_management_system.Dto.LoanResponseDTO;
import com.example.library_management_system.Exception.BookNotAvailableException;
import com.example.library_management_system.Exception.ResourceNotFoundException;
import com.example.library_management_system.Mapper.LoanMapper;
import com.example.library_management_system.Model.Book;
import com.example.library_management_system.Model.Loan;
import com.example.library_management_system.Model.LoanStatus;
import com.example.library_management_system.Model.User;
import com.example.library_management_system.repository.BookRepository;
import com.example.library_management_system.repository.LoanRepository;
import com.example.library_management_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImp implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;

    @Override
    @Transactional
    public LoanResponseDTO borrowBook(LoanRequestDTO request){
        Book book=bookRepository.findById(request.getBookId())
                .orElseThrow(()-> new ResourceNotFoundException("Book with id " + request.getBookId() + " was not found"));

        User user=userRepository.findById(request.getUserId())
                .orElseThrow(() ->new ResourceNotFoundException("User with id " + request.getBookId() + " was not found"));

        if (!(book.getAvailableAmount() >0)){
            throw new BookNotAvailableException("Book with id " + book.getId() + " is not available");
        }

        boolean alreadyBorrowed=loanRepository.findLoanByUserIdAndBookIdAndStatus(user.getId(),book.getId(), LoanStatus.ACTIVE)
                .isPresent();
        if (alreadyBorrowed){
            throw new BookNotAvailableException("The requested book is currently not available.");
        }

        book.setAvailableAmount(book.getAvailableAmount()-1);
        bookRepository.save(book);

        Loan loan=Loan.builder()
                .book(book)
                .user(user)
                .loanDate(LocalDate.now())
                .status(LoanStatus.ACTIVE)
                .build();

        return loanMapper.loanToLoanResponseDTO(loanRepository.save(loan));


    }

    @Override
    public LoanResponseDTO returnBook(Long loanId) {
        Loan loan=loanRepository.findById(loanId).orElseThrow(()->new ResourceNotFoundException("loan not found"));
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());
        Book book=loan.getBook();
        book.setAvailableAmount(book.getAvailableAmount()+1);
        bookRepository.save(book);

        return loanMapper.loanToLoanResponseDTO(loanRepository.save(loan));

    }

    @Override
    public List<LoanResponseDTO> getUserLoans(Long userId) {
        return loanRepository.findLoanByUserId(userId)
                .stream()
                .map(loanMapper::loanToLoanResponseDTO)
                .toList();
    }

}
