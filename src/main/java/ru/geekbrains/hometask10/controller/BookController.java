package ru.geekbrains.hometask10.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.hometask10.entity.BookEntity;
import ru.geekbrains.hometask10.service.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@Tag(name = "Book")
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /book - получить все книги
    @GetMapping
    @Operation(summary = "get all books", description = "Получение списка всех книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    // GET /book/{id} - получить книгу по ID
    @GetMapping("/{id}")
    @Operation(summary = "get a book by id", description = "Поиск книги по её id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public ResponseEntity<Optional<BookEntity>> getBookById(@PathVariable long id) {
        final Optional<BookEntity> book;
        book = bookService.getBookById(id);
        if (book.isEmpty()) {
            System.out.println("Книга: не найдена");
            return ResponseEntity.notFound().build();
        } else {
            System.out.println("Книга: " + bookService.getBookById(id));
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }
    }

    // POST /book - добавить книгу (принимает JSON)
    @PostMapping
    @Operation(summary = "add a book to library", description = "Добавление книги в библиотеку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    public ResponseEntity<BookEntity> addBook(@RequestBody BookEntity book) {
        bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    // DELETE /book/{id} - удалить книгу
    @DeleteMapping("/{id}")
    @Operation(summary = "delete a book by id", description = "Удаление книги по её id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public ResponseEntity<BookEntity> deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

