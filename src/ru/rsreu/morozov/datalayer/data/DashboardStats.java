package ru.rsreu.morozov.datalayer.data;

public record DashboardStats(long userCount, long trackCount, long albumCount,
		long subscriptionCount, long artistCount, long genreCount) {
	public static final DashboardStats DEFAULT = new DashboardStats(0, 0, 0, 0, 0, 0);
}
