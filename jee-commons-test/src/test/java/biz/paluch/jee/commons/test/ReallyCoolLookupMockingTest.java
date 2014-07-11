package biz.paluch.jee.commons.test;

import static org.junit.Assert.assertSame;

import java.util.Iterator;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
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
    public void testCdiLookupUsingBeanLookup() throws Exception {
        assertSame(mock, BeanLookup.lookupBean(SimpleDependency.class));
    }

    @Test
    public void testCdiLookupUsingBeanManager() throws Exception {
        BeanManager beanManager = InitialContext.doLookup("java:comp/BeanManager");
        Set<Bean<?>> beans = beanManager.getBeans(SimpleDependency.class);
        Iterator<Bean<?>> iterator = beans.iterator();

        Bean<?> bean = iterator.next();
        CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
        SimpleDependency dependency = (SimpleDependency) beanManager.getReference(bean, bean.getBeanClass(), ctx);
        assertSame(mock, dependency);
    }

}
