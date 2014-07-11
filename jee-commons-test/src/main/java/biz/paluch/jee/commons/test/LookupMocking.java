package biz.paluch.jee.commons.test;

import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import biz.paluch.jee.commons.BeanLookup;

/**
 * Setup CDI and JNDI mocking.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 13:55
 */
public class LookupMocking {
    /**
     * Setup CDI and JNDI mocking.
     * 
     * @see biz.paluch.jee.commons.test.CdiMocking
     * @see biz.paluch.jee.commons.test.JndiMocking
     * @param source object containing the dependencies (mostly your test class object).
     */
    public static void setup(Object source) {
        CdiMocking.setup(source);

        SimpleNamingContextBuilder contextBuilder = JndiMocking.setup(source);
        contextBuilder.bind(BeanLookup.BEANMANAGER_JNDI_NAME, CdiMocking.beanManager(source));
    }

    /**
     * Cleanup the lookup mock setup to the initial (default) state.
     */
    public static void cleanup() {
        JndiMocking.cleanup();
        CdiMocking.cleanup();
    }
}
