package pl.parser.nbp.aggregator;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author activey
 */
public class ValuesAggregatorTest {

    @Test
    public void shouldGatherAllSellValuesInArray() {
        // given
        ValuesAggregator aggregator = new ValuesAggregator();

        // when
        aggregator.useSellValue(1);
        aggregator.useSellValue(2);
        aggregator.useSellValue(3);
        aggregator.useSellValue(4);
        aggregator.useSellValue(5);

        // then
        assertThat(aggregator.getSellValues()).contains(1, 2, 3, 4, 5);
    }
}