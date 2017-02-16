package pl.parser.nbp;

import org.junit.Before;

import static java.lang.System.getProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assume.assumeThat;

/**
 * @author activey
 */
public class BaseIntegrationTest extends BaseUnitTest {

    @Before

    public void before() {
        assumeThat(getProperty("integrationTests"), is(equalTo(Boolean.TRUE.toString())));
    }
}
