package ru.geekbrains.hometask10.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.geekbrains.hometask10.JUnitSpringBootBase;
import ru.geekbrains.hometask10.entity.IssueEntity;
import ru.geekbrains.hometask10.repository.IssueRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class IssueControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    @NoArgsConstructor
    static class JUnitIssueResponse {
        private Long id;
        private Long bookId;
        private Long readerId;
        private LocalDateTime issued_at;
        private LocalDateTime returned_at;
    }

    @Test
    void getIssueSuccess() {
        // Подготовка данных: добавляем выдачу на случай если в репозитории пусто
        IssueEntity expected = issueRepository.save(new IssueEntity(1L,1L,1L));
        // действие: получаем книгу по ее id
        JUnitIssueResponse responseBody = webTestClient.get()
                .uri("/issue/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();
        // проверяем, что в теле ответа вернулся не null
        Assertions.assertNotNull(responseBody);
        // проверяем, что id выдачи совпадают и остальные значения полей тоже совпадают
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getBookId(), responseBody.getBookId());
        Assertions.assertEquals(expected.getReaderId(), responseBody.getReaderId());
        Assertions.assertEquals(expected.getIssued_at(), responseBody.getIssued_at());
        Assertions.assertEquals(expected.getReturned_at(), responseBody.getReturned_at());
    }

    @Test
    void getIssueNotFound() {
        // подготовка данных: узнаем самый большой id
        Long maxId = jdbcTemplate.queryForObject("select max(id) from issues", Long.class);
        // действие: ищем в репозитории выдачу с заведомо не существующим в нем id
        webTestClient.get()
                .uri("/issue/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllIssues() {
        // подготовка данных
        issueRepository.saveAll(List.of(
                new IssueEntity(1L,1L,1L),
                new IssueEntity(2L,2L,1L)
        ));

        // получение данных
        List<IssueEntity> expected = issueRepository.findAll();

        List<JUnitIssueResponse> responseBody = webTestClient.get()
                .uri("/issue")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitIssueResponse>>() {
                })
                .returnResult()
                .getResponseBody();
        // проверка полученных данных:
        // размер ожидаемого списка книг и полученного совпадает
        Assertions.assertEquals(expected.size(), responseBody.size());
        // проверяем каждый элемент списка: id совпадают и значения полей совпадают
        for (JUnitIssueResponse issueResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), issueResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getBookId(), issueResponse.getBookId()));
            Assertions.assertTrue(found);
        }
    }

}