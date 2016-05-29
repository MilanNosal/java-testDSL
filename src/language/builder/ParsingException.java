package language.builder;

/**
 * Exception during creating the model.
 */
public class ParsingException extends Exception {
    private static final long serialVersionUID = 1L;

    public ParsingException(String msg) {
        super(msg);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
