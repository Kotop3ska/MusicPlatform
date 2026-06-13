package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Label;
import java.util.List;

public interface LabelDAO {
	List<Label> getAllLabels();
	void addNewLabel(String name, int foundationYear);
	void updateLabel(long id, String name, int foundationYear);
	void deleteLabel(long id);
}
