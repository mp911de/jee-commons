package biz.paluch.jee.commons.test;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.internal.util.MockUtil;
import org.mockito.mock.MockName;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import biz.paluch.jee.commons.BeanLookupStrategy;
import biz.paluch.jee.commons.NamingLookupStrategy;

/**
 * Strategy which uses Mockito as source for mocks.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 31.03.14 10:03
 */
public class MockitoLookupStrategy implements NamingLookupStrategy, BeanLookupStrategy {

    private Object source;

    public MockitoLookupStrategy(Object source) {
        this.source = source;
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

        List<T> list = lookupBeans(null, name);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager unused, String beanName) {
        List result = new ArrayList();

        Map<String, Object> mocks = getMocksWithNames();
        if (mocks.containsKey(beanName)) {
            result.add(mocks.get(beanName));
        }

        return result;
    }

    /**
     * Register mocks with their names within the NamingContext.
     * 
     * @param contextBuilder
     */
    public void registerMocks(SimpleNamingContextBuilder contextBuilder) {
        for (Map.Entry<String, Object> entry : getMocksWithNames().entrySet()) {
            contextBuilder.bind(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager unused, Class<T> type, Annotation... qualifier) {

        Set<Object> mocks = getMocks();
        List result = new ArrayList();

        for (Object mock : mocks) {
            if (type.isAssignableFrom(mock.getClass())) {
                result.add(mock);
            }
        }
        return result;
    }

    private Map<String, Object> getMocksWithNames() {
        Map<String, Object> result = new HashMap<String, Object>();
        MockUtil mockUtil = new MockUtil();
        for (Object mock : getMocks()) {
            MockName mockName = mockUtil.getMockName(mock);
            result.put(mockName.toString(), mock);
        }

        return result;
    }

    private Set<Object> getMocks() {
        Class<?> clazz = source.getClass();
        Set<Object> mocks = newMockSafeHashSet();

        while (clazz != Object.class) {
            new MockScanner(source, clazz).addPreparedMocks(mocks);
            clazz = clazz.getSuperclass();
        }
        return mocks;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(getClass().getSimpleName());
        sb.append(" [source=").append(source);
        sb.append(", mocks=").append(getMocksWithNames());
        sb.append(']');
        return sb.toString();
    }
}
