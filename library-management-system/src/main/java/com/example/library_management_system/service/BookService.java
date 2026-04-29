package com.example.library_management_system.service;

import com.example.library_management_system.Dto.BookRequestDTO;
import com.example.library_management_system.Dto.BookResponseDTO;

import java.util.List;

public interface BookService {
    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);
    List<BookResponseDTO> getAllBooks();
    BookResponseDTO getBookById(Long id);
    BookResponseDTO updateBook(Long id, BookRequestDTO request);
    void deleteBook(Long id);
    List<BookResponseDTO> searchBooks(String title);
}
