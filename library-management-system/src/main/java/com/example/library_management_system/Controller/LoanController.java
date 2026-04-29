package com.example.library_management_system.Controller;

import com.example.library_management_system.Dto.LoanRequestDTO;
import com.example.library_management_system.Dto.LoanResponseDTO;
import com.example.library_management_system.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDTO> borrowBook(@Valid @RequestBody LoanRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.borrowBook(request));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanResponseDTO> returnBook(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponseDTO>> getUserLoans(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getUserLoans(userId));
    }
}
