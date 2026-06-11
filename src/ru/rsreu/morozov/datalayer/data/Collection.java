package ru.rsreu.morozov.datalayer.data;

public record Collection(String title, String description, int trackCount) {
	public static final Collection DEFAULT = new Collection("NULL", "NULL", -1);
}
