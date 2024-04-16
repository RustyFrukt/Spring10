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
import ru.geekbrains.hometask10.entity.ReaderEntity;
import ru.geekbrains.hometask10.service.IssueService;
import ru.geekbrains.hometask10.service.ReaderService;

import java.util.List;
import java.util.Optional;

// @Slf4j - модуль lombok для логирования (см. в коде)
@RestController
@RequestMapping("/reader")
@Tag(name = "Reader")
public class ReaderController {

    private final ReaderService readerService;
    private final IssueService issueService;
    @Autowired
    public ReaderController(ReaderService readerService, IssueService issueService) {
        this.readerService = readerService;
        this.issueService = issueService;
    }

    // GET /reader/{id} - получить читателя по ID
    @GetMapping("/{id}")
    @Operation(summary = "get reader by id", description = "Поиск читателя по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public ResponseEntity<Optional<ReaderEntity>> getReaderById(@PathVariable long id) {
        //log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", request.getReaderId(), request.getBookId());
        final Optional<ReaderEntity> reader;
        reader = readerService.getReaderById(id);
        if (reader.isEmpty()) {
            System.out.println("Читатель: не найден");
            return ResponseEntity.notFound().build();
        } else {
            System.out.println("Читатель: " + readerService.getReaderById(id));
            return ResponseEntity.status(HttpStatus.OK).body(reader);
        }
    }

    // GET /reader - получить всех читателей
    @GetMapping
    @Operation(summary = "get all readers", description = "Получение списка всех читателей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<List<ReaderEntity>> getAllReaders() {
        return ResponseEntity.status(HttpStatus.OK).body(readerService.getAllReaders());
    }

    // POST /reader - добавить читателя (принимает JSON)
    @PostMapping
    @Operation(summary = "add a new reader", description = "Добавление нового читателя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    public ResponseEntity<ReaderEntity> addReader(@RequestBody ReaderEntity reader) {
        readerService.addReader(reader);
        return ResponseEntity.status(HttpStatus.CREATED).body(reader);
    }

    // PUT /reader/{id} - обновить данные читателя (принимает JSON)
    @PutMapping("/{id}")
    @Operation(summary = "update reader by id", description = "Обновление данных читателя по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public ResponseEntity<ReaderEntity> updateReaderById(@PathVariable long id, @RequestBody ReaderEntity reader) {
        readerService.updateReader(id, reader);
        return ResponseEntity.status(HttpStatus.CREATED).body(reader);
    }

    // DELETE /reader/{id} - удалить читателя
    @DeleteMapping("/{id}")
    @Operation(summary = "delete reader by id", description = "Удаление читателя по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public ResponseEntity<ReaderEntity> deleteReader(@PathVariable long id) {
        readerService.deleteReader(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // GET /reader/{id}/issue - вернуть список всех выдачей для данного читателя
    @GetMapping("/{id}/issue")
    @Operation(summary = "get issues by reader by id",
            description = "Получение всех событий выдачи книг читателю по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Объект с таким id не найден")
    })
    public ResponseEntity<List<IssueEntity>> getBooksByReader(@PathVariable long id) {
        final List<IssueEntity> readerIssues;
        readerIssues = issueService.getIssuesByReader(id);
        if (readerIssues.size() < 1) {
            System.out.println("Выдачи по читателю не найдены");
            return ResponseEntity.notFound().build();
        } else {
            System.out.println("Читатель: " + readerService.getReaderById(id));
            return ResponseEntity.status(HttpStatus.OK).body(readerIssues);
        }
    }

}
