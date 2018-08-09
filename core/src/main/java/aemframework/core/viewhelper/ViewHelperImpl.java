
package aemframework.core.viewhelper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import org.apache.commons.collections.MapUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import aemframework.core.exception.ComponentException;
import aemframework.core.exception.SystemException;
import aemframework.core.utility.Utility;

/**
 * <p>
 * CoreViewHelper implements the {@link ViewHelper} interface thus providing an
 * implementation to meet for following responsibilities:
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
public abstract class ViewHelperImpl implements ViewHelper {

	/**
	 * <p>
	 * Initial Map size for data and objects map.
	 * </p>
	 */
	protected static final int INITIAL_MAP_SIZE = 10;

	/**
	 * Holds the instance of {@link Logger} for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ViewHelperImpl.class);

	/**
	 * Constant key for Selector Array in map.
	 */
	public static final String KEY_SELECTORS = "selectors";

	/**
	 * <p>
	 * Constant to identify <code>currentPage</code> in the properties.
	 * </p>
	 */
	public static final String CURRENT_PAGE = "currentPage";

	/**
	 * <p>
	 * Constant to identify <code>currentNode</code> in the properties.
	 * </p>
	 */
	public static final String CURRENT_NODE = "currentNode";

	/**
	 * <p>
	 * Constant to identify <code>request</code> in the properties.
	 * </p>
	 */
	public static final String REQUEST = "request";

	/**
	 * <p>
	 * Constant to identify <code>response</code> in the properties.
	 * </p>
	 */
	public static final String RESPONSE = "response";

	/**
	 * <p>
	 * Provides implementation for the interface {@link #getData(PageContext)}.
	 * This method also provides for some pre and post processing.
	 * </p>
	 * 
	 * <p>
	 * This method does the following pre-processing:
	 * <ul>
	 * <li>Reads all the properties for the current node and sets those in a
	 * {@link Map}</li>
	 * <li>Fetches the {@link SlingScriptHelper} and sets into the {@link Map}
	 * </li>
	 * <li>Fetches the <code>currentStyle</code> and sets into the {@link Map}
	 * </li>
	 * <li>Fetches the <code>currentPage</code> and sets into the {@link Map}
	 * </li>
	 * </ul>
	 * <p>
	 * 
	 * <p>
	 * This method does not have any logic for post-processing and returns a
	 * {@link Map} as returned by the child implementations.
	 * </p>
	 * 
	 * @param pageContext
	 *            - page specific context that is used by the method to prepare
	 *            the data.
	 * 
	 * @return a {@link Map} as returned by the child implementations.
	 */
	public final Map<String, Object> getData(final PageContext pageContext) {

		Map<String, Object> content = new HashMap<String, Object>(INITIAL_MAP_SIZE);
		Map<String, Object> resources = new HashMap<String, Object>(INITIAL_MAP_SIZE);

		SlingHttpServletRequest request = (SlingHttpServletRequest) pageContext.getRequest();
		SlingHttpServletResponse response = (SlingHttpServletResponse) pageContext.getResponse();
		try {

			content.put("contentResourcePath", pageContext.getAttribute("contentResourcePath")); // TODO
																									// megha
			resources.put(REQUEST, request);
			resources.put(RESPONSE, response);
			resources.put(SLING, pageContext.findAttribute("sling"));
			SlingScriptHelper sling = (SlingScriptHelper) resources.get("sling");
			content.put(KEY_SELECTORS, sling.getRequest().getRequestPathInfo().getSelectors());
			Page currentPage = (Page) pageContext.findAttribute("currentPage");

			if (currentPage != null) {
				content.put("CURRENT_PAGE_PATH", currentPage.getPath());
				Map<String, Object> pageProps = new HashMap<String, Object>();
				pageProps.putAll(currentPage.getProperties());
				content.put("CURRENT_PAGE_PROPERTIES", pageProps);
			}
			content.putAll((ValueMap) pageContext.getAttribute(PROPERTIES));
			resources.put("session", pageContext.findAttribute("session"));
			resources.put("resourceResolver", pageContext.findAttribute("resourceResolver"));
			resources.put(CURRENT_PAGE, currentPage);
			resources.put(CURRENT_NODE, pageContext.findAttribute(CURRENT_NODE));
			resources.put("currentStyle", pageContext.findAttribute("currentStyle"));

			return this.onGetData(content, resources);
		} catch (ComponentException compEx) {
			LOGGER.error("Error in processing request for data: " + getStringFromMap(content));
			LOGGER.error("Stack Trace: " + compEx);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("Exception", "component.exception.msg");
			return result;
		} catch (Exception ex) {
			LOGGER.error("Error in processing request for data: " + getStringFromMap(content));
			LOGGER.error("Stack Trace: " + ex);
			request.setAttribute("error.status_code", "5000");
			throw new SystemException("Error in processing request for data: " + getStringFromMap(content), ex);
		}
	}

	protected Object getPageProperty(Map<String, Object> content, String key) {
		Map<String, Object> pageProps = MapUtils.getMap(content, "CURRENT_PAGE_PROPERTIES",
				new HashMap<String, Object>());
		return pageProps.get(key);
	}

	protected String getPropertyFromStyle(Map<String, Object> objects, String key) {
		String keyValue = "";
		Style currentStyle = (Style) objects.get("currentStyle");
		if (currentStyle.containsKey(key)) {
			keyValue = currentStyle.get(key, "");
		}
		return keyValue;
	}

	protected String getPagePropertyAsString(Map<String, Object> content, String key) {
		Map<String, Object> pageProps = MapUtils.getMap(content, "CURRENT_PAGE_PROPERTIES",
				new HashMap<String, Object>());
		Object propValue = pageProps.get(key);
		String valueString = "";
		if (propValue != null) {
			valueString = propValue.toString();
		}
		return valueString;
	}

	/**
	 * <p>
	 * Converts a {@link Map} to a {@link String} using the {@link MapUtils}
	 * utility class. This method has been created as private and not placed
	 * into utility because of the specific nature of its usage.
	 * </p>
	 * 
	 * @param content
	 *            - the {@link Map} which needs to be converted in a {@link Map}
	 *            .
	 * @return String representing the {@link Map}.
	 */
	private String getStringFromMap(final Map<String, Object> content) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(baos);

		MapUtils.verbosePrint(printStream, "", content);

		try {
			return baos.toString("utf-8");
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error("EUnsupportedEncodingException: ", ex);
			return "";
		}
	}




	/**
	 * This method is used to get the parameter date which is passed as a part
	 * of ajax call
	 * 
	 * @param content
	 * 
	 * @return returns date parameter as String
	 */
	protected String getDateParameterFromSelector(Map<String, Object> content) {
		String[] selectors = (String[]) content.get(KEY_SELECTORS);
		String date = "";
		if (selectors != null) {
			for (String selector : selectors) {
				if (selector.startsWith("date-")) {
					String[] shareClassSelector = selector.split("-");
					if (shareClassSelector.length == 2) {
						date = shareClassSelector[1];
						// TODO validate if share class obtained from selector
						// is valid
					}
					break;
				}
			}
		}
		return date;
	}

	protected boolean checkDummyData(SlingSettingsService slingSettings) {
		return isRunModeAuthor(slingSettings);
	}


	/**
	 * This method is use to determine if the current run mode is author or not.
	 * 
	 * @return
	 */
	public boolean isRunModeAuthor(SlingSettingsService slingSettings) {
		boolean flag = Boolean.FALSE;
		Set<String> runModes = slingSettings.getRunModes();
		if (runModes.contains("author")) {
			flag = Boolean.TRUE;
		}
		return flag;
	}

	/**
	 * <p>
	 * This method must be implemented by all viewHelpers and contains all the
	 * logic specific to components view helper requirements.
	 * </p>
	 * 
	 * @param content
	 *            - map of all data/properties in request and will hold any
	 *            <key,value> that needs to be sent back to the view
	 * @param objects
	 *            - map holding various sling objects to be used in various CQ
	 *            specific methods.
	 * 
	 * @return {@link Map} holding the data
	 */

	public abstract Map<String, Object> onGetData(Map<String, Object> content, Map<String, Object> objects);
}
