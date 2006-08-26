package spin;


import spin.CGLibProxyFactory;
import spin.ProxyFactory;

public class CGLibProxyEqualityTest extends AbstractProxyFactoryTest {

    protected ProxyFactory getFactory() {
        return new CGLibProxyFactory();
    }
}