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
 * Service that adds a unreliability to another service.
 */
public class UnreliableService implements DirectoryService {

  private DirectoryService service;

  /**
   * Constructor.
   */
  public UnreliableService(DirectoryService service) {
    this.service = service;
  }

  /**
   * Get the root directory.
   *
   * @return   the root directory
   */
  public Directory getRoot() {
    return service.getRoot();
  }

  /**
   * Get the children of a directory.
   *
   * @param directory    directory to get children for
   * @return             children
   */
  public Directory[] getChildren(Directory directory) throws DirectoryServiceException {

    // simulate exception
    if (0.7d - Math.random() < 0) {
      throw new DirectoryServiceException("Service is unreliable");
    }

    return service.getChildren(directory);
  }
}
