package com.example.library_management_system.Dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequestDTO {

    @NotNull(message = "شناسه کاربر الزامی است")
    private Long userId;

    @NotNull(message = "شناسه کتاب الزامی است")
    private Long bookId;

    @FutureOrPresent(message = "تاریخ بازگشت نمی‌تواند در گذشته باشد")
    private LocalDate returnDate;

}
