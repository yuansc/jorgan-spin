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
package spin.over;

import javax.swing.SwingUtilities;

import spin.Invocation;

/**
 * Invocation subclass that handles spin-over.
 */
public class OverInvocation extends Invocation {

  private boolean wait;
  
  public OverInvocation(boolean wait) {
      this.wait = wait;
  }
  
  /**
   * Spin-over this invocation to the EDT (if not already on the EDT).
   */
  protected void spin() throws Throwable {

    if (SwingUtilities.isEventDispatchThread()) {
      evaluate();
    } else {
      Runnable runnable = new Runnable() {
        public void run() {
          evaluate();
        }
      };
      
      if (wait) {
          SwingUtilities.invokeAndWait(runnable);
      } else {
          SwingUtilities.invokeLater(runnable);          
      }
    }
  }
}