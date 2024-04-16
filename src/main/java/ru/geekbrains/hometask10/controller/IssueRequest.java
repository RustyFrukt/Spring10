package ru.geekbrains.hometask10.controller;

import lombok.Data;

@Data
public class IssueRequest {

    // Идентификатор читателя
    private long readerId;

    // Идентификатор книги
    private long bookId;

}
