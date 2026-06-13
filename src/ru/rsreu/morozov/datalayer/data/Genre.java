package ru.rsreu.morozov.datalayer.data;

public record Genre(long id, String name) {
	public static final Genre DEFAULT = new Genre(-1, "NULL");
}
