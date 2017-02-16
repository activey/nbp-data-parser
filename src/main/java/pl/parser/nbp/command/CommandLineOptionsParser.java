package pl.parser.nbp.command;

import org.fest.util.VisibleForTesting;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static java.lang.String.format;

/**
 * This class is responsible for parsing command line string arguments into set of parameters for computation.
 *
 * @author activey
 */
public class CommandLineOptionsParser {

    private static final int REQUIRED_ARGS_LENGTH = 3;

    public CommandLineOptions parseCommandLineOptions(String... applicationArguments)
            throws CommandLineParsingException {
        validateArgumentsLength(applicationArguments);

        String currencyCode = applicationArguments[0];
        LocalDate startDate = parseDateArgument(applicationArguments[1]);
        LocalDate endDate = parseDateArgument(applicationArguments[2]);

        // both dates should be in past
        validateIsDateInPast(startDate);
        validateIsDateInPast(endDate);

        // checking if start date is before end date
        validateStartBeforeEnd(startDate, endDate);

        return new CommandLineOptions(currencyCode, startDate, endDate);
    }

    private void validateIsDateInPast(LocalDate date) throws CommandLineParsingException {
        if (date.isBefore(LocalDate.now())) {
            return;
        }
        throw new CommandLineParsingException("Input date has to be in past!");
    }

    @VisibleForTesting
    LocalDate parseDateArgument(String dateArgument) throws CommandLineParsingException {
        try {
            return LocalDate.parse(dateArgument, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException parseException) {
            throw new CommandLineParsingException("Unable to parse date.", parseException);
        }
    }

    private void validateStartBeforeEnd(LocalDate startDate, LocalDate endDate) throws CommandLineParsingException {
        if (startDate.compareTo(endDate) > 0) {
            throw new CommandLineParsingException("End date can not be before start date!");
        }
    }

    private void validateArgumentsLength(String[] applicationArguments) throws CommandLineParsingException {
        if (applicationArguments.length != REQUIRED_ARGS_LENGTH) {
            throw new CommandLineParsingException(
                format("Wrong number of arguments passed, required length = %s.", REQUIRED_ARGS_LENGTH));
        }
    }
}
