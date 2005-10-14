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
import spin.Spinner;

/**
 * A spinner for spin-over.
 */
public class OverSpinner extends Spinner {

    private boolean wait;

    public OverSpinner() {
        this(true);
    }

    public OverSpinner(boolean wait) {
        this.wait = wait;
    }

    /**
     * Spin the given invocation on the EDT.
     * 
     * @param invocation
     *            invocation to spin-over
     */
    public final void spin(final Invocation invocation) throws Throwable {

        if (SwingUtilities.isEventDispatchThread()) {
            invocation.evaluate();
        } else {
            Runnable runnable = new Runnable() {
                public void run() {
                    invocation.evaluate();
                }
            };
            if (wait) {
                SwingUtilities.invokeAndWait(runnable);
            } else {
                if (invocation.getMethod().getReturnType() == null) {
                    throw new IllegalArgumentException(
                            "cannot invokeLater a method with non-void return");
                }
                SwingUtilities.invokeLater(runnable);
            }
        }
    }
}