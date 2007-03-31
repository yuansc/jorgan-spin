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

import spin.demo.Assert;

/**
 * Implementation of a exception throwing bean.
 */
public class ExceptionBean {

	/**
	 * Possibly throw an exception.
	 */
	public void possiblyThrowException() throws BeanException {
		Assert.offEDT();

		long time = (long) (Math.random() * 5000);

		try {
			synchronized (this) {
				wait(time);
			}
		} catch (InterruptedException ex) {
			// ignore
		}

		if (time > 2500) {
			throw new BeanException();
		}
	}
}
