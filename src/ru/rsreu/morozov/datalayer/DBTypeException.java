package ru.rsreu.morozov.datalayer;

public class DBTypeException extends RuntimeException {
	public DBTypeException() {
		super("Неизвестный тип базы данных");
	}
}
