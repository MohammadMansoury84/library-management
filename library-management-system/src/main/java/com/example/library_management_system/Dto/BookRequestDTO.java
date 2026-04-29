package com.example.library_management_system.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookRequestDTO {

    @NotBlank(message = "Book title is required")
    private String title;

    @Min(value = 1, message = "Total copies must be at least 1")
    private int totalCopies;


}
