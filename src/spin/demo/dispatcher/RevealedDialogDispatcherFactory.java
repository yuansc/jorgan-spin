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
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Window;

import javax.swing.JProgressBar;

import spin.off.DialogDispatcherFactory;

/**
 * Implementation of a dispatcher which uses dialogs to dispatch events. 
 * <br>
 * Due to the inability to change the owner of a dialog (after creation) this
 * class has to create a fresh dialog for each invocation on the bean.
 * This admittedly inperformant behaviour could be improved by pooling the
 * dialogs on a per-owner basis (e.g. utilizing a <code>WeakHashMap</code>).
 * <br>
 * Subclasse might want to offer cancel functionality, display real progress
 * or show an animated image (e.g. the famous Netscape icon).
 * 
 * @see #aquireDialog(Window)
 */
public class RevealedDialogDispatcherFactory extends DialogDispatcherFactory {

  /**
   * Aquire a dialog for the currently active window.
   */
  protected Dialog aquireDialog() {
    Dialog dialog;
      
    Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
      
    if (window == null) {
      throw new IllegalStateException("no active window");
    } else {
      dialog = aquireDialog(window); 
    }
      
    initDialog(dialog);
        
    return dialog;
  }

  /**
   * Aquire a dialog for the given owning window.
   * <br>
   * This default implementation always creates a new dialog.
   * 
   * @param owner   owner to aquire dialog for
   * @return        aquired dialog
   * @see           #createDialog(window) 
   */
  protected Dialog aquireDialog(Window owner) {

    return createDialog(owner);
  }
  
  /**
   * Create a dialog for the given owner.
   * 
   * @param owner   owner of dialog to create
   * @return        created dialog
   */
  protected Dialog createDialog(Window owner) {
    Dialog dialog;
    if (owner instanceof Dialog) {
      dialog = new Dialog((Dialog)owner, "spin", true);
    } else if (owner instanceof Frame) {
      dialog = new Dialog((Frame)owner, "spin", true);
    } else {
      throw new Error("owner is no dialog or frame");
    }
    dialog.setUndecorated(true);
     
    JProgressBar progressBar = new JProgressBar();
    progressBar.setIndeterminate(true);
    dialog.add(progressBar);

    return dialog;
  }

  protected void initDialog(Dialog dialog) {      
    dialog.pack();
    dialog.setLocationRelativeTo(dialog.getOwner());
  }
  
  protected void releaseDialog(Dialog dialog) {
    dialog.dispose();
  }  
}