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
package spin.demo.complex;

/**
 * A directory.
 */
public class Directory {

  private String  name;
  private boolean isLeaf;

  /**
   * Constructor.
   *
   * @param name      name of this directory
   * @param isLeaf    is this directory a leaf
   */
  public Directory(String name, boolean isLeaf) {
    this.name   = name;
    this.isLeaf = isLeaf;
  }

  /**
   * Test if directory has children.
   *
   * @return   <code>true</code> if this directory is a leaf
   */
  public boolean isLeaf() {
    return isLeaf;
  }

  /**
   * Get the name.
   *
   * @return      name of this directory
   */
  public String getName() {
    return name;
  }
}
