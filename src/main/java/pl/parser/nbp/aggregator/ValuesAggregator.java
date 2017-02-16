package pl.parser.nbp.aggregator;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import static java.util.Arrays.copyOf;

/**
 * @author activey
 */
public class ValuesAggregator {

    private int buySamples;
    private double totalBuyValue;

    private double[] sellValues = new double[0];

    public void useBuyValue(double buyValue) {
        buySamples++;
        totalBuyValue = totalBuyValue + buyValue;
    }

    public void useSellValue(double sellValue) {
        sellValues = copyOf(sellValues, sellValues.length + 1);
        sellValues[sellValues.length - 1] = sellValue;
    }

    public double buyValueAverage() {
        return totalBuyValue / buySamples;
    }

    public double sellValueDeviation() {
        StandardDeviation deviationEvaluator = new StandardDeviation(false);
        return deviationEvaluator.evaluate(sellValues);
    }
}
