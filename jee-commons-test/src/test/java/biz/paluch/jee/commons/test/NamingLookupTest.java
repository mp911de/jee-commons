package biz.paluch.jee.commons.test;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import biz.paluch.jee.commons.BeanLookup;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 14:32
 */
@RunWith(MockitoJUnitRunner.class)
public class NamingLookupTest {
    @Mock
    private SimpleDependency mock;

    @Before
    public void before() throws Exception {

        CdiMocking.setup(this);
    }

    @Test
    public void testTypeLookup() throws Exception {
        assertSame(mock, BeanLookup.lookupBean(null, SimpleDependency.class));
    }

    @Test
    public void testNameLookup() throws Exception {
        assertSame(mock, BeanLookup.lookupBean(null, "mock"));
    }
}
