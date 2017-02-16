package pl.parser.nbp.command;

import java.time.LocalDate;

/**
 * @author activey
 */
public class CommandLineOptions {

    private final String currencyCode;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public CommandLineOptions(String currencyCode, LocalDate startDate, LocalDate endDate) {
        this.currencyCode = currencyCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
