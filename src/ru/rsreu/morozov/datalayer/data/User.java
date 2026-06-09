package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record User(String username, String email, String subscriptionName, LocalDate createdAt) {
    public static final User DEFAULT = new User("NULL", "NULL", "NULL", LocalDate.MIN);
}
