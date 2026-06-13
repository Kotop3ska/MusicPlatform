package ru.rsreu.morozov.datalayer.data;

public record Subscription(long id, String name, double price, int durationDays) {
	public static final Subscription DEFAULT = new Subscription(-1, "NULL", -1.0, -1);
}
