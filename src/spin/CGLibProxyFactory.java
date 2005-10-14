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

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * A factory of proxies utilizing CGLib.
 */
public class CGLibProxyFactory implements ProxyFactory {

    public Object createProxy(Object object, Spinner spinner) {
        return Enhancer.create(object.getClass(), new SpinMethodInterceptor(object, spinner));
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
        
        if (!(object instanceof Factory)) {
            return false;
        }

        Factory factory = (Factory)object;
        return (factory.getCallback(0) instanceof SpinMethodInterceptor);
    }    
    
    /**
     * Abstract base class for handlers of invocations on the <em>Spin</em> proxy.
     */
    private class SpinMethodInterceptor implements MethodInterceptor {

      private Object object;
      private Spinner spinner;
      
      public SpinMethodInterceptor(Object object, Spinner spinner) {
          this.object  = object;
          this.spinner = spinner;
      }

      public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
          if (Invocation.isEqualsMethod(method)) {
              return new Boolean(isSpinProxy(args[0]) &&
                                 equals(((Factory)args[0]).getCallback(0)));
          }
          Invocation invocation = new Invocation();
          invocation.setObject(this.object);
          invocation.setMethod(method);
          invocation.setArguments(args);
          
          spinner.spin(invocation);
          
          return invocation.resultOrThrow();
      }
      
      /**
       * Test on equality based on the wrapped object.
       */
      public boolean equals(Object object) {
        if (object != null && object.getClass().equals(getClass())) {
          SpinMethodInterceptor interceptor = (SpinMethodInterceptor)object;
          return (this.object.equals(interceptor.object));
        }
        return false;
      }      
    } 
}