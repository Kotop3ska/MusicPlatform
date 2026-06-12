package ru.rsreu.morozov.datalayer.data;

public record Label(String name, int foundationYear) {
	public static final Label DEFAULT = new Label("NULL", -1);
}
