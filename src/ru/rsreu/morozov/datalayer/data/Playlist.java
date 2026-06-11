package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record Playlist(String playlistName, String username, LocalDate createdAt, int trackCount) {
	public static final Playlist DEFAULT = new Playlist("NULL", "NULL", LocalDate.MIN, -1);
}
