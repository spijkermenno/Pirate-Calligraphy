package nl.saxion.cos;

/**
 * Thrown when the generated Jasmin code is not accepted by Jasmin.
 */
public class AssembleException extends Exception {
	public AssembleException( String msg, Exception innerException ) {
			super(msg, innerException);
	}
}
