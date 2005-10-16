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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import spin.Spin;
import spin.demo.Assert;
import spin.off.ListenerSpinOver;

/**
 * A demonstration of a GUI handling an async bean. Note that this
 * class uses {@link spin.off.ListenerSpinOver} to automatically
 * spin-over a listener to async events.
 */
public class AsyncGUI extends JPanel implements AsyncListener {

  private JButton   button   = new JButton("Start");
  private JTextArea textArea = new JTextArea();

  private AsyncBean asyncBean;

  /**
   * Constructor.
   */
  public AsyncGUI(AsyncBean anAsyncBean) {
    this.asyncBean = anAsyncBean;

    // ListenerSpinOver will spin-over us automatically
    asyncBean.addListener(this);

    setLayout(new BorderLayout());

    add(button, BorderLayout.NORTH);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        button.setEnabled(false);

        asyncBean.start();

        button.setEnabled(true);
      }
    });

    add(new JScrollPane(textArea), BorderLayout.CENTER);
  }

  /**
   * @see AsyncListener
   */
  public void finished(int number, long duration) {
    Assert.isEDT();
      
    textArea.append("Duration of " + number + " was " + duration + "\n");
  }
  /**
   * Entrance to this demo.
   */
  public static void main(String[] args) {

    // Automatically spin-over all listeners
    Spin.setDefaultOffEvaluator(new ListenerSpinOver());

    AsyncBean asyncBean = new AsyncBeanImpl();
    AsyncGUI  asyncGUI  = new AsyncGUI((AsyncBean)Spin.off(asyncBean));

    JFrame frame = new JFrame("Async");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(asyncGUI);
    frame.pack();
    frame.setVisible(true);
  }
}

