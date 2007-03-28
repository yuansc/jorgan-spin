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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import spin.demo.Assert;

/**
 * Implementation of a progress.
 */
public class ProgressBeanImpl implements ProgressBean {

	private PropertyChangeListener listener;

	private boolean cancelled;

	private double status;

	/**
	 * Constructor.
	 */
	public ProgressBeanImpl() {
	}

	/**
	 * Add a listener to property changes.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		Assert.isNotEDT();

		this.listener = listener;
	}

	/**
	 * Start.
	 */
	public void start() {
		Assert.isNotEDT();

		cancelled = false;

		status = 0.0d;
		for (int i = 0; i < 10; i++) {
			try {
				synchronized (this) {
					wait(1000);
				}
				Double oldValue = new Double(status);
				status += 0.1d;
				Double newValue = new Double(status);

				if (listener != null) {
					listener.propertyChange(new PropertyChangeEvent(this,
							"value", oldValue, newValue));
				}
			} catch (InterruptedException ex) {
				// ignore
			}
			if (cancelled) {
				break;
			}
		}
	}

	/**
	 * Cancel the progress.
	 */
	public void cancel() {
		Assert.isNotEDT();

		cancelled = true;
	}

	/**
	 * Get the current status.
	 * 
	 * @return status of progress
	 */
	public double getStatus() {
		Assert.isNotEDT();

		return status;
	}
}
