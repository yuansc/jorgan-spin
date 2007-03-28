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

import java.beans.PropertyChangeListener;

/**
 * A common interface for a bean.
 */
public interface Bean {

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public String getValue();

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            value to set
	 */
	public void setValue(String value);

	/**
	 * Add a listener to property changes.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener);
}
