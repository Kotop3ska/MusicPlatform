package ru.rsreu.morozov.datalayer.data;

public record Track(String title, String artistName, String AlbumTitle, int duration, int playCount, String genreName) {
	public static final Track DEFAULT = new Track("NULL", "NULL", "NULL", -1, -1, "NULL");
}


