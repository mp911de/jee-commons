package biz.paluch.jee.commons.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import biz.paluch.jee.commons.NamingLookup;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 08:44
 */
public class MapLookupTest {

    public static final String MOCK_NAME = "myMock";

    @Before
    public void before() throws Exception {
        SimpleDependency mock = Mockito.mock(SimpleDependency.class);
        MapLookupStrategy mapLookup = new MapLookupStrategy();
        mapLookup.add(MOCK_NAME, mock);

        NamingLookup.setNamingLookupStrategy(mapLookup);
    }

    @Test
    public void testLookup() throws Exception {
        Object result = NamingLookup.doLookup(MOCK_NAME);
        assertNotNull(result);

    }
}
