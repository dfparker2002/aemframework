/*
 * ===========================================================================
 *
 * EnvironmentConfig.java
 *
 * Created on Oct 9, 2012
 *
 * Copyright 2012, Sapient;  All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * SapientNitro, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SapientNitro.
 * ===========================================================================
 */
package aemframework.core.config;

import java.util.Dictionary;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import aemframework.core.utility.DictionayUtils;

/**
 * <p>
 * The responsibility of this class is to read properties specific for an
 * Environment.
 * </p>
 */
@Component(description = "EnvironmentConfig", immediate = true, metatype = true, label = "EnvironmentConfig")
@Service(value = EnvironmentConfig.class)
public class EnvironmentConfig {
	/**
	 * Injected service {@link BaseConfig}.
	 */
	@Reference
	private BaseConfig baseConfig;

	/**
	 * This method reads the system configurations.
	 * 
	 * @return <code>Dictionary</code> of properties.
	 */
	@SuppressWarnings("rawtypes")
	public Dictionary getConfigProperties() {
		return baseConfig.getConfigProperties((EnvironmentConfig.class).getName());
	}

	/**
	 * This method returns ViewHelper class name to the specific component
	 * passed as parameter.
	 * 
	 * @param propertyKey
	 *            -key of property to be retrieved.
	 * @return - value of property.
	 */
	public String getProperty(final String propertyKey) {
		@SuppressWarnings("rawtypes")
		Dictionary properties = getConfigProperties();

		return DictionayUtils.getString(properties, propertyKey);
	}
}
