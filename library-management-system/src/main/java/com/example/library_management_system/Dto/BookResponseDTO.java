package com.example.library_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BookResponseDTO {

    private Long id;
    private String title;
    private Integer availableAmount;
    private boolean available;

}
