package spin;


import spin.JDKProxyFactory;
import spin.ProxyFactory;

public class JDKProxyEqualityTest extends AbstractProxyFactoryTest {

    protected ProxyFactory getFactory() {
        return new JDKProxyFactory();
    }
}