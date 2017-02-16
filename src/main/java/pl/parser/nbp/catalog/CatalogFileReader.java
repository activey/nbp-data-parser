package pl.parser.nbp.catalog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * Class is responsible for reading contents of a directory file for given year.
 *
 * @author activey
 */
public class CatalogFileReader {

    private static final String CATALOG_URL_TEMPLATE = "http://www.nbp.pl/kursy/xml/dir%s.txt";

    private int year;

    public CatalogFileReader(int year) {
        this.year = year;
    }

    public List<String> filter(Predicate<String> fileLineFilter) throws IOException {
        List<String> catalogFileLines = new ArrayList<>();
        Scanner directoryFileReader = new Scanner(getCatalogFileUrl().openStream());
        while (directoryFileReader.hasNextLine()) {
            String directoryLine = directoryFileReader.nextLine();
            if (fileLineFilter.test(directoryLine)) {
                catalogFileLines.add(directoryLine);
            }
        }
        return catalogFileLines;
    }

    private URL getCatalogFileUrl() throws MalformedURLException {
        // if it is for current year
        if (year == LocalDate.now().getYear()) {
            new URL(format(CATALOG_URL_TEMPLATE, ""));
        }
        return new URL(format(CATALOG_URL_TEMPLATE, year));
    }

}
