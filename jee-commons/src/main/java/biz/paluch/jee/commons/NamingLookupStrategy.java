package biz.paluch.jee.commons;

/**
 * Lookup strategy for object directories.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 */
public interface NamingLookupStrategy {

    /**
     * Lookup from the LookupStrategy.
     * 
     * @param name
     * @param <T>
     * @return Object, null or a RuntimeException, if not found. Depends on the underlying implementation.
     * @throws java.lang.RuntimeException if the object is not found.
     */
    <T> T doLookup(String name);
}
