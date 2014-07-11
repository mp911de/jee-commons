package biz.paluch.jee.commons.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import javax.naming.InitialContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import biz.paluch.jee.commons.NamingLookup;
import biz.paluch.jee.commons.test.rules.MockitoRule;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 08:15
 */
public class JndiMockingTest {

    @Rule
    public MockitoRule mockitoRule = new MockitoRule(this);

    @Mock(name = "java:ejb/MyMockedDependency")
    private SimpleDependency mock;

    @Before
    public void before() throws Exception {
        JndiMocking.setup(this);

        when(mock.anyMethod()).thenReturn(42);
    }

    @Test
    public void testNamingLookup() throws Exception {
        SimpleDependency dependency = NamingLookup.doLookup("java:ejb/MyMockedDependency");

        int result = dependency.anyMethod();

        assertEquals(42, result);

    }

    @Test
    public void testInitialContext() throws Exception {
        SimpleDependency dependency = InitialContext.doLookup("java:ejb/MyMockedDependency");

        int result = dependency.anyMethod();

        assertEquals(42, result);

    }

    @Test
    public void testManualBinding() throws Exception {

        SimpleDependency simpleDependency = Mockito.mock(SimpleDependency.class);

        SimpleNamingContextBuilder.getCurrentContextBuilder().bind("someName", simpleDependency);
        Object result = InitialContext.doLookup("someName");

        assertSame(simpleDependency, result);
        assertNotSame(mock, result);

    }
}
