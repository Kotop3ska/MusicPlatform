package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.User;
import java.util.List;

public interface UserDAO {
	List<User> getAllUsers();
	User getUserDetail(long userId);
}
