package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record Album(long id, String title, String artistName, LocalDate releaseDate, String releaseType, double avgRating, long reviewCount) {
	public static final Album DEFAULT = new Album(-1, "NULL", "NULL", LocalDate.MIN, "NULL", 0.0, 0);
}
