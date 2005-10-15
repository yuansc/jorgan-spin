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

import spin.off.OffSpinner;
import spin.over.OverSpinner;

/**
 * <p>
 * <em>Spin</em> offers a transparent threading solution for non-freezing
 * Swing applications.
 * </p>
 * <p>
 * Let <code>bean</code> be a reference to a non-visual (possibly
 * multithreaded) bean implementing the interface <code>Bean</code> whose
 * methods have to be called by a Swing component. You can avoid any freezing by
 * using one line of code:
 * 
 * <pre>
 * bean = (Bean) Spin.off(bean);
 * </pre>
 * 
 * Now each method call on <code>bean</code> is executed on a separate thread,
 * while the EDT is continuing to dispatch events. All return values or
 * exceptions are handled by <em>Spin</em> and transparently returned to the
 * calling method.
 * </p>
 * <p>
 * For calls from other threads than the EDT to your Swing component you can use
 * the following (being <ode>XYListener</code> any interface your component
 * implements):
 * 
 * <pre>
 *     bean.addXYListener((XYListener)Spin.over(component); 
 * </pre>
 * 
 * Now all required updates to your component (and/or its model) are
 * transparently excuted on the EDT.
 * </p>
 * 
 * @see #off(Object)
 * @see #over(Object)
 */
public class Spin {

    private static ProxyFactory defaultProxyFactory = new JDKProxyFactory();

    private static Spinner defaultOffSpinner = new OffSpinner();

    private static Spinner defaultOverSpinner = new OverSpinner();

    private Object proxy;

    /**
     * Create a <em>Spin</em> wrapper for the given object.
     * 
     * @param object    object to wrap
     * @param spinner   spinner of invocations on the given object
     */
    public Spin(Object object, Spinner spinner) {
        this(object, defaultProxyFactory, spinner);
    }

    /**
     * Create a <em>Spin</em> wrapper for the given object.
     * 
     * @param object        object to wrap
     * @param proxyFactory  factory for a proxy 
     * @param spinner       spinner of invocations on the given object
     */
    public Spin(Object object, ProxyFactory proxyFactory, Spinner spinner) {
        if (object == null) {
            throw new IllegalArgumentException("object must not be null");
        }
        if (proxyFactory == null) {
            throw new IllegalArgumentException("proxyFactory must not be null");
        }
        if (spinner == null) {
            throw new IllegalArgumentException("spinner must not be null");
        }

        proxy = proxyFactory.createProxy(object, spinner);
    }

    /**
     * Get a proxy for the wrapped object. <br>
     * The returned object can safely be casted to any interface the wrapped
     * object implements.
     * 
     * @return the new proxy
     */
    public Object getProxy() {
        return proxy;
    }

    /**
     * Convenience method to spin-off the given object from Swing. <br>
     * The returned object can safely be casted to any interface the given
     * object implements.
     * 
     * @param object
     *            the object to spin-off
     * @return proxy for the given object
     * @see #setDefaultProxyFactory(ProxyFactory)
     * @see #setDefaultOffSpinner(Spinner)
     */
    public static Object off(Object object) {
        return new Spin(object, defaultProxyFactory, defaultOffSpinner).getProxy();
    }

    /**
     * Convenience method to spin-over the given object with Swing. <br>
     * The returned object can safely be casted to any interface the given
     * object implements.
     * 
     * @param object
     *            the object to spin-over
     * @return proxy for the given object
     * @see #setDefaultProxyFactory(ProxyFactory)
     * @see #setDefaultOverSpinner(Spinner)
     */
    public static Object over(Object object) {
        return new Spin(object, defaultProxyFactory, defaultOverSpinner)
                .getProxy();
    }

    /**
     * Set the default factory of proxies.
     * 
     * @param factory   proxy factore to use as default
     */
    public static void setDefaultProxyFactory(ProxyFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory must not be null");
        }
        defaultProxyFactory = factory;
    }

    /**
     * Set the default spinner for spin-off.
     * 
     * @param spinner   spinner to use for spin-off
     */
    public static void setDefaultOffSpinner(Spinner spinner) {
        if (spinner == null) {
            throw new IllegalArgumentException("spinner must not be null");
        }
        defaultOffSpinner = spinner;
    }

    /**
     * Set the default spinner for spin-over.
     * 
     * @param spinner   spinner for spin-over
     */
    public static void setDefaultOverSpinner(Spinner spinner) {
        if (spinner == null) {
            throw new IllegalArgumentException("spinner must not be null");
        }
        defaultOverSpinner = spinner;
    }

    /**
     * Get the default proxy factory.
     * 
     * @return  the default factory of proxies
     */
    public static ProxyFactory getDefaultProxyFactory() {
        return defaultProxyFactory;
    }

    /**
     * Get the default spinner for spin-off.
     * 
     * @return  spinner for spin-off
     */
    public static Spinner getDefaultOffSpinner() {
        return defaultOffSpinner;
    }

    /**
     * Get the default spinner for spin-over.
     * 
     * @return  spinner for spin-over
     */
    public static Spinner getDefaultOverSpinner() {
        return defaultOverSpinner;
    }
}