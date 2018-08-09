package aemframework.core.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import aemframework.core.config.BaseConfig;

/**
 * Utility methods
 * 
 * @author Sapient
 * @version 1.0.DEV
 */

public final class Utility {
	// Holds the instance of logging information.
	private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);
	private static final int START = 0;
	private static final char CHAR_LESS_THAN = '<';
	private static final char CHAR_GREATER_THAN = '>';
	private static final int DAYS_IN_YEAR = 365;

	private Utility() {
		// private constructor
	}

	/**
	 * returns cookie value for a particular cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(SlingHttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(cookieName)) {
					return cookies[i].getValue();
				}
			}
		}
		return "";
	}

	/**
	 * This method will return true if the string object passed is null or ""
	 * (empty)
	 * 
	 * @param pString
	 *            The String object
	 */
	public static boolean isNullOrEmpty(final String pString) {
		Boolean nullOrEmpty = true;
		if (pString == null) {
			nullOrEmpty = true;
		} else if (("").equalsIgnoreCase(pString.trim())) {
			nullOrEmpty = true;
		} else {
			nullOrEmpty = false;
		}
		return nullOrEmpty;
	}

	/**
	 * This method will strip the HTML tags from a given String.
	 * 
	 * @param pString
	 * @return
	 */
	public static String stripHTML(final String pString) {
		String returnValue = "";
		if (!Utility.isNullOrEmpty(pString)) {
			returnValue = pString;
			int index = returnValue.indexOf(CHAR_LESS_THAN);
			while (index >= 0) {
				final String temp = returnValue.substring(START, index);
				returnValue = temp + returnValue.substring(returnValue.indexOf(CHAR_GREATER_THAN) + 1);
				index = returnValue.indexOf(CHAR_LESS_THAN);
			}
		}
		return returnValue;
	}

	/**
	 * This method will return todays date in the passed date format.
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String getTodayDate(final String dateFormat) {
		// get today's date
		final Calendar cal = Calendar.getInstance();
		final Date tDate = cal.getTime();
		final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		return sdf.format(tDate);
	}

	/**
	 * This method will return todays time in the passed time/date format.
	 * 
	 * @param timeFormat
	 * @return
	 */
	public static String getTodayTime(final String timeFormat) {
		// get today's date
		final Calendar cal = Calendar.getInstance();
		final Date tDate = cal.getTime();
		final SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
		return sdf.format(tDate);
	}

	/**
	 * Add parameters to a URL
	 * 
	 * @param theUrl
	 * @param theParam
	 * @param theValue
	 * @return
	 */
	public static String addParamToUrl(final String theUrl, final String theParam, final String theValue) {
		String url = "";
		if (theUrl == null) {
			LOGGER.error("addParamToUrl : the URL sent in is null");
		} else {
			final String appendation = theParam + "=" + theValue;
			if (theUrl.indexOf('?') >= 0) {
				url = theUrl + "&" + appendation;
			} else {
				url = theUrl + "?" + appendation;
			}
		}
		return url;
	}

	public static String formatDate(Date date, String format) {
		// get today's date
		String dateStr = "";
		SimpleDateFormat f = new SimpleDateFormat(format);
		if (date != null) {
			dateStr = f.format(date);
		}
		return dateStr;
	}

	public static String formatDate(Date date, String format, String timezone) {
		String dateStr = "";
		SimpleDateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(TimeZone.getTimeZone(timezone));
		if (date != null) {
			dateStr = df.format(date);
		}
		return dateStr;

	}

	/**
	 * Removes a single parameter from the supplied URL string.
	 * 
	 * @param theUrl
	 * @param theParam
	 * @return
	 */
	public static String removeParamFromUrl(final String theUrl, final String theParam) {
		return theUrl.replaceFirst("(\\?|&)" + theParam + "=[^&]*($|&)", "$1").replaceFirst("&$", "");
	}

	/**
	 * Reads a property for the given node and stores the values in a String
	 * list.
	 * 
	 * @param n
	 * @param propertyName
	 * @return
	 */
	public static List<String> getPropertyListFromNode(Node n, String propertyName) {
		List<String> output = new ArrayList<String>();
		try {
			Property p = n.getProperty(propertyName);
			if (p.isMultiple()) {
				Value[] v = p.getValues();
				for (int i = 0; i < v.length; i++) {
					output.add(v[i].getString());
				}
			} else {
				output.add(p.getString());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return output;
	}

	/**
	 * Creates a list of background images for the paths provided in the list.
	 * 
	 * @param resolver
	 *            ResourceResolver
	 * @param nodePaths
	 * @param propertyName
	 * @return
	 */
	public static List<String> getBackGroundImageForNodes(ResourceResolver resolver, List<String> nodePaths,
			String propertyName) {

		LOGGER.debug(
				"Utility.getBackGroundImageForNodes() :: Preparing a list of background images of the carousel articles/pages.");
		List<String> bgImagePaths = new ArrayList<String>();
		try {

			Node contentNode = null;
			Node pathNode = null;
			Node imageNode = null;
			String contentPath = "";
			Resource resource = null;
			for (int i = 0; i < nodePaths.size(); i++) {

				contentPath = nodePaths.get(i);
				resource = resolver.getResource(contentPath);
				pathNode = resource != null ? resource.adaptTo(Node.class) : null;
				if (pathNode != null && pathNode.hasNode("jcr:content")) {

					contentNode = pathNode.getNode("jcr:content");
					if (contentNode != null && contentNode.hasNode("image")) {

						imageNode = contentNode.getNode("image");
						if (imageNode != null && imageNode.hasProperty("fileReference")) {

							bgImagePaths.add(imageNode.getProperty("fileReference").getString());
						} else if (null != getBackgroundImageFromDesign(contentPath, contentNode, resolver)
								&& !("").equals(getBackgroundImageFromDesign(contentPath, contentNode, resolver))) {

							bgImagePaths.add(getBackgroundImageFromDesign(contentPath, contentNode, resolver));
						} else {
							LOGGER.info(
									"Utility.getBackGroundImageForNodes() :: Image not configured for article/page :: "
											+ contentPath);
							bgImagePaths.add("");
						}
					} else {
						bgImagePaths.add("");
					}
				} else {
					bgImagePaths.add("");
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return bgImagePaths;
	}

	/**
	 * This method is used to get background image as string.
	 * 
	 * @param String
	 *            contentPath
	 * @param Node
	 *            contentNode
	 * @param ResourceResolver
	 *            resolver
	 * @return returns background image path or null
	 */

	private static String getBackgroundImageFromDesign(String contentPath, Node contentNode,
			ResourceResolver resolver) {

		String imagePath = "";
		try {

			Page page = null;
			Design design = null;
			String jsonString = "";
			JsonObject jsonObject = null;
			String resType = "";
			if (contentNode != null && contentNode.hasProperty("sling:resourceType")) {

				resType = contentNode.getProperty("sling:resourceType").getString();
				resType = resType.substring(resType.lastIndexOf("/") + 1); // Get
																			// the
																			// design
																			// node
				page = resolver.adaptTo(PageManager.class).getPage(contentPath);
				design = resolver.adaptTo(Designer.class).getDesign(page);
				jsonString = design.getJSON();
				if (StringUtils.isNotEmpty(jsonString) && jsonString.contains(resType)) {
					jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
					try {

						if (jsonObject.getAsJsonObject(resType).has("backgroundImage")) {

							if (null != jsonObject.getAsJsonObject(resType).getAsJsonObject("backgroundImage")
									&& jsonObject.getAsJsonObject(resType).getAsJsonObject("backgroundImage")
											.has("image")) {

								if (null != jsonObject.getAsJsonObject(resType).getAsJsonObject("backgroundImage")
										.getAsJsonObject("image")
										&& jsonObject.getAsJsonObject(resType).getAsJsonObject("backgroundImage")
												.getAsJsonObject("image").has("fileReference")) {

									imagePath = jsonObject.getAsJsonObject(resType).getAsJsonObject("backgroundImage")
											.getAsJsonObject("image").getAsJsonObject("fileReference").getAsString();
								}
							}
						}
						if (null != imagePath && ("").equals(imagePath)) {

							LOGGER.debug(
									"Utility.getBackgroundImageFromDesign() :: Image is not set in design dialog.");
						}
					} catch (JsonParseException jsonExc) {

						LOGGER.error("Utility.getBackgroundImageFromDesign() :: JSONException while retrieving image.",
								jsonExc);
					}
				}
			}
		} catch (ValueFormatException valueFormatExc) {

			LOGGER.error("Utility.getBackgroundImageFromDesign() :: ValueFormatException while retrieving image.",
					valueFormatExc);
		} catch (PathNotFoundException pathNotFoundExc) {

			LOGGER.error("Utility.getBackgroundImageFromDesign() :: PathNotFoundException while retrieving image.",
					pathNotFoundExc);
		} catch (RepositoryException repExc) {

			LOGGER.error("Utility.getBackgroundImageFromDesign() :: RepositoryException while retrieving image.",
					repExc);
		} catch (JsonParseException jsonExc) {

			LOGGER.error("Utility.getBackgroundImageFromDesign() :: JSONException while retrieving image.", jsonExc);
		} catch (Exception exc) {

			LOGGER.error("Utility.getBackgroundImageFromDesign() :: Exception while retrieving image.", exc);
		}
		return imagePath;
	}

	/**
	 * This method is used to get property as string array irrespective of what
	 * is returned.
	 * 
	 * @param properties
	 *            properties
	 * @param propertyName
	 *            propertyName
	 * @return returns property value as object array or null
	 */
	public static List<String> getPropertyAsStringList(final Map<String, Object> properties,
			final String propertyName) {
		List<String> property = new ArrayList<String>();
		if (properties.get(propertyName) != null) {
			if (properties.get(propertyName) instanceof Object[]) {
				Object[] objects = (Object[]) properties.get(propertyName);
				for (int i = 0; i < objects.length; i++) {
					property.add(objects[i].toString());
				}
			} else {
				if (StringUtils.isNotEmpty(properties.get(propertyName).toString())) {
					property.add(properties.get(propertyName).toString());
				}
			}
		}
		return property;
	}

	/**
	 * This method is use to convert the input stream to byte array so that we
	 * can read from the input stream multiple times to create the DAM asset.
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[8192];
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		int length;
		baos.reset();

		while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
			baos.write(buffer, 0, length);
		}

		return baos.toByteArray();
	}

	/**
	 * This method returns absolute URL of a page node.
	 * 
	 * @param resolver
	 * @param node
	 * @param extension
	 *            - If extension is null, the default URL extension will be
	 *            html.
	 * @return The absolute URL of the node resource.
	 */
	public static URI getAbsoluteUrlOnAuthor(ResourceResolver resolver, Node node, String extension) {
		return getAbsoluteUrlByType(resolver, node, extension, Externalizer.AUTHOR);
	}

	/**
	 * This method returns absolute URL of a page node based on Externalizer
	 * type i.e. local, publish, author.
	 * 
	 * @param resolver
	 * @param node
	 * @param extension
	 *            - If extension is null, the default URL extension will be
	 *            html.
	 * @param type
	 *            -Externalizer type i.e. local, publish, author.
	 * 
	 * @return The absolute URL of the node resource.
	 */
	public static URI getAbsoluteUrlByType(ResourceResolver resolver, Node node, String extension, String type) {
		URI uri = null;
		Node pageNode = node;
		String pageExtension = "html";
		try {
			if (node.getPath().endsWith("jcr:content")) {
				pageNode = node.getParent();
			}
			if (extension != null) {
				pageExtension = extension;
			}
			Externalizer ext = resolver.adaptTo(Externalizer.class);
			uri = new URI(ext.externalLink(resolver, type, pageNode.getPath() + "." + pageExtension));
			LOGGER.debug("Absolute uri--::" + uri.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return uri;
	}

	/**
	 * Get the base site URL for Author
	 * 
	 * @param resolver
	 * @return Base URL.
	 * @throws URISyntaxException
	 */
	public static URI getAuthorBaseUrl(ResourceResolver resolver) throws URISyntaxException {
		return getBaseUrl(resolver, Externalizer.AUTHOR);
	}

	/**
	 * Get Author Url from the path provided
	 * 
	 * @param resolver
	 * @param path
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI getAuthorUrl(ResourceResolver resolver, String path) throws URISyntaxException {
		Externalizer ext = resolver.adaptTo(Externalizer.class);
		return new URI(ext.externalLink(resolver, Externalizer.AUTHOR, path));
	}

	/**
	 * Get the base URL based on type
	 * 
	 * @param resolver
	 * @param type
	 *            e.g. <code>Externalizer.AUTHOR</code>
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI getBaseUrl(ResourceResolver resolver, String type) throws URISyntaxException {
		Externalizer ext = resolver.adaptTo(Externalizer.class);
		return new URI(ext.externalLink(resolver, type, "/"));
	}

	/**
	 * This method returns the number classified in Millions or Billions.
	 * 
	 * @param growthValue
	 * @param requiredFormat
	 *            TODO
	 * @return The PDF version of a page.
	 */
	public static String formatBigNumber(Double growthValue, String requiredFormat) {

		double endFundValue = 0.00F;
		String endGrowthValue = "";
		String symbol = " K";
		if (growthValue >= 1000000000) {
			endFundValue = (double) growthValue / 1000000000;
			symbol = " B";
		} else if (growthValue >= 1000000) {
			endFundValue = (double) growthValue / 1000000;
			symbol = " M";
		} else {
			endFundValue = (double) growthValue / 1000;
			symbol = " K";
		}
		endGrowthValue = new DecimalFormat(requiredFormat).format(endFundValue);
		endGrowthValue = endGrowthValue + symbol;
		return endGrowthValue;
	}

	/**
	 * @param configNode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Dictionary getDictionary(BaseConfig baseConfig, String configNode) {
		return baseConfig.getConfigProperties(configNode);
	}

	/**
	 * Method to calculate years between dates
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int calculateDays(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		long daysBetween = 0;
		while (startCal.before(endDate)) {
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return (int) (daysBetween / DAYS_IN_YEAR);
	}

	/**
	 * Change the TimeZone of a Date object. It does not format the date.
	 * 
	 * Only TimeZone value is changed.
	 * 
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static Date fixDateWithZone(Date date, String timeZone) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(date);
		long millis = startCal.getTimeInMillis() + startCal.getTimeZone().getRawOffset()
				- TimeZone.getTimeZone(timeZone).getRawOffset();

		// Start output
		Calendar finalCal = Calendar.getInstance();
		finalCal.setTimeInMillis(millis);
		return finalCal.getTime();
	}

	public static String getCleansedString(String inputString, boolean isToLowerCase) {
		String outputString = StringUtils.defaultString(inputString);
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9_-]");
		Matcher match = pattern.matcher(inputString);
		while (match.find()) {
			String specialChar = match.group();
			if (isToLowerCase) {
				outputString = outputString.replaceAll("\\s+", "_").replaceAll("\\" + specialChar, "").toLowerCase();
			} else {
				outputString = outputString.replaceAll("\\s+", "_").replaceAll("\\" + specialChar, "");
			}
		}
		return outputString;
	}

}