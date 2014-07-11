package biz.paluch.jee.commons.test;

import java.util.Map;

import javax.naming.NamingException;

import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import biz.paluch.jee.commons.NamingLookup;
import biz.paluch.jee.commons.NamingLookupStrategy;

/**
 * Setup Mocking for Jndi Naming Lookup. This is a setup (bootstrap) class to initiate mocking using predefines maps or using
 * dependencies from a object.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.07.13 13:45
 */
public class JndiMocking {

    /**
     * Build a {@link CompositeLookupStrategy} using a list of items.
     * 
     * @param items
     * @return CompositeLookupStrategy containing the ListLookupStrategy
     */
    protected static CompositeLookupStrategy build(Map<String, ?> items) {
        CompositeLookupStrategy result = new CompositeLookupStrategy();
        result.add(new MapLookupStrategy((Map) items));
        return result;
    }

    /**
     * Setup the environment ({@link NamingLookup}) for lookup using a map of items.
     * 
     * @param items map containing a mapping between name and the object
     * @return NamingLookupStrategy
     */
    public static NamingLookupStrategy setup(Map<String, ?> items) {
        NamingLookupStrategy result = build(items);
        NamingLookup.setNamingLookupStrategy(result);
        return result;
    }

    /**
     * Build a {@link CompositeLookupStrategy} using the class fields and Mockito mocks for lookup.
     * 
     * @param source object containing the dependencies (mostly your test class object).
     * @return CompositeLookupStrategy
     */
    protected static CompositeLookupStrategy build(Object source) {
        CompositeLookupStrategy result = new CompositeLookupStrategy();
        result.add((NamingLookupStrategy) new MockitoLookupStrategy(source));
        return result;
    }

    protected static NamingLookupStrategy build(Object source, SimpleNamingContextBuilder contextBuilder) {
        new MockitoLookupStrategy(source).registerMocks(contextBuilder);
        return build(source);
    }

    /**
     * Build a {@link SimpleNamingContextBuilder} using the class fields and Mockito mocks for lookup.
     * 
     * @param source object containing the dependencies (mostly your test class object).
     * @return activated {@link SimpleNamingContextBuilder}
     */
    public static SimpleNamingContextBuilder setup(Object source) {
        try {
            SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            build(source, contextBuilder);
            return contextBuilder;
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Cleanup the lookup mock setup to the initial (default) state.
     */
    public static void cleanup() {
        NamingLookup.reset();

        SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.getCurrentContextBuilder();
        if (contextBuilder != null) {
            contextBuilder.clear();
        }
    }
}
