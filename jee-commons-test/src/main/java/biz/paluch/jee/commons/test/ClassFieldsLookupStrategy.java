package biz.paluch.jee.commons.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

import org.springframework.util.ReflectionUtils;

import biz.paluch.jee.commons.BeanLookupStrategy;

/**
 * Lookup-Strategy which takes a object and scans it fields. Every field, which matches the requested class type will be used as
 * return candidate.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 */
public class ClassFieldsLookupStrategy implements BeanLookupStrategy {

    private Object source;

    public ClassFieldsLookupStrategy(Object source) {
        this.source = source;
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager source, Class<T> type, Annotation... qualifier) {
        List<Object> sourceValues = getSourceValues();
        List<T> result = new ArrayList<T>();

        for (Object sourceValue : sourceValues) {
            if (type.isAssignableFrom(sourceValue.getClass())) {
                result.add((T) sourceValue);
            }
        }
        return result;
    }

    @Override
    public <T> List<T> lookupBeans(BeanManager source, String beanName) {
        return Collections.emptyList();
    }

    private List<Object> getSourceValues() {
        final List<Object> result = new ArrayList<Object>();
        ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {

            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    result.add(value);
                }
            }
        });

        return result;
    }
}
