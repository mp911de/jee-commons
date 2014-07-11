package biz.paluch.jee.commons.test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

import org.mockito.internal.util.MockUtil;

import biz.paluch.jee.commons.BeanLookupStrategy;

/**
 * Lookup strategy for looking up beans which are backed by a list. The selection is based on the requested type.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.07.13 13:47
 */
public class ListLookupStrategy implements BeanLookupStrategy {

    private List<Object> objects = new ArrayList<Object>();

    public ListLookupStrategy(List<? extends Object> objects) {
        this.objects.addAll(objects);
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager source, Class<T> type, Annotation... qualifier) {
        List result = new ArrayList();

        for (Object object : objects) {
            if (type.isAssignableFrom(object.getClass())) {
                result.add(object);
            }
        }
        return result;
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager source, String beanName) {
        List result = new ArrayList();

        MockUtil mockUtil = new MockUtil();
        for (Object object : objects) {
            if (beanName.equals(mockUtil.getMockName(object))) {
                result.add(object);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(getClass().getSimpleName());
        sb.append(" [objects=").append(objects);
        sb.append(']');
        return sb.toString();
    }
}
