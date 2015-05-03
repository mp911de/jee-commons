package biz.paluch.jee.commons;

import javax.enterprise.inject.spi.BeanManager;

/**
 * Strategy Interface for providing a bean manager.
 * 
 * @author <a href="mailto:mail@pfink.de">Patrick Fink</a>
 */
public interface BeanManagerProvider {
    public BeanManager getBeanManager();
}
