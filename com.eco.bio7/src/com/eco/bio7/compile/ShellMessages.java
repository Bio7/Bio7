package com.eco.bio7.compile;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ShellMessages {
	private static final String BUNDLE_NAME = "imports.linuxshell"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private ShellMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
