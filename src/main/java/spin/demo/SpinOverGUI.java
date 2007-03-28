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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import spin.Spin;

/**
 * A demonstration of a GUI using spin over.
 */
public class SpinOverGUI extends JPanel implements PropertyChangeListener {

	private JLabel label = new JLabel("???");

	/**
	 * Constructor.
	 */
	public SpinOverGUI() {
		setLayout(new BorderLayout());

		add(label, BorderLayout.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
	}

	/**
	 * @see java.beans.PropertyChangeListener
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		Assert.isEDT();

		if ("value".equals(evt.getPropertyName())) {
			String text = (String) evt.getNewValue();

			label.setText(text);
		}
	}

	/**
	 * Entrance to this demo.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Bean bean = new BeanImpl();
		SpinOverGUI spinOverGUI = new SpinOverGUI();
		bean.addPropertyChangeListener((PropertyChangeListener) Spin
				.over(spinOverGUI));

		JFrame frame = new JFrame("Spin over");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(spinOverGUI);
		frame.pack();
		frame.setVisible(true);
	}
}
