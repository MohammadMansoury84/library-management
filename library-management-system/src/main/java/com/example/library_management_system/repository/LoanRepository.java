package com.example.library_management_system.repository;

import com.example.library_management_system.Model.Loan;
import com.example.library_management_system.Model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    List<Loan> findLoanByUserId(Long UserId);

    List<Loan> findLoanByBookIdAndStatus(Long bookId, LoanStatus status);

    Optional<Loan> findLoanByUserIdAndBookIdAndStatus(Long userId,Long bookId,LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansByBook(Long bookId);

}
