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
package spin.demo.async;

import java.util.ArrayList;
import java.util.List;

import spin.demo.Assert;

/**
 * Implementation of an async bean.
 */
public class AsyncBeanImpl implements AsyncBean, Runnable {

  private List listeners = new ArrayList();
  private int  number    = 0;

  /**
   * Add a listener.
   *
   * @param listener   listener to add
   */
  public void addListener(AsyncListener listener) {
    Assert.isNotEDT();

    listeners.add(listener);
  }

  /**
   * Start this bean in an asynchron thread.
   */
  public void start() {

    Assert.isNotEDT();
    
    new Thread(this).start();
  }

  /**
   * @see Runnable
   */
  public void run() {
    int  number   = this.number++;
    long duration = (long)(Math.random() * 5000);

    try {
      synchronized (this) {
        wait(duration);
      }
    } catch (InterruptedException ex) {
      // ignore
    }

    for (int l = 0; l < listeners.size(); l++) {
      ((AsyncListener)listeners.get(l)).finished(number, duration);
    }
  }
}
