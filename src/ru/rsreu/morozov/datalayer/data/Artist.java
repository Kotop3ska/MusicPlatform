package ru.rsreu.morozov.datalayer.data;

public record Artist(long id, String name, String country, String labelName) {
	public static final Artist DEFAULT = new Artist(-1, "NULL", "NULL", "NULL");
}
