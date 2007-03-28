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
package spin.demo.exception;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import spin.Spin;
import spin.proxy.CGLibProxyFactory;

/**
 * A demonstration of a GUI handling an exception.
 */
public class ExceptionGUI extends JPanel {

	private JButton button = new JButton("Exception");

	private ExceptionBean exceptionBean;

	/**
	 * Constructor.
	 */
	public ExceptionGUI(ExceptionBean aExceptionBean) {
		this.exceptionBean = aExceptionBean;

		setLayout(new BorderLayout());

		add(button, BorderLayout.CENTER);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				button.setEnabled(false);

				try {
					exceptionBean.possiblyThrowException();
				} catch (BeanException ex) {
					JOptionPane.showMessageDialog(ExceptionGUI.this,
							"Bean threw an exception", "Exception",
							JOptionPane.ERROR_MESSAGE);
				}

				button.setEnabled(true);
			}
		});
	}

	/**
	 * Entrance to this demo.
	 */
	public static void main(String[] args) {

		ExceptionBean bean = new ExceptionBean();

		Spin spin = new Spin(bean, new CGLibProxyFactory(), Spin
				.getDefaultOffEvaluator());

		ExceptionGUI exceptionGUI = new ExceptionGUI((ExceptionBean) spin
				.getProxy());

		JFrame frame = new JFrame("Exception");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(exceptionGUI);
		frame.pack();
		frame.setVisible(true);
	}
}
