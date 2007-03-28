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
package spin.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import spin.Spin;

/**
 * A demonstration of a GUI using spin off.
 */
public class SpinOffGUI extends JApplet {

	private JLabel label = new JLabel("???");

	private JButton button = new JButton("Get");

	private Bean bean;

	/**
	 * Constructor.
	 * 
	 * @param aBean
	 *            the bean for this demonstration
	 */
	public SpinOffGUI(Bean aBean) {
		bean = aBean;

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(label, BorderLayout.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);

		getContentPane().add(button, BorderLayout.SOUTH);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				label.setText("...");
				button.setEnabled(false);

				String value = bean.getValue();

				label.setText(value);
				button.setEnabled(true);
			}
		});
	}

	/**
	 * Entrance to this demo.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Bean bean = new BeanImpl();
		SpinOffGUI spinOffGUI = new SpinOffGUI((Bean) Spin.off(bean));

		JFrame frame = new JFrame("Spin off");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(spinOffGUI);
		frame.pack();
		frame.setVisible(true);
	}
}
