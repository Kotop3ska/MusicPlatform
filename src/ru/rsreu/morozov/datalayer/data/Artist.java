package ru.rsreu.morozov.datalayer.data;

public record Artist(String name, String country, String labelName) {
	public static final Artist DEFAULT = new Artist("NULL", "NULL", "NULL");
}
