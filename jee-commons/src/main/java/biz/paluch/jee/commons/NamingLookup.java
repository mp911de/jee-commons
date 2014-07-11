package biz.paluch.jee.commons;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 02.04.14 10:55
 */
public class NamingLookup {

    /**
     * defaults to JndiLookupStrategy.
     */
    private static NamingLookupStrategy namingLookupStrategy = new JndiLookupStrategy();

    /**
     * 
     * @return Object, null or a RuntimeException, if not found. Depends on the underlying implementation.
     * @throws java.lang.RuntimeException if the object is not found.
     */
    public static <T> T doLookup(String name) {
        return namingLookupStrategy.doLookup(name);
    }

    /**
     * Set a lookup strategy.
     * 
     * @param namingLookupStrategy
     */
    public static void setNamingLookupStrategy(NamingLookupStrategy namingLookupStrategy) {
        NamingLookup.namingLookupStrategy = namingLookupStrategy;
    }

    /**
     * Reset the lookup strategy to default.
     */
    public static void reset() {
        namingLookupStrategy = new JndiLookupStrategy();
    }
}
