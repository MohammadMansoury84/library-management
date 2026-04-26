package com.example.library_management_system.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name ="book_id",nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(nullable = false)
    private LocalDate loanDate;


    private LocalDate returnDate;
}
