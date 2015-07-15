package biz.paluch.jee.commons;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

/**
 * Utility to lookup beans.
 */
public final class BeanLookup {
    /**
     * Default lookup strategy is using the BeanManager directly.
     */
    private static BeanLookupStrategy lookupStrategy = new BeanManagerLookupStrategy();
    /**
     * Default strategy for obtaining the bean manager.
     */
    private static BeanManagerProvider beanManagerProvider = new CachedNamingLookupBeanManagerProvider();

    private BeanLookup() {
    }

    /**
     * Lookup Beans using its class.
     * 
     * @param beanManager
     * @param type
     * @return List of beans (or empty list)
     */
    public static <T> List<T> lookupBeans(BeanManager beanManager, Class<T> type) {
        return lookupStrategy.lookupBeans(beanManager, type);
    }

    /**
     * Lookup Bean using its class.
     * 
     * @param beanManager
     * @param type
     * @return The bean.
     * @throws IllegalStateException in case no unique Bean is found.
     */
    public static <T> T lookupBean(BeanManager beanManager, Class<T> type) {
        List<T> beans = lookupBeans(beanManager, type);
        return returnOneBean(type, beans);
    }

    /**
     * Lookup Bean using its class.
     * 
     * @param type
     * @return The bean.
     * @throws IllegalStateException in case no unique Bean is found.
     */
    public static <T> T lookupBean(Class<T> type) {
        return lookupBean(beanManager(), type);
    }

    /**
     * Lookup Beans using the Bean-Name.
     * 
     * @param beanManager
     * @param beanName
     * @return List of beans (or empty list)
     */
    public static <T> List<T> lookupBeans(BeanManager beanManager, String beanName) {
        return lookupStrategy.lookupBeans(beanManager, beanName);
    }

    /**
     * Lookup Bean using the Bean-Name.
     * 
     * @param beanManager
     * @param beanName
     * @param <T>
     * @return The bean or IllegalStateException.
     * @throws IllegalStateException in case no unique Bean is found.
     */
    public static <T> T lookupBean(BeanManager beanManager, String beanName) {
        List<T> beans = lookupBeans(beanManager, beanName);
        return returnOneBean(beanName, beans);
    }

    /**
     * Lookup Bean using the Bean-Name.
     * 
     * @param beanName
     * @param <T>
     * @return The bean or IllegalStateException.
     * @throws IllegalStateException in case no unique Bean is found.
     */
    public static <T> T lookupBean(String beanName) {
        return lookupBean(beanManager(), beanName);
    }

    private static <T> T returnOneBean(Object beanIdentifier, List<T> beans) {
        if (beans.isEmpty()) {
            throw new IllegalStateException("bean " + beanIdentifier + " not found");
        } else if (beans.size() > 1) {
            throw new IllegalStateException("found multiple beans <" + beanIdentifier + ">");
        }

        return beans.get(0);
    }

    /**
     * 
     * @return the current lookupStrategy.
     */
    public static BeanLookupStrategy getLookupStrategy() {
        return lookupStrategy;
    }

    /**
     * Set a new lookup strategy.
     * 
     * @param strategy
     */
    public static void setLookupStrategy(BeanLookupStrategy strategy) {
        BeanLookup.lookupStrategy = strategy;
    }
    
    public static BeanManagerProvider getBeanManagerProvider() {
        return beanManagerProvider;
    }

    public static void setBeanManagerProvider(BeanManagerProvider beanManagerProvider) {
        BeanLookup.beanManagerProvider = beanManagerProvider;
    }

    /**
     * Lookup the BeanManager using {@code NamingLookup}
     * 
     * @return the BeanManager
     */
    public static BeanManager beanManager() {        
        return beanManagerProvider.getBeanManager();
    }
 
    /**
     * Reset the lookup strategy to default.
     */
    public static void reset() {
        lookupStrategy = new BeanManagerLookupStrategy();
    }
}
