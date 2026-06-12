package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Subscription;

import java.util.List;

public interface SubscriptionDAO {
	List<Subscription> getAllSubscriptions();
	void addNewSubscription(String name, Double price, int durationDays);
	void deleteSubscriptionByName(String name);
}
