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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Abstract base class for handling of one single method invocation
 * on a <em>Spin</em> proxy.
 */
public abstract class Invocation {

  private Object    object;
  private Method    method;
  private Object[]  args;
  private Throwable throwable;
  private Object    result;

  /**
   * Set the object to invoke method on.
   * 
   * @param object    object to invoke method on
   */
  public void setObject(Object object) {
    this.object = object;
  }
  
  /**
   * Get the object which method is invoked.
   * 
   * @return    the object of the invoked method
   */
  public Object getObject() {
    return object;
  }

  /**
   * Set the method to invoke
   *  
   * @param method    method to invoke
   */
  public void setMethod(Method method) {
    this.method = method;
  }


  /**
   * Get the invoked method.
   * 
   * @return    the invoked method
   */
  public Method getMethod() {
    return method;
  }

  /**
   * Set the arguments for the invoked method
   * 
   * @param args   the arguments for the invoked method
   */
  public void setArguments(Object[] args) {
    this.args = args;
  }

  /**
   * Get the arguments for this invocation.
   * 
   * @return    the arguments for the invoked method
   */
  public Object[] getArguments() {
    return args;
  }

  /**
   * Start the invocation.
   *
   * @return           the result of the invocation
   * @throws Throwable if the wrapped method throws any exception
   */
  public Object start() throws Throwable {

    spin();

    if (throwable != null) {
      throw throwable;
    }
    return result;
  }

  /**
   * Spin the evaluation of this invocation on an appropriate thread.
   * This method has to be implemented in subclasses to call {@link #evaluate()}.
   */
  protected abstract void spin() throws Throwable;

  /**
   * Evaluate the return value (or a possibly thrown <code>Throwable</code>)
   * by forwarding this invocation to the corresponding method of the wrapped
   * object.
   */
  protected void evaluate() {

    try {
      result = method.invoke(object, args);
    } catch (InvocationTargetException ex) {
      this.throwable = ex.getTargetException();
    } catch (Throwable throwable) {
      this.throwable = throwable;
    }
  }
}