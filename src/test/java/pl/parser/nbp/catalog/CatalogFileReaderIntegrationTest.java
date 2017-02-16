package pl.parser.nbp.catalog;

import org.junit.Test;
import org.mockito.Mock;
import pl.parser.nbp.BaseIntegrationTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author activey
 */
public class CatalogFileReaderIntegrationTest extends BaseIntegrationTest {

    @Mock
    private Predicate<String> dummyPredicate;

    @Test
    public void shouldThrowAnExceptionWhenCatalogForGivenYearDoesNotExist() throws IOException {
        // given
        CatalogFileReader reader = new CatalogFileReader(1540);

        // then
        expectedException.expect(FileNotFoundException.class);

        // when
        reader.filter(dummyPredicate);
    }

    @Test
    public void shouldNotThrowAnExceptionWhenCatalogForGivenYearExists() throws IOException {
        // given
        CatalogFileReader reader = new CatalogFileReader(2013);

        // then
        // expect no exception

        // when
        reader.filter(catalogLine -> true);
    }

    @Test
    public void shouldFilterCatalogLinesAccordingToGivenPredicate() throws IOException {
        // given
        CatalogFileReader reader = new CatalogFileReader(2013);

        // when
        List<String> filteredLines = reader.filter(catalogLine -> catalogLine.startsWith("c"));

        // then
        assertThat(filteredLines.stream().anyMatch(line -> !line.startsWith("c"))).isFalse();
    }
}