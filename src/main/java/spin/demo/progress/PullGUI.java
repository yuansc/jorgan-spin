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
package spin.demo.progress;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import spin.Spin;

/**
 * A demonstration of a GUI showing pulled progress.
 */
public class PullGUI extends JPanel implements ActionListener {

	private JProgressBar progressBar = new JProgressBar();

	private JButton button = new JButton("Start");

	private ProgressBean progressBean;

	/**
	 * Constructor.
	 */
	public PullGUI(ProgressBean aProgressBean) {
		this.progressBean = aProgressBean;

		setLayout(new BorderLayout());

		add(progressBar, BorderLayout.CENTER);

		add(button, BorderLayout.SOUTH);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if ("Start".equals(button.getText())) {
					button.setText("Cancel");
					progressBar.setValue(0);

					Timer timer = new Timer(1000, PullGUI.this);
					timer.start();

					progressBean.start();

					timer.stop();

					button.setText("Start");
					progressBar.setValue(100);
				} else {
					progressBean.cancel();
				}
			}
		});
	}

	/**
	 * @see ActionListener
	 */
	public void actionPerformed(ActionEvent ev) {
		progressBar.setValue((int) (progressBean.getStatus() * 100));
	}

	/**
	 * Entrance to this demo.
	 */
	public static void main(String[] args) {

		ProgressBean progressBean = new ProgressBeanImpl();
		PullGUI pullGUI = new PullGUI((ProgressBean) Spin.off(progressBean));

		JFrame frame = new JFrame("Pulled progress");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(pullGUI);
		frame.pack();
		frame.setVisible(true);
	}
}
