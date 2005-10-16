/**
 * Spin - transparent threading solution for non-freezing Swing applications.
 * Copyright (C) 2002 Sven Meier
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package spin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A factory of proxies utilizing JDK proxies.
 */
public class JDKProxyFactory implements ProxyFactory {

    public Object createProxy(Object object, Evaluator evaluator) {
        Class clazz = object.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                                      interfaces(clazz),
                                      new SpinInvocationHandler(object, evaluator));
    }
    
    /**
     * Utility method to retrieve all implemented interfaces of a class
     * and its superclasses.
     * 
     * @param clazz   class to get interfaces for
     * @return        implemented interfaces
     */
    private static Class[] interfaces(Class clazz) {
      Set interfaces = new HashSet();
      while (clazz != null) {
        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
        clazz = clazz.getSuperclass();
      }

      return (Class[])interfaces.toArray(new Class[interfaces.size()]);
    }

    /**
     * Test if the given object is a <em>Spin</em> proxy.
     * 
     * @param object    object to test
     * @return          <code>true</code> if given object is a <em>Spin</em> proxy,
     *                  <code>false</code> otherwise
     */
    public static boolean isSpinProxy(Object object) {
      
      if (object == null) {
          return false;
      }
      
      if (!Proxy.isProxyClass(object.getClass())) {
          return false;
      }
      
      return (Proxy.getInvocationHandler(object) instanceof SpinInvocationHandler); 
    }
    
    /**
     * Invocation handler for the <em>Spin</em> proxy.
     */
    private class SpinInvocationHandler implements InvocationHandler {

      private Object object;
      private Evaluator evaluator;
      
      public SpinInvocationHandler(Object object, Evaluator evaluator) {
          this.object  = object;
          this.evaluator = evaluator;
      }
      /**
       * Handle the invocation of a method on the <em>Spin</em> proxy.
       *
       * @param proxy      the proxy instance
       * @param method     the method to invoke
       * @param args       the arguments for the method
       * @return           the result of the invocation on the wrapped object
       * @throws Throwable if the wrapped method throws a <code>Throwable</code>
       */
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    
        if (Invocation.isEqualsMethod(method)) {
          return new Boolean(isSpinProxy(args[0]) &&
                             equals(Proxy.getInvocationHandler(args[0])));
        } else {
          Invocation invocation = new Invocation();
          invocation.setObject(object);
          invocation.setMethod(method);
          invocation.setArguments(args);
          
          evaluator.evaluate(invocation);
          
          return invocation.resultOrThrow();
        }
      }
      
      /**
       * Test on equality based on the wrapped object.
       */
      public boolean equals(Object object) {
        if (object != null && object.getClass().equals(getClass())) {
          SpinInvocationHandler handler = (SpinInvocationHandler)object;
          return (this.object.equals(handler.object));
        }
        return false;
      }      
    } 
}