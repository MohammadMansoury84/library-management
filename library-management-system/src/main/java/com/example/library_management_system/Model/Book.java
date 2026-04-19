package com.example.library_management_system.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(nullable = false)
    private String title;


    @Column(columnDefinition = "BOOLEAN")
    private boolean available;

    @NotBlank
    @Column(nullable = false)
    private int availableAmount;

    @OneToMany(mappedBy = "book" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Loan> loans;
}
