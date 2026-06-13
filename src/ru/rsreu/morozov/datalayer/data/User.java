package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record User(long id, String username, String email, String subscriptionName, LocalDate createdAt) {
	public static final User DEFAULT = new User(-1, "NULL", "NULL", "NULL", LocalDate.MIN);
}
