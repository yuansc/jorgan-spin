package spin.test;

import junit.framework.TestCase;

import spin.Spin;

public class JDKProxyEqualityTest extends TestCase {

    public void testReflexive() throws Exception {
        
        Runnable runnable = new RunnableBean();
        
        Runnable proxy = (Runnable)Spin.off(runnable);

        assertTrue("equals() is not reflexive",
                   proxy.equals(proxy));
    }
    
    public void testTwoProxiesOfSameBeanAreEqual() throws Exception {
        
        Runnable runnable = new RunnableBean();

        Runnable proxy1 = (Runnable)Spin.off(runnable);
        Runnable proxy2 = (Runnable)Spin.off(runnable);
        
        assertTrue("two proxies of same bean are not equal",
                   proxy1.equals(proxy2));
    }
    
    public void testTwoProxiesOfDifferentBeansAreNotEqual() throws Exception {
        
        Runnable runnable1 = new RunnableBean();

        Runnable runnable2 = new RunnableBean();
        
        Runnable proxy1 = (Runnable)Spin.off(runnable1);
        Runnable proxy2 = (Runnable)Spin.off(runnable2);
        
        assertTrue("two proxies of different beans are equal",
                   !proxy1.equals(proxy2));
    }
    
    private static class RunnableBean implements Runnable {
        public void run() {
        }
    }
}
