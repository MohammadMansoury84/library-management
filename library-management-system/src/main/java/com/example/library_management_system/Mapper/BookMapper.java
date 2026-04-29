package com.example.library_management_system.Mapper;

import com.example.library_management_system.Dto.BookResponseDTO;
import com.example.library_management_system.Model.Book;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BookMapper {
   Book bookResponseDTOToBook(BookResponseDTO bookResponseDTO);
   BookResponseDTO bookToBookResponseDTO(Book book);
}
