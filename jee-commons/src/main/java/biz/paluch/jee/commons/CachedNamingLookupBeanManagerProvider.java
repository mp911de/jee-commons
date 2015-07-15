package biz.paluch.jee.commons;

import javax.enterprise.inject.spi.BeanManager;
/**
 * Strategy for providing a bean manager. This strategy tries by default some
 * JNDI Lookups for obtaining a bean manager.
 * 
 * @author <a href="mailto:mail@pfink.de">Patrick Fink</a>
 */
public class CachedNamingLookupBeanManagerProvider implements BeanManagerProvider {
    /**
     * Default JNDI names of the BeanManager.
     */
    public static final String[] BEANMANAGER_NAMES = {"java:comp/BeanManager", "java:comp/env/BeanManager"};
    
    private BeanManager beanManager;
    
    @Override
    public BeanManager getBeanManager() {
        if(beanManager == null)
            lookupBeanManager();
        return beanManager;
    }
    
    private void lookupBeanManager() {
        IllegalArgumentException noBeanManagerFound = null;
        for(String beanManagerName : BEANMANAGER_NAMES) {
            try {
                beanManager = NamingLookup.doLookup(beanManagerName);
            } catch(Exception ex) {
                if(noBeanManagerFound == null) {
                   noBeanManagerFound = new IllegalArgumentException("Did not found any BeanManager from "+BEANMANAGER_NAMES, ex);
                }
                else {
                   noBeanManagerFound.addSuppressed(ex);
                }                
            }
        }
        if(beanManager == null)
            throw noBeanManagerFound;
    }
}
