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
package spin.demo.prompt;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import spin.Spin;

/**
 * A demonstration of a GUI showing a call prompt.
 */
public class CallGUI extends JPanel {

  private JButton button = new JButton("Start");

  private PromptBean promptBean;

  /**
   * Constructor.
   */
  public CallGUI(PromptBean aPromptBean) {
    this.promptBean = aPromptBean;

    setLayout(new BorderLayout());

    add(button, BorderLayout.CENTER);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        button.setEnabled(false);

        for (int i = promptBean.size() - 1; i >= 0; i--) {
          String value = promptBean.get(i);

          if (JOptionPane.YES_OPTION ==
              JOptionPane.showConfirmDialog(CallGUI.this,
                                            value, "Prompt",
                                            JOptionPane.YES_NO_OPTION)) {
            promptBean.process(i);
          }
        }

        button.setEnabled(true);
      }
    });
  }

  /**
   * Entrance to this demo.
   */
  public static void main(String[] args) {

    PromptBean promptBean = new PromptBeanImpl();
    CallGUI    callGUI    = new CallGUI((PromptBean)Spin.off(promptBean));

    JFrame frame = new JFrame("Call prompt");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(callGUI);
    frame.pack();
    frame.setVisible(true);
  }
}

