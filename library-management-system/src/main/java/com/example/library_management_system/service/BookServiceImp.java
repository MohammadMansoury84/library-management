package com.example.library_management_system.service;

import com.example.library_management_system.Dto.BookRequestDTO;
import com.example.library_management_system.Dto.BookResponseDTO;
import com.example.library_management_system.Exception.ResourceNotFoundException;
import com.example.library_management_system.Mapper.BookMapper;
import com.example.library_management_system.Model.Book;
import com.example.library_management_system.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO){
        Book book= Book.builder()
                .title(bookRequestDTO.getTitle())
                .totalCopies(bookRequestDTO.getTotalCopies())
                .availableAmount(bookRequestDTO.getTotalCopies())
                .build();

        return bookMapper.bookToBookResponseDTO(bookRepository.save(book));
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::bookToBookResponseDTO)
                .toList();
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book=getBookOrThrow(id);
        return bookMapper.bookToBookResponseDTO(book);

    }

    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO request) {
        Book book=getBookOrThrow(id);
        book.setTitle(request.getTitle());
        book.setTotalCopies(request.getTotalCopies());
        return bookMapper.bookToBookResponseDTO(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)){
            throw new  ResourceNotFoundException("Book with id " + id + " was not found");
        }
        bookRepository.deleteById(id);
    }


    @Override
    public List<BookResponseDTO> searchBooks(String title) {
        return bookRepository.findBookByTitleContainsIgnoreCase(title)
                .stream()
                .map(bookMapper::bookToBookResponseDTO)
                .toList();
    }



    private Book getBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book with id " + id + " was not found")
                );
    }



}
