package ru.rsreu.morozov.datalayer.data;

import java.time.LocalDate;

public record Playlist(long id, String playlistName, String username, LocalDate createdAt, int trackCount) {
	public static final Playlist DEFAULT = new Playlist(-1, "NULL", "NULL", LocalDate.MIN, -1);
}
