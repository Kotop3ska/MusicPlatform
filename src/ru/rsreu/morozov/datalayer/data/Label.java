package ru.rsreu.morozov.datalayer.data;

public record Label(long id, String name, int foundationYear) {
	public static final Label DEFAULT = new Label(-1, "NULL", -1);
}
