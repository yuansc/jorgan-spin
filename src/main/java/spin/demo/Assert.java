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

import javax.swing.SwingUtilities;

/**
 * Utility class to assert correct thread handling.
 */
public class Assert {

	/**
	 * No instance.
	 */
	private Assert() {
	}

	/**
	 * Must be executed on the EDT.
	 */
	public static void onEDT() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new Error("assertion failed: on EDT");
		}
	}

	/**
	 * Must be executed off the EDT.
	 */
	public static void offEDT() {
		if (SwingUtilities.isEventDispatchThread()) {
			throw new Error("assertion failed: off EDT");
		}
	}
}
