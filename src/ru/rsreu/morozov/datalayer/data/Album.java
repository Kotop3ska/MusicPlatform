package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record Album(String title, String artistName, LocalDate releaseDate, String releaseType) {
	public static final Album DEFAULT = new Album("NULL", "NULL", LocalDate.MIN, "NULL");
}
