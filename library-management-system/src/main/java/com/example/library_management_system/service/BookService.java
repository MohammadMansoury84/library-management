package com.example.library_management_system.service;

import com.example.library_management_system.Dto.BookRequestDTO;
import com.example.library_management_system.Dto.BookResponseDTO;
import com.example.library_management_system.Dto.PageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);
    PageResponseDTO<BookResponseDTO> getAllBooks(Pageable pageable);
    BookResponseDTO getBookById(Long id);
    BookResponseDTO updateBook(Long id, BookRequestDTO request);
    void deleteBook(Long id);
    PageResponseDTO<BookResponseDTO> searchBooks(String title, Pageable pageable);
}
