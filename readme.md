jee-commons
===============

jee-commons and jee-commons-test are two libraries for improved handling within Java Enterprise applications.

Both libraries cover encapsulation and add convenience in using enterprise resources like JNDI (`InitialContext`) and CDI (`BeanManager`)



Download
--------------

The packages are available as Maven artifacts

```xml
<dependency>
    <groupId>biz.paluch.jee.commons</groupId>
    <artifactId>jee-commons</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>biz.paluch.jee.commons</groupId>
    <artifactId>jee-commons-test</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>

```

or http://search.maven.org/remotecontent?filepath=biz/paluch/jee/commons/jee-commons-test/1.0-SNAPSHOT/jee-commons-test-1.0-SNAPSHOT.jar


## Quick start


### JNDI / Initial Context

The initial context depends on System or invocation-level configuration. Therefore has unit testing of the initial context a certain complexity. Building own context structures within tests or omitting those tests is a common pattern. Let's stop with this and have a common way to enable unit testing with the initial context.

```java
@RunWith(MockitoJUnitRunner.class)
public class JndiMockingTest {

    @Mock(name = "java:ejb/MyMockedDependency")
    private SimpleDependency mock;

    @Before
    public void before() throws Exception {
        JndiMocking.setup(this);
    }

    @Test
    public void testNamingLookup() throws Exception {
        SimpleDependency dependency = NamingLookup.doLookup("java:ejb/MyMockedDependency");
        // your test code
    }

    @Test
    public void testInitialContext() throws Exception {
        SimpleDependency dependency = InitialContext.doLookup("java:ejb/MyMockedDependency");
        // your test code
    }
}
```

The class `NamingLookup` is a simple wrapper containing a strategy pattern around the InitialContext. This allows to substitute the lookup strategy in test cases. In addition, `NamingLookup.doLookup` does not declare an exception, the inner `NamingException`which is thrown by `InitialContext.doLookup` is wrapped into a runtime exception.

### CDI Bean lookup

CDI is intended for dependency injection and providing of dependencies itself. In some rare cases you've got to lookup beans yourself (i.e. when you're decoupled from Injection).

```java
BeanManager beanManager = InitialContext.doLookup("java:comp/BeanManager");
Set<Bean<?>> beans = beanManager.getBeans(YourClassToLookup.class);
Iterator<Bean<?>> iterator = beans.iterator();

Bean<?> bean = iterator.next();
CreationalContext<?> ctx = beanManager.createCreationalContext(bean);

YourClassToLookup result = (YourClassToLookup) beanManager.getReference(bean, bean.getBeanClass(), ctx);
```

jee-commons provides a convenient way to call this lookup code so you don't need to build the code for every project you need manual bean lookup

```java
YourClassToLookup result = BeanLookup.lookupBean(YourClassToLookup.class);
```

or

```java
YourClassToLookup result = BeanLookup.lookupBean(BeanLookup.beanManager(), YourClassToLookup.class);
```

In addition to that, jee-commons-test provides a simple way of mocking the BeanManager.

### JEE Lookup mocking as Test-Rule

jee-commons-test provides a test rule to run your tests within a mocked environment. The rule `LookupMockingRule` enables you to run tests which need the `InitialContext` and the CDI `BeanManager`. The objects which are provided though any of these lookup mechanisms are taken from your class and the mocks which are declared within your test class.

The JNDI names are taken from the @Mock(name=...) annotation.

```java
public class ReallyCoolLookupMockingTest {

    @Rule
    public LookupMockingRule lookupMockingRule = new LookupMockingRule(this);

    @Mock(name = "java:ejb/MyMockedDependency")
    private SimpleDependency mock;

    @Test
    public void testLookupInitialContext() throws Exception {
        assertSame(mock, InitialContext.doLookup("java:ejb/MyMockedDependency"));
    }

    @Test
    public void testNamingLookup() throws Exception {
        Object o = NamingLookup.doLookup("java:ejb/MyMockedDependency");
        assertSame(mock, o);
    }

    @Test
    public void testCdiLookupUsingBeanLookup() throws Exception {
        assertSame(mock, BeanLookup.lookupBean(SimpleDependency.class));
    }

    @Test
    public void testCdiLookupUsingBeanManager() throws Exception {
        BeanManager beanManager = InitialContext.doLookup("java:comp/BeanManager");
        Set<Bean<?>> beans = beanManager.getBeans(SimpleDependency.class);
        Iterator<Bean<?>> iterator = beans.iterator();

        Bean<?> bean = iterator.next();
        CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
        SimpleDependency dependency = (SimpleDependency) beanManager.getReference(bean, bean.getBeanClass(), ctx);
        assertSame(mock, dependency);
    }

}

```


### Mockito as Test-Rule

Mockito provides a JUnit test runner for unit tests, that use Mockito annotations. jee-commons-test enables Mockito to be used as test rule (@Rule). This is handy in cases, where you need a specific test runner but still need Mockito.


```java
public class YourTest {
    @Rule
    public MockitoRule mockitoRule = new MockitoRule(this);

    @Mock
    private SimpleDependency mock;

    @Test
    public void test() throws Exception {
        ...
    }
}
```

instead of

```java
@RunWith(MockitoJUnitRunner.class)
public class YourTest {

    @Mock
    private SimpleDependency mock;

    @Test
    public void test() throws Exception {
        ...
    }
}
```

License
-------
* [The MIT License (MIT)] (http://opensource.org/licenses/MIT)

Contributing
-------
Github is for social coding: if you want to write code, I encourage contributions through pull requests from forks of this repository.
Create Github tickets for bugs and new features and comment on the ones that you are interested in.
