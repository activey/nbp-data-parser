package pl.parser.nbp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

/**
 * @author activey
 */
public class BaseUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
    }
}
