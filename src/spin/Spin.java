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

import spin.off.AWTReflectDispatcherFactory;
import spin.off.DispatcherFactory;
import spin.off.OffInvocation;
import spin.off.SimpleStarter;
import spin.off.Starter;
import spin.over.OverInvocation;

/**
 * <p>
 * <em>Spin</em> offers a transparent threading solution for non-freezing Swing
 * applications.
 * </p>
 * <p>
 * Let <code>bean</code> be a reference to a non-visual (possibly multithreaded)
 * bean implementing the interface <code>Bean</code> whose methods have to be
 * called by a Swing component. You can avoid any freezing by using one line of
 * code:
 * <pre>
 *   bean = (Bean)Spin.off(bean); 
 * </pre>
 * Now each method call on <code>bean</code> is executed on a separate thread,
 * while the EDT is continuing to dispatch events. All return values or
 * exceptions are handled by <em>Spin</em> and transparently returned to the
 * calling method. 
 * </p>
 * <p>
 * For calls from other threads than the EDT to your Swing component you can use
 * the following (being <ode>XYListener</code> any interface your component
 * implements):  
 * <pre>
 *   bean.addXYListener((XYListener)Spin.over(component); 
 * </pre>
 * Now all required updates to your component (and/or its model) are
 * transparently excuted on the EDT.
 * </p>
 * <p>
 * For spin-off <em>Spin</em> uses implementations of two supporting interfaces
 * {@link spin.off.Starter} and {@link spin.off.DispatcherFactory}. These can be
 * specified on explicit construction of a <code>Spin</code> instance:
 * <pre>
 *   Spin spin = new Spin(bean, interceptor, starter, dispatcherFactory);
 *   bean = (Bean)spin.getProxy();
 * </pre>
 * Alternatively you can specify default implementations with the system properties
 * <code>spin.off.starter</code> and <code>spin.off.dispatcher.factory</code>
 * or with their corresponding static methods {@link #setDefaultOffStarter(Starter)}
 * and {@link #setDefaultOffDispatcherFactory(DispatcherFactory)}.
 * </p>
 * 
 * @see #off(Object)
 * @see #over(Object)
 * @see #Spin(Object, Interceptor)
 * @see #Spin(Object, Interceptor, Starter, DispatcherFactory)
 * @see spin.off.Starter
 * @see spin.off.DispatcherFactory
 */
public class Spin {

  /**
   * The system property <code>spin.over.interceptor</code> that specifies
   * the default interceptor for spin-over.
   */
  public static final String SPIN_OVER_INTERCEPTOR = "spin.off.interceptor";

  /**
   * The system property <code>spin.off.interceptor</code> that specifies
   * the default interceptor for spin-off.
   */
  public static final String SPIN_OFF_INTERCEPTOR = "spin.off.interceptor";

  /**
   * The system property <code>spin.off.starter</code> that specifies
   * the default starter for spin-off.
   */
  public static final String SPIN_OFF_STARTER = "spin.off.starter";

  /**
   * The system property <code>spin.off.dispatcher.factory<code> that specifies
   * the default factory for dispatchers for spin-off.
   */
  public static final String SPIN_OFF_DISPATCHER_FACTORY = "spin.off.dispatcher.factory";

  /**
   * Interceptor for spin-over used as default.
   */
  private static Interceptor defaultOverInterceptor;

  /**
   * Interceptor for spin-off used as default.
   */
  private static Interceptor defaultOffInterceptor;

  /**
   * The equals method of class <code>object</code>.
   */
  private static final Method equalsMethod;

  /**
   * Starter for spin-off used as default.
   */
  private static Starter defaultOffStarter;

  /**
   * DispatcherFactory for spin-off used as default.
   */
  private static DispatcherFactory defaultOffDispatcherFactory;
  
  /**
   * The wrapped object.
   */
  private Object object;
  
  /**
   * The handler of invocations of methods on the <em>Spin</em>-proxy.
   */
  private AbstractInvocationHandler invocationHandler;

  /**
   * Create a spin-off-wrapper for the given object.
   *
   * @param  spinOffObject            the object to spin-off
   * @param  interceptor              interceptor to intercept invocations
   * @param  starter                  starter to use
   * @param  dispatcherFactory        factory for dispatchers to use
   * @throws IllegalArgumentException if any argument is <code>null</code>
   * 
   * @see #off(Object)
   */
  public Spin(Object spinOffObject, Interceptor interceptor,
              Starter starter, DispatcherFactory dispatcherFactory) {
    if (spinOffObject == null) {
      throw new IllegalArgumentException("object to spin-off must not be null");
    }
    this.object = spinOffObject;
    
    invocationHandler = new OffHandler(interceptor, starter, dispatcherFactory); 
  }

  /**
   * Create a spin-over-wrapper for the given object.
   *
   * @param  spinOverObject           the object to spin over
   * @param  interceptor              interceptor to intercept invocations
   * @throws IllegalArgumentException if any argument is <code>null</code>
   * 
   * @see #over(Object)
   */
  public Spin(Object spinOverObject, Interceptor interceptor) {
    if (spinOverObject == null) {
      throw new IllegalArgumentException("object to spin-over must not be null");
    }
    
    this.object = spinOverObject;

    invocationHandler = new OverHandler(interceptor); 
  }

  /**
   * Get a proxy for the wrapped object.
   * <br>
   * The returned object can safely be casted to any interface the wrapped
   * object implements.
   *
   * @return   the new proxy
   */
  public Object getProxy() {
    Class clazz = object.getClass();

    return Proxy.newProxyInstance(clazz.getClassLoader(),
                                  interfaces(clazz),
                                  invocationHandler);
  }

  /**
   * Convenience method to spin-off the given object from swing.
   * <br>
   * The returned object can safely be casted to any interface
   * the given object implements.
   *
   * @param object    the object to spin-off
   * @return          proxy for the given object
   * @see             #setDefaultOffInterceptor(Interceptor)
   * @see             #setDefaultOffStarter(Starter)
   * @see             #setDefaultOffDispatcherFactory(DispatcherFactory)
   */
  public static Object off(Object object) {
    return new Spin(object, defaultOffInterceptor,
                    defaultOffStarter, defaultOffDispatcherFactory).getProxy();
  }

  /**
   * Convenience method to spin-over the given object with swing.
   * <br>
   * The returned object can safely be casted to any interface
   * the given object implements.
   *
   * @param object    the object to spin-over
   * @return          proxy for the given object
   * @see             #setDefaultOverInterceptor(Interceptor)
   */
  public static Object over(Object object) {
    return new Spin(object, defaultOverInterceptor).getProxy();
  }

  /**
   * Test if the given object is a <em>Spin</em> proxy.
   * 
   * @param object    object to test
   * @return          <code>true</code> if given object is a <em>Spin</em> proxy,
   *                  <code>false</code> otherwise
   */
  public static boolean isSpinProxy(Object object) {
    
    return (object != null) &&
           Proxy.isProxyClass(object.getClass()) &&
           (Proxy.getInvocationHandler(object) instanceof AbstractInvocationHandler); 
  }

  /**
   * Set the default interceptor to be used for spin-over.
   * 
   * @param interceptor interceptor to use as default
   */
  public static void setDefaultOverInterceptor(Interceptor interceptor) {
    if (interceptor == null) {
      throw new IllegalArgumentException("interceptor must not be null");
    }
    defaultOverInterceptor = interceptor;
  }

  /**
   * Get the default interceptor for spin-over.
   * 
   * @return  interceptor used as default
   */
  public static Interceptor getDefaultOverInterceptor() {
    return defaultOverInterceptor;
  }

  /**
   * Set the default interceptor to be used for spin-off.
   * 
   * @param interceptor interceptor to use as default
   */
  public static void setDefaultOffInterceptor(Interceptor interceptor) {
    if (interceptor == null) {
      throw new IllegalArgumentException("interceptor must not be null");
    }
    defaultOffInterceptor = interceptor;
  }

  /**
   * Get the default interceptor for spin-off.
   * 
   * @return  interceptor used as default
   */
  public static Interceptor getDefaultOffInterceptor() {
    return defaultOffInterceptor;
  }

  /**
   * Set the default starter to be used for spin-off.
   * 
   * @param starter   starter to use as default
   */
  public static void setDefaultOffStarter(Starter starter) {
    if (starter == null) {
      throw new IllegalArgumentException("starter must not be null");
    }
    defaultOffStarter = starter;
  }

  /**
   * Get the default starter for spin-off.
   * 
   * @return  starter used as default
   */
  public static Starter getDefaultOffStarter() {
    return defaultOffStarter;
  }

  /**
   * Set the default factory for dispacthers to be used for spin-off.
   * 
   * @param dispatcherFactory    factory for dispatchers to use as default
   */
  public static void setDefaultOffDispatcherFactory(DispatcherFactory dispatcherFactory) {
    if (dispatcherFactory == null) {
      throw new IllegalArgumentException("dispatcherFactory must not be null");
    }
    defaultOffDispatcherFactory = dispatcherFactory;
  }

  /**
   * Get the default factory for dispatchers for spin-off.
   * 
   * @return  dispatcherFactory used as default
   */
  public static DispatcherFactory getDefaultOffDispatcherFactory() {
    return defaultOffDispatcherFactory;
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
   * Utility method to get an instance of a class.
   * 
   * @param property    system property holding the name of the class to instantiate
   * @param clazz       class to instantiate if system property is not set
   * @return            created instance
   */
  private static Object instance(String property, Class clazz) throws Exception {
    String className = System.getProperty(property);
    if (className != null) {
      clazz = Class.forName(className);
    }
    return clazz.newInstance();
  }
  
  /**
   * Initialize reflection and defaults for spin-off.
   */
  static {
    try {
      equalsMethod = Object.class.getDeclaredMethod("equals", new Class[]{Object.class});

      defaultOverInterceptor      = (Interceptor)      instance(SPIN_OVER_INTERCEPTOR      , Interceptor.class);
      defaultOffInterceptor       = (Interceptor)      instance(SPIN_OFF_INTERCEPTOR       , Interceptor.class);
      defaultOffStarter           = (Starter)          instance(SPIN_OFF_STARTER           , SimpleStarter.class);
      defaultOffDispatcherFactory = (DispatcherFactory)instance(SPIN_OFF_DISPATCHER_FACTORY, AWTReflectDispatcherFactory.class);
    } catch (Exception ex) {
      throw new Error("unable to initialize Spin, check your system properties", ex);
    }
  }

  /**
   * Abstract base class for handlers of invocations on the <em>Spin</em> proxy.
   */
  private abstract class AbstractInvocationHandler implements InvocationHandler {

    /**
     * The interceptor.
     */
    private Interceptor interceptor;

    /**
     * Create an handler for invocations.
     * 
     * @param interceptor   interceptor
     */
    public AbstractInvocationHandler(Interceptor interceptor) {
      this.interceptor = interceptor;
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
  
      if (equalsMethod.equals(method)) {
        return new Boolean(isSpinProxy(args[0]) &&
                           equals(Proxy.getInvocationHandler(args[0])));
      } else {
        Invocation invocation = createInvocation();
        invocation.setObject(object);
        invocation.setMethod(method);
        invocation.setArguments(args);
        
        return interceptor.intercept(invocation);
      }
    }
    
    /**
     * Create an {@link Invocation}.
     * 
     * @return  new <code>Invocation</code>
     */
    protected abstract Invocation createInvocation();
    
    /**
     * Test on equality based on the wrapped object.
     */
    public boolean equals(Object object) {
      if (object != null && object.getClass().equals(getClass())) {
        AbstractInvocationHandler handler = (AbstractInvocationHandler)object;
        return (getObject().equals(handler.getObject()));
      }
      return false;
    }
    
    /**
     * Get the wrapped Object.
     */
    private Object getObject() {
      return Spin.this.object;
    }
       
  }
   
  /**
   * Spin-off handler of invocations on the <em>Spin</em> proxy.
   */
  private class OffHandler extends AbstractInvocationHandler {
  
    /**
     * The starter to be used only for spin-off.
     */
    private Starter starter;

    /**
     * The dispatcherFactory to be used only for spin-off.
     */
    private DispatcherFactory dispatcherFactory;
 
    /**
     * Create a new handler for spin-off.
     * 
     * @param interceptor       interceptor
     * @param starter           starter to use for starting asynchronous
     *                          invocations 
     * @param dispatcherFactory factory to use for dipatching of events
     */
    public OffHandler(Interceptor interceptor, Starter starter, DispatcherFactory dispatcherFactory) {
      super(interceptor);
      
      if (starter == null) {
        throw new IllegalArgumentException("executor must not be null");
      }
      if (dispatcherFactory == null) {
        throw new IllegalArgumentException("dispatcherFactory must not be null");
      }

      this.starter           = starter;
      this.dispatcherFactory = dispatcherFactory; 
    }
    
    /**
     * Create an {@link Invocation}.
     * 
     * @return  new <code>Invocation</code>
     */
    protected Invocation createInvocation() {
      return new OffInvocation(starter, dispatcherFactory.createDispatcher());
    }
  }

  /**
   * Spin-over handler of invocations on the <em>Spin</em> proxy.
   */
  private class OverHandler extends AbstractInvocationHandler {

    /**
     * Create a new handler for spin-over.
     * 
     * @param interceptor       interceptor
     */
    public OverHandler(Interceptor interceptor) {
      super(interceptor);
    }
  
    /**
     * Create an {@link Invocation}.
     * 
     * @return  new <code>Invocation</code>
     */
    protected Invocation createInvocation() {
      return new OverInvocation();
    }
  } 
}