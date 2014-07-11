package biz.paluch.jee.commons.test.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;

import biz.paluch.jee.commons.test.LookupMocking;

/**
 * Rule to activate Mockito and lookup mocking (CDI and InitialContext/JNDI). Usage: <code>
 *   public class YourTest {
 * 
 * @Rule public LookupMockingRule lookupMockingRule = new LookupMockingRule(this);
 * 
 * @Mock(name = "java:ejb/YourEjbName") private SimpleDependency mock;
 * 
 * @Test public void testLookupInitialContext() throws Exception { assertSame(mock,
 *       InitialContext.doLookup("java:ejb/YourEjbName")); } } </code>
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.07.14 14:55
 */
public class LookupMockingRule implements TestRule {
    private Object target;

    public LookupMockingRule(Object target) {
        this.target = target;
    }

    @Override
    public Statement apply(final Statement base, Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                MockitoAnnotations.initMocks(target);
                LookupMocking.setup(target);
                base.evaluate();
                LookupMocking.cleanup();
            }
        };
    }
}
