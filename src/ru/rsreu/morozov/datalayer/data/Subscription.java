package ru.rsreu.morozov.datalayer.data;

public record Subscription(String name, Double price, int durationDays) {
	public static final Subscription DEFAULT = new Subscription("NULL", -1.0, -1);
}
