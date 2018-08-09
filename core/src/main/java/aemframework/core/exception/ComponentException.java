package aemframework.core.exception;

/**
 * Class to handle the component exceptions so that the errors are not visible
 * to the end user.
 * 
 */
public class ComponentException extends RuntimeException {

	/**
	 * Default Serial UID.
	 */
	private static final long serialVersionUID = -1903025623362164418L;

	/**
	 * Parameterized constructor. Takes exception object as argument
	 * 
	 * @param exc
	 *            exception.
	 * @param message
	 *            exception message. throwable exception parameter.
	 */
	public ComponentException(String message, Throwable exc) {
		super(message, exc);
	}

}
