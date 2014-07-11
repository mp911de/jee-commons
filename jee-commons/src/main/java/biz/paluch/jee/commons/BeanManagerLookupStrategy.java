package biz.paluch.jee.commons;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

/**
 * Lookup strategy using the CDI BeanManager.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 23.07.13 13:40
 */
public class BeanManagerLookupStrategy implements BeanLookupStrategy {

    /**
     * Lookup a bean using its class.
     * 
     * @param source
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> lookupBeans(BeanManager source, Class<T> type, Annotation... qualifier) {
        Iterator<Bean<?>> iter = source.getBeans(type, qualifier).iterator();

        return retrieveBeans(source, iter, type.getName());
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager source, String beanName) {
        Iterator<Bean<?>> iter = source.getBeans(beanName).iterator();
        return retrieveBeans(source, iter, beanName);
    }

    private <T> List<T> retrieveBeans(BeanManager source, Iterator<Bean<?>> iter, Object beanIdentifier) {
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI bean manager has no instance of requested bean: " + beanIdentifier);
        }

        List<T> beans = new ArrayList<T>();
        while (iter.hasNext()) {
            Bean<?> bean = iter.next();
            CreationalContext<?> ctx = source.createCreationalContext(bean);
            T beanInstance = (T) source.getReference(bean, bean.getBeanClass(), ctx);
            beans.add(beanInstance);
        }
        return beans;
    }
}
