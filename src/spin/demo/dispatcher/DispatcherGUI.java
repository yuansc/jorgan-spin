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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import spin.Spin;
import spin.demo.Bean;
import spin.demo.BeanImpl;
import spin.off.AWTReflectDispatcherFactory;
import spin.off.DispatcherFactory;

/**
 * A demonstration of a GUI using different dispatchers.
 */
public class DispatcherGUI extends JPanel {

  private JLabel  label  = new JLabel("???");
  private JButton button = new JButton("Get");
  
  private JPanel    dispatcherFactoryPanel   = new JPanel();
  private ButtonGroup dispatcherFactoryGroup = new ButtonGroup(); 

  private Bean bean;
  private Bean spinOffBean;
  
  private static Map dispatcherFactories = new HashMap();
  static {
    dispatcherFactories.put("AWT Reflection"  , new AWTReflectDispatcherFactory());
    dispatcherFactories.put("Concealed Dialog", new ConcealedDialogDispatcherFactory());
    dispatcherFactories.put("Revealed Dialog" , new RevealedDialogDispatcherFactory());      
  }

  /**
   * Constructor.
   *
   * @param bean      the bean for this demonstration
   */
  public DispatcherGUI(Bean aBean) {
    bean = aBean;    

    setLayout(new BorderLayout());

    add(label, BorderLayout.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment  (JLabel.CENTER);

    add(button, BorderLayout.SOUTH);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        label.setText("...");
        button.setEnabled(false);

        String value = spinOffBean.getValue();

        label.setText(value);
        button.setEnabled(true);
      }
    });
    
    dispatcherFactoryPanel.setLayout(new GridLayout());
    add(dispatcherFactoryPanel, BorderLayout.NORTH);

    // create a radioButton for each available dispatcher factory
    Iterator iterator = dispatcherFactories.keySet().iterator();
    while (iterator.hasNext()) {
      final String            name    = (String)iterator.next();
      final DispatcherFactory factory = (DispatcherFactory)dispatcherFactories.get(name); 

      JRadioButton button = new JRadioButton(name);
      button.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          changeDispatcherFactory(factory);
        }
      });
      dispatcherFactoryGroup.add(button);
      dispatcherFactoryPanel.add(button);
      button.setSelected(true);
    }
  }

  private void changeDispatcherFactory(DispatcherFactory factory) {
    spinOffBean = (Bean)new Spin(bean,
                                 Spin.getDefaultOffInterceptor(),
                                 Spin.getDefaultOffStarter(),
                                 factory).getProxy();
  }
  
  /**
   * Entrance to this demo.
   */
  public static void main(String[] args) {

    Bean          bean          = new BeanImpl();
    DispatcherGUI dispatcherGUI = new DispatcherGUI(bean);

    JFrame frame = new JFrame("Dispatchers");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(dispatcherGUI);
    frame.pack();
    frame.setVisible(true);
  }    
}
