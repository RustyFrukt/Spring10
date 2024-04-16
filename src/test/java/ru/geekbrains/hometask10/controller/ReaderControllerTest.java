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
import ru.geekbrains.hometask10.entity.ReaderEntity;
import ru.geekbrains.hometask10.repository.ReaderRepository;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ReaderControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    @NoArgsConstructor
    static class JUnitReaderResponse {
        private Long id;
        private String name;
    }

    @Test
    void getReaderByIdSuccess() {
        // Подготовка данных: добавляем книгу на случай если в репозитории пусто
        ReaderEntity expected = readerRepository.save(ReaderEntity.ofName("New Reader"));
        // действие: получаем читателя по его id
        JUnitReaderResponse responseBody = webTestClient.get()
                .uri("/reader/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitReaderResponse.class)
                .returnResult().getResponseBody();
        // проверяем, что в теле ответа вернулся не null
        Assertions.assertNotNull(responseBody);
        // проверяем, что id читателя совпадают и имена читателя тоже совпадают
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void getReaderByIdNotFound() {
        // подготовка данных: узнаем самый большой id
        Long maxId = jdbcTemplate.queryForObject("select max(id) from readers", Long.class);
        // действие: ищем в репозитории читателя с заведомо не существующим в нем id
        webTestClient.get()
                .uri("/reader/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllReaders() {

        // подготовка данных
        readerRepository.saveAll(List.of(
                ReaderEntity.ofName("First"),
                ReaderEntity.ofName("Second")
        ));

        List<ReaderEntity> expected = readerRepository.findAll();
        // получение данных
        List<JUnitReaderResponse> responseBody = webTestClient.get()
                .uri("/reader")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitReaderResponse>>() {
                })
                .returnResult()
                .getResponseBody();
        // проверка полученных данных:
        // размер ожидаемого списка книг и полученного совпадает
        Assertions.assertEquals(expected.size(), responseBody.size());
        // проверяем каждый элемент списка: id совпадают и названия книги совпадают
        for (JUnitReaderResponse readerResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), readerResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getName(), readerResponse.getName()));
            Assertions.assertTrue(found);
        }

    }

    @Test
    void addReader() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from readers", Long.class);
        JUnitReaderResponse request = new ReaderControllerTest.JUnitReaderResponse();
        request.setName("New Reader");
        request.setId(maxId + 1L);
        JUnitReaderResponse responseBody = webTestClient.post()
                .uri("/reader")
                .body(Mono.just(request), JSONObject.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitReaderResponse.class)
                .returnResult().getResponseBody();
        // проверяем, что в теле ответа вернулся не null
        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        // проверяем, есть ли в репозитории новый элемент по id
        Assertions.assertTrue(readerRepository.findById(request.getId()).isPresent());
    }

    @Test
    void updateReaderById() {
    }

    @Test
    void deleteReader() {
        // подготовка данных: узнаем самый большой id
        Long expected = jdbcTemplate.queryForObject("select max(id) from readers", Long.class);
        // действие: удаляем из репозитория этот элемент с самым большим id
        webTestClient.delete()
                .uri("/api/reader/" + expected)
                .exchange()
                .expectStatus().isOk();
        // проверяем, есть ли в репозитории элемент с искомым самым большим id
        Assertions.assertFalse(readerRepository.existsById(expected));
    }

    @Test
    void getBooksByReader() {
    }
}