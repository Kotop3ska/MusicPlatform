package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record Review(long id, String username, String albumTitle, int rating, LocalDate reviewDate) {
	public static final Review DEFAULT = new Review(-1, "NULL", "NULL", -1, LocalDate.MIN);
}
