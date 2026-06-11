package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record Label(String name, LocalDate foundationYear) {
	public static final Label DEFAULT = new Label("NULL", LocalDate.MIN);
}
