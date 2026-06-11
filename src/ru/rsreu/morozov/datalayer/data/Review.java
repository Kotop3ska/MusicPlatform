package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record Review(String username, String albumTitle, int rating, LocalDate reviewDate) {
	public static final Review DEFAULT = new Review("NULL", "NULL", -1, LocalDate.MIN);
}
