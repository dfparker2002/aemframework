/*
 * DictionayUtils.java
 *
 * Created on Jun 19, 2012
 *
 * Copyright 2012, SapientNitro;  All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * SapientNitro, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SapientNitro.
 * 
 */
package aemframework.core.utility;

import java.util.Dictionary;

/**
 * This class provides utility methods for the {@link java.util.Dictionary}.
 * 
 */
public final class DictionayUtils {

	private static final String RAWTYPES = "rawtypes";

	/**
	 * private constructor to prevent instantiation of class.
	 */
	private DictionayUtils() {
	}

	/**
	 * This method return back the property value specified by the key. In case
	 * the key is not present, method returns null.
	 * 
	 * @param properties
	 *            - Dictionary properties out of which the value is required.
	 * @param key
	 *            - Key whose value is required from the properties.
	 * @return String value of property or null in case the key is not present.
	 */
	public static String getString(@SuppressWarnings(RAWTYPES) final Dictionary properties, final String key) {
		if (null != properties && null != properties.get(key)) {
			return (String) properties.get(key);
		}
		return null;
	}

	/**
	 * This method return back the property value specified by the key. In case
	 * the key is not present, method returns null.
	 * 
	 * @param properties
	 *            - Dictionary properties out of which the value is required.
	 * @param key
	 *            - Key whose value is required from the properties.
	 * @return String [] value of property or null in case the key is not
	 *         present.
	 */
	public static String[] getStringArray(@SuppressWarnings(RAWTYPES) final Dictionary properties, final String key) {
		if (null != properties && null != properties.get(key)) {
			return (String[]) properties.get(key);
		}
		return null;
	}

	/**
	 * This method returns the property value specified by the key. In case the
	 * key is not present, method returns zero.
	 * 
	 * @param properties
	 *            - Dictionary properties out of which the value is required.
	 * @param key
	 *            - Key whose value is required from the properties.
	 * @return long value of property or zero in case the key is not present.
	 */
	public static long getLong(@SuppressWarnings(RAWTYPES) final Dictionary properties, final String key) {
		if (null != properties && null != properties.get(key)) {
			return ((Long) properties.get(key)).longValue();
		}
		return 0L;
	}

	/**
	 * This method return back the property value specified by the key. In case
	 * the key is not present, method returns false.
	 * 
	 * @param properties
	 *            - Dictionary properties out of which the value is required.
	 * @param key
	 *            - Key whose value is required from the properties.
	 * @return Boolean value of property or false in case the key is not
	 *         present.
	 */
	public static boolean getBoolean(@SuppressWarnings(RAWTYPES) final Dictionary properties, final String key) {
		if (null != properties && null != properties.get(key)) {
			return ((Boolean) properties.get(key)).booleanValue();
		}
		return false;
	}
}
