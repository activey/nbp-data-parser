package pl.parser.nbp.xml;

import org.junit.Test;
import org.mockito.Mock;
import org.xml.sax.SAXException;
import pl.parser.nbp.BaseIntegrationTest;
import pl.parser.nbp.command.CommandLineOptions;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import static java.time.LocalDate.now;
import static org.mockito.BDDMockito.given;

/**
 * @author activey
 */
public class RatesXMLReaderIntegrationTest extends BaseIntegrationTest {

    @Mock
    private CommandLineOptions commandLineOptions;

    @Mock
    private BiConsumer<Double, Double> dummyConsumer;

    @Test
    public void shouldThrowAnExceptionWhenXmlFileNotFound() throws IOException, SAXException {
        // given
        RatesXMLReader reader = givenXmlReader("nonexistentfile", commandLineOptions, dummyConsumer);

        // then
        expectedException.expect(FileNotFoundException.class);

        // when
        reader.readRates();
    }

    @Test
    public void shouldNotThrowAnExceptionWhenXmlFileFound() throws IOException, SAXException {
        // given
        givenCommandLineOptions(now(), now().plusMonths(3), "EUR");
        RatesXMLReader reader = givenXmlReader("c235z131205", commandLineOptions, dummyConsumer);

        // then
        // expect no exception

        // when
        reader.readRates();
    }

    private void givenCommandLineOptions(LocalDate startDate, LocalDate endDate, String currencyCode) {
        given(commandLineOptions.getStartDate()).willReturn(startDate);
        given(commandLineOptions.getEndDate()).willReturn(endDate);
        given(commandLineOptions.getCurrencyCode()).willReturn(currencyCode);
    }

    private RatesXMLReader givenXmlReader(String fileId, CommandLineOptions commandLineOptions,
                                          BiConsumer<Double, Double> valuesConsumer) {
        return new RatesXMLReader(fileId, commandLineOptions, valuesConsumer);
    }
}