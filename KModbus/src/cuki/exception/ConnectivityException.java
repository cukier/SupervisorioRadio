package cuki.exception;

/**
 * Connect server exception
 */
@SuppressWarnings("serial")
public class ConnectivityException extends Exception {

	public ConnectivityException(String message) {
		super("Erro to Connect: " + message);
	}
}
