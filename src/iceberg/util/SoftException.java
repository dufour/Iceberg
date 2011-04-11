package iceberg.util;

public class SoftException extends RuntimeException {
	private static final long serialVersionUID = 2945424002747885038L;

	public SoftException(String message, Throwable cause) {
		super(message, cause);
	}

	public SoftException(Throwable cause) {
		super(cause);
	}
}
