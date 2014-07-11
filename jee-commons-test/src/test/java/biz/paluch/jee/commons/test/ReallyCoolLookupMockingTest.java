package biz.paluch.jee.commons.test;

import static org.junit.Assert.assertSame;

import javax.naming.InitialContext;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import biz.paluch.jee.commons.BeanLookup;
import biz.paluch.jee.commons.NamingLookup;
import biz.paluch.jee.commons.test.rules.LookupMockingRule;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 14:00
 */
public class ReallyCoolLookupMockingTest {
    public static final String JNDI_NAME = "java:ejb/MyMockedDependency";

    @Rule
    public LookupMockingRule lookupMockingRule = new LookupMockingRule(this);

    @Mock(name = JNDI_NAME)
    private SimpleDependency mock;

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

}
