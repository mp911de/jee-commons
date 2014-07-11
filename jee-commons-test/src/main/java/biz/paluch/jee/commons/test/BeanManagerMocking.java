package biz.paluch.jee.commons.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import biz.paluch.jee.commons.BeanLookupStrategy;

/**
 * Mocking for a CDI {@link javax.enterprise.inject.spi.BeanManager}.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 13:17
 */
public class BeanManagerMocking {
    private BeanLookupStrategy beanLookupStrategy;
    private BeanManager beanManager;
    private List<Tuple<Object, Bean<?>>> beans = Collections.synchronizedList(new ArrayList<Tuple<Object, Bean<?>>>());

    private static CreationalContext dummy = new CreationalContext() {
        @Override
        public void push(Object incompleteInstance) {

        }

        @Override
        public void release() {

        }
    };

    public BeanManagerMocking(BeanLookupStrategy strategy) {
        this.beanLookupStrategy = strategy;

        beanManager = Mockito.mock(BeanManager.class);

        when(beanManager.getBeans(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Set<Bean<?>> result = new HashSet<Bean<?>>();

                String name = (String) invocationOnMock.getArguments()[0];

                List<Object> objects = beanLookupStrategy.lookupBeans(null, name);

                setupBeans(result, name, objects);

                return result;
            }
        });

        when(beanManager.getBeans(any(Type.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Set<Bean<?>> result = new HashSet<Bean<?>>();

                Class<?> classType = (Class<?>) invocationOnMock.getArguments()[0];

                List<?> objects = beanLookupStrategy.lookupBeans(null, classType);

                setupBeans(result, null, objects);

                return result;
            }
        });

        when(beanManager.getBeans(any(Type.class), any(Annotation[].class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Set<Bean<?>> result = new HashSet<Bean<?>>();

                Class<?> classType = (Class<?>) invocationOnMock.getArguments()[0];
                Annotation[] annotations = (Annotation[]) invocationOnMock.getArguments()[1];

                List<?> objects = beanLookupStrategy.lookupBeans(null, classType, annotations);

                setupBeans(result, null, objects);

                return result;
            }
        });
    }

    /**
     * 
     * @return the mocked {@link javax.enterprise.inject.spi.BeanManager}.
     */
    public BeanManager getBeanManager() {
        return beanManager;
    }

    private void setupBeans(Set<Bean<?>> result, String name, List<? extends Object> objects) {
        for (Object object : objects) {
            Bean bean = getBean(object);

            if (bean == null) {
                bean = mockObject(object, name);
                beans.add(new Tuple<Object, Bean<?>>(object, bean));

                when(beanManager.createCreationalContext(bean)).thenReturn(dummy);
                when(beanManager.getReference(bean, bean.getBeanClass(), dummy)).thenReturn(object);
            }

            result.add(bean);
        }
    }

    private Bean getBean(Object object) {

        for (Tuple<Object, Bean<?>> bean : beans) {
            if (bean.key == object) {
                return bean.value;
            }
        }
        return null;
    }

    private Bean mockObject(Object object, String name) {
        Bean bean = Mockito.mock(Bean.class);

        when(bean.getBeanClass()).thenReturn(object.getClass());
        when(bean.getName()).thenReturn(name);
        when(bean.create(any(CreationalContext.class))).thenReturn(object);
        return bean;
    }

    private static class CreateAnswer implements Answer<Object> {
        private Object instance;

        public CreateAnswer(Object instance) {
            this.instance = instance;
        }

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {

            return instance;
        }
    }

    private static class Tuple<K, V> {
        public K key;
        public V value;

        public Tuple(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
