package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Genre;
import java.util.List;

public interface GenreDAO {
	List<Genre> getAllGenres();
	void addNewGenre(String name);
	void updateGenre(long id, String name);
	void deleteGenre(long id);
}
