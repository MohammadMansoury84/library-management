package com.example.library_management_system.Dto;

import com.example.library_management_system.Model.LoanStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanResponseDTO {

    private long id;

    private String bookTitle;
    private long bookId;

    private String userName;

    private LoanStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    private String message;

}



