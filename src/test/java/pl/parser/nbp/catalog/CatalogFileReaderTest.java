package pl.parser.nbp.catalog;

import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author activey
 */
public class CatalogFileReaderTest {

    @Test
    public void shouldUseYearValueInUrlWhenDifferentThanCurrentYear() throws MalformedURLException {
        // given
        CatalogFileReader reader = new CatalogFileReader(2013);

        // when
        URL fileUrl = reader.getCatalogFileUrl();

        // then
        assertThat(fileUrl.getPath()).isEqualTo("/kursy/xml/dir2013.txt");
    }

    @Test
    public void shouldNotUseAnyYearInUrlIfTheSameAsCurrentYear() throws MalformedURLException {
        // given
        CatalogFileReader reader = new CatalogFileReader(LocalDate.now().getYear());

        // when
        URL fileUrl = reader.getCatalogFileUrl();

        // then
        assertThat(fileUrl.getPath()).isEqualTo("/kursy/xml/dir.txt");
    }
}
