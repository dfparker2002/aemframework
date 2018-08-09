/*
 * ViewHelper.java
 *
 * Created on June 1, 2012
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
package aemframework.core.viewhelper;

import java.util.Map;

import javax.servlet.jsp.PageContext;

/**
 * <p>
 * This interface is the core to CQ application. This interface provided a
 * contract that every View Helper should implement.
 * </p>
 * 
 * <p>
 * Any implementation of this interface has the following responsibilities:
 * 
 * <ul>
 * <li>Provide a contract for all View Helper classes, to match up what the
 * <code>jsp</code> expect.</li>
 * <li>Provide capability to the framework to pre or post decorate the objects.
 * This allows the view helpers which inherit from this implementation to be
 * unaware of the nuisances of sling</li>
 * </ul>
 * <p>
 * 
 * @author kahuja
 * @version 1.0
 * 
 */
public interface ViewHelper {

	/**
	 * <p>
	 * Constant to identify component properties.
	 * </p>
	 */
	String PROPERTIES = "properties";

	/**
	 * <p>
	 * Constant to identify <code>Resource Resolver</code> in the properties.
	 * </p>
	 */
	String RESOURCE_RESOLVER = "resourceResolver";

	/**
	 * <p>
	 * Constant to identify <code>Sling</code> in the properties.
	 * </p>
	 */
	String SLING = "sling";

	/**
	 * <p>
	 * This is the method that every <code>view</code> should invoke to get the
	 * data they need from the view helper. This method allows us to provide a
	 * contract with which a <code>view</code> can interact with.
	 * 
	 * 
	 * @param pageContext
	 *            - page specific context that is used by the method to prepare
	 *            the data.
	 * 
	 * @return {@link Map} containing the data, which <code>view</code> uses to
	 *         render itself.
	 */
	Map<String, Object> getData(final PageContext pageContext);
}
