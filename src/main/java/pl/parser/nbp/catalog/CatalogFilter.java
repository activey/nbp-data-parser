package pl.parser.nbp.catalog;

import java.util.function.Predicate;

/**
 * @author activey
 */
public final class CatalogFilter {

    public static Predicate<String> sellAndBuyRatesOnly() {
        return directoryFileLine -> directoryFileLine.startsWith("c");
    }
}
