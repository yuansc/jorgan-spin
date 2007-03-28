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
package spin.demo.dispatcher;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;

import spin.off.DialogDispatcherFactory;

/**
 * Implementation of a dispatcher which uses a pool of modal dialogs to dispatch
 * events. The dialogs are concealed, i.e. they are located outside of the
 * visible screen area. <br>
 * Note that the <code>Dispatcher</code>s created by this class are not
 * usable for GUIs with <code>Cancel</code> functionality since they will
 * block user access to all windows of your application during dispatching.<br>
 * While this seems to be a major drawback this class nevertheless has its
 * eligibility:
 * <ul>
 * <li> It shows that <em>Spin</em> does no 'magic' nor any dirty tricks -
 * every shown <code>Dialog</code> does the same. </li>
 * <li> If your application really needs a <code>Cancel</code> functionality
 * you should consider to use a real asynchronous solution as showed in
 * <code>spin.demo.async.AsyncGUI</code> - please see also the caveat <a
 * href="../../../index.html#asynchronous">'asynchronous'</a>. </li>
 * </ul>
 */
public class ConcealedDialogDispatcherFactory extends DialogDispatcherFactory {

	/**
	 * The shared owner of all created dialogs.
	 */
	private static Frame frame = new Frame();

	/**
	 * Pool of dialogs.
	 */
	private ArrayList dialogPool = new ArrayList();

	/**
	 * Aquire a pooled dialog.
	 * 
	 * @return dialog
	 */
	protected Dialog aquireDialog() {
		synchronized (dialogPool) {
			Dialog dialog;
			if (dialogPool.size() == 0) {
				dialog = createDialog();
			} else {
				dialog = (Dialog) dialogPool.remove(0);
			}
			initDialog(dialog);

			return dialog;
		}
	}

	/**
	 * Release a pooled dialog.
	 * 
	 * @param dialog
	 *            dialog to release
	 */
	protected void releaseDialog(Dialog dialog) {
		synchronized (dialogPool) {
			dialogPool.add(dialog);
		}
	}

	/**
	 * Factory method to create a new dialog. Can be overriden to create a
	 * custom dialog.
	 * 
	 * @return created dialog.
	 */
	protected Dialog createDialog() {
		return new Dialog(frame, "spin", true);
	}

	/**
	 * Initialize the given dialog. <br>
	 * This default implementation positions the dialog outside of the visible
	 * screen boundary.
	 * 
	 * @param dialog
	 *            dialog to initialize
	 */
	protected void initDialog(Dialog dialog) {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation(size.width, size.height);
	}
}
