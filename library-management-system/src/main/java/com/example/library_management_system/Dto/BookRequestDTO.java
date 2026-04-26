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

    @NotBlank(message = "عنوان کتاب الزامی است")
    private String title;

    @Min(value = 1, message = "تعداد کل نسخه‌ها حداقل باید ۱ باشد")
    private int totalCopies;


}
