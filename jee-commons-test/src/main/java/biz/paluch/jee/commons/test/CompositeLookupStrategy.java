package biz.paluch.jee.commons.test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import biz.paluch.jee.commons.BeanLookupStrategy;
import biz.paluch.jee.commons.NamingLookupStrategy;

/**
 * Composite lookup strategy which combines multiple lookup sources.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 31.03.14 10:03
 */
public class CompositeLookupStrategy implements NamingLookupStrategy, BeanLookupStrategy {

    private List strategies = new ArrayList();

    public boolean add(NamingLookupStrategy lookupStrategy) {
        return strategies.add(lookupStrategy);
    }

    public boolean add(BeanLookupStrategy lookupStrategy) {
        return strategies.add(lookupStrategy);
    }

    /**
     * Perform Lookup.
     * 
     * @param name
     * @param <T>
     * @return Object or NoSuchElementException, if not found.
     */
    @Override
    public <T> T doLookup(String name) {

        for (Object strategy : strategies) {
            if (strategy instanceof NamingLookupStrategy) {

                NamingLookupStrategy lookupStrategy = (NamingLookupStrategy) strategy;
                T result = lookupStrategy.doLookup(name);
                if (result != null) {
                    return result;
                }
            }
        }

        throw new NoSuchElementException("Lookup failed for " + name + " using strategies " + strategies);
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager source, Class<T> type, Annotation... qualifier) {
        Set set = new HashSet();
        for (Object strategy : strategies) {
            if (strategy instanceof BeanLookupStrategy) {
                BeanLookupStrategy lookupStrategy = (BeanLookupStrategy) strategy;
                set.addAll(lookupStrategy.lookupBeans(source, type, qualifier));
            }
        }

        return new ArrayList(set);
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager source, String beanName) {
        Set<T> set = new HashSet<T>();
        for (Object strategy : strategies) {
            if (strategy instanceof BeanLookupStrategy) {
                BeanLookupStrategy lookupStrategy = (BeanLookupStrategy) strategy;
                set.addAll(lookupStrategy.<T> lookupBeans(source, beanName));
            }
        }

        return new ArrayList<T>(set);
    }
}
