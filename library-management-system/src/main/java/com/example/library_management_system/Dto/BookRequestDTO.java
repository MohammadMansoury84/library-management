package com.example.library_management_system.Dto;

import com.example.library_management_system.Validation.UniqueIsbn;
import com.example.library_management_system.Validation.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookRequestDTO {

    @NotBlank(message = "Book title is required")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;

    @Min(value = 1, message = "Total copies must be at least 1")
    @Max(value = 100, message = "Total copies cannot be more than 100")
    private int totalCopies;

    @Pattern(
            regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$).*$",
            message = "فرمت ISBN معتبر نیست"
    )

    @UniqueIsbn(groups = ValidationGroups.OnCreate.class)
    @Pattern(regexp = "...", message = "فرمت ISBN معتبر نیست")
    private String isbn;
}
