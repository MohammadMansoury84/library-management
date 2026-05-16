package com.example.library_management_system.Controller;

import com.example.library_management_system.Dto.BookRequestDTO;
import com.example.library_management_system.Dto.BookResponseDTO;
import com.example.library_management_system.Dto.PageResponseDTO;
import com.example.library_management_system.Validation.ValidationGroups;
import com.example.library_management_system.service.BookService;
import com.example.library_management_system.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<BookResponseDTO> addBook(@Validated(ValidationGroups.OnCreate.class) @RequestBody BookRequestDTO bookRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.addBook(bookRequestDTO));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<BookResponseDTO>> getAllBooks(@RequestParam(required = false) String title, @PageableDefault(size = 10,sort = "title",direction = Sort.Direction.ASC) Pageable pageable){
        if(title!=null && !title.isBlank()){
            return ResponseEntity.ok(bookService.searchBooks(title,pageable));
        }
        return ResponseEntity.ok(bookService.getAllBooks(pageable));

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBook(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id,@Validated(ValidationGroups.OnUpdate.class) @RequestBody BookRequestDTO bookRequestDTO){
        return ResponseEntity.ok(bookService.updateBook(id,bookRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }



}
