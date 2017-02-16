package pl.parser.nbp.command;

/**
 * @author activey
 */
public class CommandLineParsingException extends Exception {

    public CommandLineParsingException(String message) {
        super(message);
    }

    public CommandLineParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}
