package ru.geekbrains.hometask10.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.hometask10.entity.IssueEntity;
import ru.geekbrains.hometask10.service.IssueService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// @Slf4j - модуль lombok для логирования (см. в коде)
@RestController
@RequestMapping("/issue")
@Tag(name = "Issue")
public class IssueController {

    private final IssueService issueService;
    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // GET /issue/{id} - получить описание факта выдачи книги по id выдачи
    @GetMapping("/{id}")
    @Operation(summary = "get issue by id", description = "Поиск события выдачи книги по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public ResponseEntity<Optional<IssueEntity>> getIssue(@PathVariable long id) {
        final Optional<IssueEntity> issue;
        issue = issueService.getIssueById(id);
        if (issue.isEmpty()) {
            System.out.println("Выдача: " + id + " не найдена");
            return ResponseEntity.notFound().build();
        } else {
            System.out.println("Выдача: " + issueService.getIssueById(id));
            return ResponseEntity.status(HttpStatus.OK).body(issue);
        }
    }

    // GET /issue - получить все записи о выдаче книг
    @GetMapping
    @Operation(summary = "get all issues", description = "Получение списка всех событий выдачи книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<List<IssueEntity>> getAllIssues() {
        return ResponseEntity.status(HttpStatus.OK).body(issueService.getAllIssues());
    }

    // POST /issue - Создать новую запись о выдаче книги
    @PostMapping
    @Operation(summary = "add new issue a book", description = "Создание новой выдачи книги читателю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Книга с заданным id уже выдана"),
    })
    public ResponseEntity<IssueEntity> issueBook(@RequestBody IssueRequest request) {
        // log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", request.getReaderId(), request.getBookId());
        final IssueEntity issue;
        try {
            issue = issueService.addIssue(request);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        if (issue != null)
            return ResponseEntity.status(HttpStatus.OK).body(issue);
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // PUT /issue/{id} - Сделать запись о возврате книги по id выдачи
    @PutMapping("/{id}")
    @Operation(summary = "return a book to library", description = "Возврат книги в библиотеку путём" +
            " добавления даты возврата в выдачу по id выдачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public void returnBook(@PathVariable long id) {
        issueService.returnBook(id);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> notFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
