package spin.test;

import junit.framework.TestCase;

import spin.Spin;

public class ProxyEqualityTest extends TestCase {

    public void testReflexive() throws Exception {
        
        Runnable runnable = new Runnable() {
          public void run() {
          }
        };
        
        Runnable proxy = (Runnable)Spin.off(runnable);
        
        assertTrue("equals() is not reflexive",
                   proxy.equals(proxy));
    }
    
    public void testTwoProxiesOfSameBeanAreEqual() throws Exception {
        
        Runnable runnable = new Runnable() {
          public void run() {
          }
        };

        Runnable proxy1 = (Runnable)Spin.off(runnable);
        Runnable proxy2 = (Runnable)Spin.off(runnable);
        
        assertTrue("two proxies of same bean are not equal",
                   proxy1.equals(proxy2));
    }
    
    public void testTwoProxiesOfDifferentBeansAreNotEqual() throws Exception {
        
        Runnable runnable1 = new Runnable() {
          public void run() {
          }
        };

        Runnable runnable2 = new Runnable() {
          public void run() {
          }
        };
        
        Runnable proxy1 = (Runnable)Spin.off(runnable1);
        Runnable proxy2 = (Runnable)Spin.off(runnable2);
        
        assertTrue("two proxies of different beans are equal",
                   !proxy1.equals(proxy2));
    }
}
