package ru.geekbrains.hometask10.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.h2.util.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.geekbrains.hometask10.JUnitSpringBootBase;
import ru.geekbrains.hometask10.entity.BookEntity;
import ru.geekbrains.hometask10.repository.BookRepository;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BookControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    @NoArgsConstructor
    static class JUnitBookResponse {
        private Long id;
        private String name;
    }

    @Test
    void getAllBooks() {

        // подготовка данных
        bookRepository.saveAll(List.of(
                BookEntity.ofName("first"),
                BookEntity.ofName("second")
        ));

        // получение данных
        List<BookEntity> expected = bookRepository.findAll();

        List<JUnitBookResponse> responseBody = webTestClient.get()
                .uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitBookResponse>>() {
                })
                .returnResult()
                .getResponseBody();
        // проверка полученных данных:
        // размер ожидаемого списка книг и полученного совпадает
        Assertions.assertEquals(expected.size(), responseBody.size());
        // проверяем каждый элемент списка: id совпадают и названия книги совпадают
        for (JUnitBookResponse bookResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), bookResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getName(), bookResponse.getName()));
            Assertions.assertTrue(found);
        }

    }

    @Test
    void getBookByIdSuccess() {
        // Подготовка данных: добавляем книгу на случай если в репозитории пусто
        BookEntity expected = bookRepository.save(BookEntity.ofName("NewBook"));
        // действие: получаем книгу по ее id
        JUnitBookResponse responseBody = webTestClient.get()
                .uri("/book/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitBookResponse.class)
                .returnResult().getResponseBody();
        // проверяем, что в теле ответа вернулся не null
        Assertions.assertNotNull(responseBody);
        // проверяем, что id книги совпадают и названия книги тоже совпадают
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void getBookByIdNotFound() {
        // подготовка данных: узнаем самый большой id
        Long maxId = jdbcTemplate.queryForObject("select max(id) from books", Long.class);
        // действие: ищем в репозитории книгу с заведомо не существующим в нем id
        webTestClient.get()
                .uri("/book/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addBook() {
        // подготовка данных: узнаем самый большой id и создаем новую книгу
        Long maxId = jdbcTemplate.queryForObject("select max(id) from books", Long.class);
        JUnitBookResponse request = new JUnitBookResponse();
        request.setName("Max id book");
        request.setId(maxId + 1L);
        // действие: добавляем в репозиторий новую книгу
        JUnitBookResponse responseBody = webTestClient.post()
                .uri("/book")
                .body(Mono.just(request), JSONObject.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitBookResponse.class)
                .returnResult().getResponseBody();
        // проверяем, что в теле ответа вернулся не null
        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        // проверяем, есть ли в репозитории новый элемент по id
        Assertions.assertTrue(bookRepository.findById(request.getId()).isPresent());
    }

    @Test
    void deleteBook() {
        // подготовка данных: узнаем самый большой id
        Long expected = jdbcTemplate.queryForObject("select max(id) from books", Long.class);
        // действие: удаляем из репозитория этот элемент с самым большим id
        webTestClient.delete()
                .uri("/api/book/" + expected)
                .exchange()
                .expectStatus().isOk();
        // проверяем, есть ли в репозитории элемент с искомым самым большим id
        Assertions.assertFalse(bookRepository.existsById(expected));
    }

}