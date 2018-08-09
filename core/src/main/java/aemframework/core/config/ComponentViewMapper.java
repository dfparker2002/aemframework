/*
 * ComponentViewMapper.java
 *
 * Created on Jun 18, 2012
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

package aemframework.core.config;

import java.util.Dictionary;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import aemframework.core.utility.DictionayUtils;

/**
 * <p>
 * This class is responsible for fetching the viewhelper class name for the
 * specified component from OSGI Configuration.
 * </p>
 * 
 * @author
 * @version 1.0
 * 
 */

@Component(description = "ComponentViewMapper", immediate = true, metatype = true, label = "ComponentViewMapper")
@Service(value = ComponentViewMapper.class)
public class ComponentViewMapper {

	/**
	 * Injected service {@link BaseConfig}.
	 */
	@Reference
	private BaseConfig baseConfig;

	/**
	 * This method returns ViewHelper class name to the specific component
	 * passed as parameter.
	 * 
	 * @param componentName
	 *            -name of the component.
	 * @return - ViewHelper class of the component.
	 */
	public String getViewHelper(final String componentName) {

		@SuppressWarnings("rawtypes")
		Dictionary properties = getConfigProperties();

		return DictionayUtils.getString(properties, componentName);

	}

	/**
	 * This method returns the properties specified under the config node.
	 * 
	 * @return <code>Dictionary</code> of properties.
	 */
	@SuppressWarnings("rawtypes")
	private Dictionary getConfigProperties() {
		return baseConfig.getConfigProperties((ComponentViewMapper.class).getName());
	}

}