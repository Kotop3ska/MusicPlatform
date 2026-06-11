package ru.rsreu.morozov.resourcer;

public interface Resourcer {
	String getString(String resourceKey);
	void setPropertyName(String basename);
}