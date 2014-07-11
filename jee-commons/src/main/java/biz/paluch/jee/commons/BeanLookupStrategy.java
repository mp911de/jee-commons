package biz.paluch.jee.commons;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

/**
 * Strategy for bean lookup using the CDI bean manager.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 01.07.14 10:03
 */
public interface BeanLookupStrategy {

    /**
     * Lookup beans by class name.
     * 
     * @param beanManager
     * @param type
     * @param qualifier
     * @param <T>
     * @return List of Beans
     */
    <T> List<T> lookupBeans(BeanManager beanManager, Class<T> type, Annotation... qualifier);

    /**
     * Lookup beans by bean name.
     * 
     * @param beanManager
     * @param beanName
     * @param <T>
     * @return
     */
    <T> List<T> lookupBeans(BeanManager beanManager, String beanName);
}
