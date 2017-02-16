package pl.parser.nbp.command;

import org.junit.Test;
import org.mockito.InjectMocks;
import pl.parser.nbp.BaseUnitTest;

/**
 * @author activey
 */
public class CommandLineOptionsParserTest extends BaseUnitTest {

    @InjectMocks
    private CommandLineOptionsParser parser;

    @Test
    public void shouldThrowExceptionIfDateValueIsIncorrect() throws CommandLineParsingException {
        // then
        expectedException.expect(CommandLineParsingException.class);

        // when
        parser.parseDateArgument("thisisnotadate");
    }

    @Test
    public void shouldNotThrowExceptionIfDateValueIsCorrect() throws CommandLineParsingException {
        // then
        // expect no exception

        // when
        parser.parseDateArgument("2013-02-01");
    }

    @Test
    public void shouldThrowExceptionWhenStartDateIsInFuture() throws CommandLineParsingException {
        // then
        expectedException.expect(CommandLineParsingException.class);

        // when
        parser.parseCommandLineOptions("USD", "2020-01-01", "2030-01-01");
    }

    @Test
    public void shouldThrowExceptionWhenEndDateIsInFuture() throws CommandLineParsingException {
        // then
        expectedException.expect(CommandLineParsingException.class);

        // when
        parser.parseCommandLineOptions("USD", "2010-01-01", "2020-01-01");
    }

    @Test
    public void shouldThrowExceptionWhenStartDateIsAfterEndDate() throws CommandLineParsingException {
        // then
        expectedException.expect(CommandLineParsingException.class);

        // when
        parser.parseCommandLineOptions("USD", "2013-02-01", "2013-01-01");
    }

    @Test
    public void shouldNotThrowExceptionWhenStartDateIsBeforeEndDate() throws CommandLineParsingException {
        // then
        // expect no exception

        // when
        parser.parseCommandLineOptions("USD", "2013-01-28", "2013-01-31");
    }

}