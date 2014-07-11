package biz.paluch.jee.commons.test;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

import biz.paluch.jee.commons.BeanLookup;
import biz.paluch.jee.commons.BeanLookupStrategy;
import biz.paluch.jee.commons.NamingLookupStrategy;

/**
 * Setup Mocks for CDI Bean-Lookup (Bean-Lookup done by BeanHelper).
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.07.13 13:45
 */
public class CdiMocking {
    /**
     * Build a {@link CompositeLookupStrategy} using a list of items.
     * 
     * @param items
     * @return CompositeLookupStrategy containing the ListLookupStrategy
     */
    protected static CompositeLookupStrategy build(List<?> items) {
        CompositeLookupStrategy result = new CompositeLookupStrategy();
        result.add(new ListLookupStrategy(items));
        return result;
    }

    /**
     * Setup the environment ({@link BeanLookup}) for lookup using a list of items.
     * 
     * @param items
     * @return BeanLookupStrategy
     */
    public static BeanLookupStrategy setup(List<?> items) {
        BeanLookupStrategy result = build(items);
        BeanLookup.setLookupStrategy(result);
        return result;
    }

    /**
     * Build a {@link CompositeLookupStrategy} using the class fields and Mockito mocks for lookup.
     * 
     * @param source object containing the dependencies (mostly your test class object).
     * @return CompositeLookupStrategy containing the ListLookupStrategy
     */
    protected static CompositeLookupStrategy build(Object source) {
        CompositeLookupStrategy result = new CompositeLookupStrategy();
        result.add(new ClassFieldsLookupStrategy(source));
        result.add((NamingLookupStrategy) new MockitoLookupStrategy(source));
        return result;
    }

    /**
     * Setup the environment ({@link BeanLookup}) for lookup using the class fields and Mockito mocks for lookup.
     * 
     * @param source object containing the dependencies (mostly your test class object).
     * @return BeanLookupStrategy
     */
    public static BeanLookupStrategy setup(Object source) {
        BeanLookupStrategy result = build(source);
        BeanLookup.setLookupStrategy(result);
        return result;
    }

    /**
     * Create a mocked {@link javax.enterprise.inject.spi.BeanManager} for bean creation/lookup using the class fields and
     * Mockito mocks for lookup. Only lookup methods are implemented, other functionality (isXXX, validateInjectionPoint, etc.)
     * is not supported.
     * 
     * @param source object containing the dependencies (mostly your test class object).
     * @return BeanManager mock
     */
    public static BeanManager beanManager(Object source) {
        BeanManagerMocking mocking = new BeanManagerMocking(build(source));

        return mocking.getBeanManager();
    }

    /**
     * Cleanup the lookup mock setup to the initial (default) state.
     */
    public static void cleanup() {
        BeanLookup.reset();
    }
}
