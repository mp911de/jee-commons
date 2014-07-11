package biz.paluch.jee.commons.test;

import java.util.HashMap;
import java.util.Map;

import biz.paluch.jee.commons.NamingLookupStrategy;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.07.13 13:19
 */
public class MapLookupStrategy implements NamingLookupStrategy {

    private Map<String, Object> map = new HashMap<String, Object>();

    public MapLookupStrategy() {
    }

    public MapLookupStrategy(Map<String, Object> map) {
        map.putAll(map);
    }

    /**
     * Perform Lookup.
     * 
     * @param name
     * @param <T>
     * @return Object, null or exception, if not found.
     */
    @Override
    public <T> T doLookup(String name) {
        return (T) map.get(name);
    }

    /**
     * Adds an entry.
     * 
     * @param name
     * @param entry
     */
    public void add(String name, Object entry) {
        map.put(name, entry);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(getClass().getSimpleName());
        sb.append(" [map=").append(map);
        sb.append(']');
        return sb.toString();
    }
}
