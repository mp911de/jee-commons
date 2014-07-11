package biz.paluch.jee.commons.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import biz.paluch.jee.commons.BeanManagerLookupStrategy;

@RunWith(MockitoJUnitRunner.class)
public class BeanManagerMockingTest {

    @Mock
    private SimpleDependency mock;

    private BeanManager sut = CdiMocking.beanManager(this);

    @Before
    public void before() throws Exception {
        CdiMocking.setup(this);
    }

    @Test
    public void testLookupByClass() throws Exception {

        List<SimpleDependency> result = new BeanManagerLookupStrategy().lookupBeans(sut, SimpleDependency.class);

        assertEquals(1, result.size());

    }

    @Test
    public void testLookupByName() throws Exception {
        List<SimpleDependency> result = new BeanManagerLookupStrategy().lookupBeans(sut, "mock");

        assertEquals(1, result.size());

    }
}
