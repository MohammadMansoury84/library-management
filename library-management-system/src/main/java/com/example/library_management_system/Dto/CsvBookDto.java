package com.example.library_management_system.Dto;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CsvBookDto {

    @CsvBindByName(column = "title")
    @NotBlank(message = "Title must not be blank")
    private String title;

    @CsvBindByName(column = "isbn")
    @NotBlank(message = "ISBN must not be blank")
    private String isbn;

    @CsvBindByName(column = "totalCopies")
    @Min(value = 1,message ="Total copies must be at least 1" )
    private int totalCopies;

}
