package cuki.exception;

/**
 * Connect server exception
 */
@SuppressWarnings("serial")
public class DisconnectivityException extends Exception {

	public DisconnectivityException(String message) {
		super("Erro to disconnect: " + message);
	}
}
