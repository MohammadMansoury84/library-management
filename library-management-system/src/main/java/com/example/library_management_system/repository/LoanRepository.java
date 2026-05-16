package com.example.library_management_system.repository;

import com.example.library_management_system.Model.Loan;
import com.example.library_management_system.Model.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    Page<Loan> findLoanByUserId(Long UserId, Pageable pageable);

    Page<Loan> findLoansByStatus(LoanStatus loanStatus,Pageable pageable);

    Page<Loan>findLoanByBookIdAndStatus(Long bookId, LoanStatus status,Pageable pageable);

    Optional<Loan> findLoanByUserIdAndBookIdAndStatus(Long userId,Long bookId,LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND l.status = 'ACTIVE'")
    Page<Loan> findActiveLoansByBook(@Param("bookId") Long bookId, Pageable pageable);

}
