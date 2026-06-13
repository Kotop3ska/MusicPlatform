package ru.rsreu.morozov.datalayer.data;

public record Track(long id, String title, String artistName, String albumTitle, int duration, int playCount, String genreName) {
	public static final Track DEFAULT = new Track(-1, "NULL", "NULL", "NULL", -1, -1, "NULL");
}
