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
package spin.off;

import javax.swing.SwingUtilities;

import spin.Invocation;

/**
 * Invocation subclass that handles spin-off.
 */
public class OffInvocation extends Invocation {

  /**
   * The starter used to asynchronous execution.
   */
  private Starter starter;

  /**
   * The dispatcher used for dispatching of events.
   */
  private Dispatcher dispatcher;
 
  
  /**
   * Create a new invocation.
   * 
   * @param starter    starter to use for asynchronous execution
   * @param dispatcher dispatcher to use for dispatching of events   
   */
  public OffInvocation(Starter starter, Dispatcher dispatcher) {
    
    this.dispatcher = dispatcher;
    this.starter    = starter;
  }
  
  /**
   * Spin-off this invocation from the EDT with the configured
   * {@link spin.off.Starter} (if not already on another thread), dispatching
   * events with the configured {@link spin.off.Dispatcher} until the method
   * of the wrapped object is finished.
   * 
   * @see spin.off.Starter
   * @see spin.off.Dispatcher
   */
  protected void spin() throws Throwable {

    if (SwingUtilities.isEventDispatchThread()) {

      starter.start(new Runnable() {
        public void run() {
          evaluate();

          dispatcher.stop();
        }
      });

      dispatcher.start();
      
      if (!isEvaluated()) {
        throw new Error("dispatcher stopped prematurely");
      }
    } else {
      evaluate();
    }
  }
}