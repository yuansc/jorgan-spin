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

import java.io.File;
import java.io.FileFilter;

import spin.demo.Assert;

/**
 * Implementation of a file service.
 */
public class FileService implements DirectoryService {

  private File       root;
  private FileFilter filter;

  /**
   * Constructor.
   */
  public FileService() {
    this(new File("./"));
  }

  /**
   * Constructor.
   *
   * @param root    root of this file service
   */
  public FileService(String root) {
    this(new File(root));
  }

  /**
   * Constructor.
   *
   * @param root    root of this file service
   */
  public FileService(File root) {
    this(root, new AllFileFilter());
  }

  /**
   * Constructor.
   *
   * @param root    root of this file service
   * @param filter  filter of this file service
   */
  public FileService(File root, FileFilter filter) {
    this.root   = root;
    this.filter = filter;
  }

  /**
   * Get the root directory.
   *
   * @return   the root directory
   */
  public Directory getRoot() {

    Assert.isNotEDT();
    
    return new FileDirectory(root, true);
  }

  /**
   * Get the children of a directory.
   *
   * @param directory    directory to get children for
   * @return             children
   */
  public Directory[] getChildren(Directory directory) throws DirectoryServiceException {

    Assert.isNotEDT();

    File   file  = ((FileDirectory)directory).file;
    File[] files = file.listFiles(filter);

    Directory[] directories = new Directory[files.length];

    for (int f = 0; f < files.length; f++) {
      directories[f] = new FileDirectory(files[f], false);
    }
    return directories;
  }

  /**
   * Extension to directory.
   */
  private class FileDirectory extends Directory {

    private File file;

    /**
     * Constructor
     *
     * @param file  file represented by this directory
     * @param root  is this directory the root
     */
    public FileDirectory(File file, boolean root) {
      super(root ? file.getAbsolutePath() : file.getName(),
            !file.isDirectory());

      this.file = file;
    }
  }

  /**
   * Filter that accepts all files.
   */
  private static class AllFileFilter implements FileFilter {

    /**
     * @see FileFilter
     */
    public boolean accept(File file) {
      return true;
    }
  }
}
