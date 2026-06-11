package ru.rsreu.morozov.datalayer.data;

public record Genre(String name) {
	public static final Genre DEFAULT = new Genre("NULL");
}
