package spin.test;

import spin.CGLibProxyFactory;
import spin.ProxyFactory;
import spin.Spin;

public class CGLibProxyEqualityTest extends JDKProxyEqualityTest {

    private ProxyFactory defaultProxyFactory;
    
    protected void setUp() throws Exception {
        super.setUp();
    
        defaultProxyFactory = Spin.getDefaultProxyFactory();
        
        Spin.setDefaultProxyFactory(new CGLibProxyFactory());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        
        Spin.setDefaultProxyFactory(defaultProxyFactory);
    }    
}