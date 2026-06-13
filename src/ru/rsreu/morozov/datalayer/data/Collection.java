package ru.rsreu.morozov.datalayer.data;

public record Collection(long id, String title, String description, int trackCount) {
	public static final Collection DEFAULT = new Collection(-1, "NULL", "NULL", -1);
}
