package pl.parser.nbp;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import pl.parser.nbp.aggregator.ValuesAggregator;
import pl.parser.nbp.command.CommandLineOptions;
import pl.parser.nbp.command.CommandLineOptionsParser;
import pl.parser.nbp.command.CommandLineParsingException;
import pl.parser.nbp.catalog.CatalogFileReader;
import pl.parser.nbp.xml.RatesXMLReader;

import java.io.IOException;
import java.time.Period;

import static java.lang.String.format;
import static pl.parser.nbp.catalog.CatalogFilter.sellAndBuyRatesOnly;

/**
 * @author activey
 */
public class MainClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainClass.class);

    private ValuesAggregator aggregator;

    static {
        BasicConfigurator.configure();
    }

    public MainClass() {
        aggregator = new ValuesAggregator();
    }

    public static void main(String... args) throws CommandLineParsingException {
        // parse input
        CommandLineOptions options = new CommandLineOptionsParser().parseCommandLineOptions(args);
        // computing and aggregating
        try {
            new MainClass().computeAndAggregateValues(options);
        } catch (IOException e) {
            LOGGER.error("An error has occurred while aggregating values!", e);
        }
    }

    private void computeAndAggregateValues(CommandLineOptions options) throws IOException {
        // count all years for dates range
        int startYear = options.getStartDate().getYear();
        int yearsBetween = Period.between(options.getStartDate(), options.getEndDate()).getYears();

        // iterating by XML catalogs for that years
        for (int year = startYear; year <= startYear + yearsBetween; year++) {
            CatalogFileReader catalogReader = new CatalogFileReader(year);
            // filtering by sell and buy rates only
            catalogReader.filter(sellAndBuyRatesOnly())
                .forEach(catalogEntry -> processCatalogEntry(catalogEntry, options));
        }

        // printing out results
        System.out.println(roundToFourPlaces(aggregator.buyValueAverage()));
        System.out.println(roundToFourPlaces(aggregator.sellValueDeviation()));
    }

    private String roundToFourPlaces(double doubleValue) {
        return format("%.4f", doubleValue);
    }

    private void processCatalogEntry(String catalogEntry, CommandLineOptions options) {
        try {
            new RatesXMLReader(catalogEntry, options, (sell, buy) -> {
                aggregator.useSellValue(sell);
                aggregator.useBuyValue(buy);
            }).readRates();
        } catch (IOException | SAXException e) {
            LOGGER.error("An error has occurred while reading catalog entry!", e);
        }
    }
}
