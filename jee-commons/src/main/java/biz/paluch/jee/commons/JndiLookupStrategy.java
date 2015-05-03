package biz.paluch.jee.commons;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Lookup strategy which performs lookups within the InitialContext. If a object is not found, a IllegalArgumentException (wraps
 * NamingException) is thrown.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 31.03.14 10:03
 */
public class JndiLookupStrategy implements NamingLookupStrategy {

    @Override
    public <T> T doLookup(String name) {
        try {
            return InitialContext.doLookup(name);
        } catch (NamingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
