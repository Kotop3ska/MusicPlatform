package ru.rsreu.morozov.resourcer;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ProjectResourcer implements Resourcer {
	public static final String MISSING_PARAMETER_VALUE_MESSAGE_FORMAT = "Value for parameter \"%s\" is missing";

	private static final String DEFAULT_PROPERTY_NAME = "resources.text";
	private static String propertyName = DEFAULT_PROPERTY_NAME;

	private ProjectResourcer() {
	}

	public static Resourcer getInstance() {
		return ResourcerHolder.resourcer;
	}

	private static void reinitialize() {
		synchronized (ProjectResourcer.class) {
			if ((ProjectResourcer.isBasenameChanged()) || (ProjectResourcer.isLocaleChanged())) {
				ResourcerHolder.propertyName = ProjectResourcer.propertyName;
				ResourcerHolder.resources = ResourceBundle.getBundle(ResourcerHolder.propertyName, Locale.getDefault());
			}
		}
	}

	private static boolean isLocaleChanged() {
		Locale systemLocale = Locale.getDefault();
		Locale resourcerLocale = ResourcerHolder.resources.getLocale();

		return (!systemLocale.equals(resourcerLocale));
	}

	private static boolean isBasenameChanged() {
		return (!ProjectResourcer.propertyName.equals(ResourcerHolder.propertyName));
	}

	@Override
	public String getString(String resourceKey) {
		ProjectResourcer.reinitialize();
		ResourceBundle resources = ResourcerHolder.resources;
		String resourceValue;

		try {
			resourceValue = resources.getString(resourceKey);
		} catch (MissingResourceException e) {
			resourceValue = String.format(ProjectResourcer.MISSING_PARAMETER_VALUE_MESSAGE_FORMAT, resourceKey);
		}

		return resourceValue;
	}

	@Override
	public  void setPropertyName(String basename) {
		ProjectResourcer.propertyName = basename;
	}

	private static class ResourcerHolder {
		private static String propertyName = ProjectResourcer.propertyName;
		private static ResourceBundle resources = ResourceBundle.getBundle(ProjectResourcer.propertyName,
				Locale.getDefault());
		private static Resourcer resourcer = new ProjectResourcer();
	}
}

