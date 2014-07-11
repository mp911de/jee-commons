package biz.paluch.jee.commons.test;

import static org.junit.Assert.assertSame;

import javax.naming.InitialContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import biz.paluch.jee.commons.BeanLookup;
import biz.paluch.jee.commons.NamingLookup;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 14:00
 */
@RunWith(MockitoJUnitRunner.class)
public class LookupMockingTest {
    public static final String JNDI_NAME = "java:ejb/MyMockedDependency";

    @Mock(name = JNDI_NAME)
    private SimpleDependency mock;

    @Before
    public void before() throws Exception {
        LookupMocking.setup(this);
    }

    @After
    public void after() throws Exception {
        LookupMocking.cleanup();
    }

    @Test
    public void testLookupInitialContext() throws Exception {
        assertSame(mock, InitialContext.doLookup(JNDI_NAME));
    }

    @Test
    public void testNamingLookup() throws Exception {
        Object o = NamingLookup.doLookup(JNDI_NAME);
        assertSame(mock, o);
    }

    @Test
    public void testCdiLookup() throws Exception {
        assertSame(mock, BeanLookup.lookupBean(BeanLookup.beanManager(), SimpleDependency.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterCleanup() throws Exception {
        LookupMocking.cleanup();
        BeanLookup.beanManager();
    }
}
