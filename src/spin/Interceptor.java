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

/**
 * An <code>Interceptor</code> intercepts {@link Invocation}s for additional
 * pre and post processing.
 * <br>
 * Subclasses of this class can be specified on construction of a <em>Spin</em>
 * instance or globally by calling the static methods:
 * <pre>
 *   Spin.setDefaultOffInterceptor (spinOffInterceptor);
 *   Spin.setDefaultOverInterceptor(spinOverInterceptor);
 * </pre>
 * For an example see {@link spin.off.ListenerSpinOver}.
 * 
 * @see Spin#Spin(Object, Interceptor)  
 * @see Spin#Spin(Object, Interceptor, spin.off.Starter, spin.off.DispatcherFactory)  
 * @see Spin#setDefaultOffInterceptor(Interceptor)  
 * @see Spin#setDefaultOverInterceptor(Interceptor)  
 */
public class Interceptor {

  /**
   * Intercept the given <code>Invocation</code>.
   * <br>
   * This default implementation simply calls {@link Invocation#start()}.
   * Subclasses may add pre and post processing to an <code>Invocation</code>.
   * 
   * @param invocation    the <code>Invocation</code> to intercept
   * @return              the result of the <code>Invocation</code>
   * @throws Throwable    if any <code>Throwable</code> is thrown by the
   *                      <code>Invocation</code>
   */
  public Object intercept(Invocation invocation) throws Throwable {
    return invocation.start();
  }
}
