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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;

import spin.Spin;

/**
 * A GUI that uses a directory service.
 */
public class DirectoryServiceGUI extends JPanel {

  private JTree  tree  = new JTree();
  private JLabel label = new JLabel("", JLabel.RIGHT);

  private DirectoryService service;
  private DefaultTreeModel model;

  /**
   * Constructor.
   *
   * @param service   directory service to use
   */
  public DirectoryServiceGUI(DirectoryService service) {
    this.service = service;

    setLayout(new BorderLayout());

    add(label, BorderLayout.NORTH);

    DirectoryServiceNode root = new DirectoryServiceNode(null, service.getRoot());
    model = new DefaultTreeModel(root);

    tree.addTreeWillExpandListener(new Listener());
    tree.setModel(model);
    add(new JScrollPane(tree), BorderLayout.CENTER);
  }

  /**
   * A node in the tree.
   */
  private class DirectoryServiceNode implements TreeNode {

    private Directory               directory;
    private DirectoryServiceNode    parent;
    private DirectoryServiceNode[]  children = null;

    /**
     * Constructor.
     *
     * @param parent      parent node
     * @param directory   directory represented by this node
     */
    public DirectoryServiceNode(DirectoryServiceNode parent,
                                Directory directory) {
      this.parent    = parent;
      this.directory = directory;
    }

    /**
     * String representation.
     */
    public String toString() {
      return directory.getName();
    }

    /**
     * Test if children are loaded.
     */
    public boolean childrenLoaded() {
      return children != null;
    }

    /**
     * Load the children.
     *
     * @return    <code>true</code> if children are loaded
     */
    public void loadChildren() throws DirectoryServiceException {
      try {
        Directory[] directories = service.getChildren(directory);
        children = new DirectoryServiceNode[directories.length];
        for (int d = 0; d < directories.length; d++) {
          children[d] = new DirectoryServiceNode(this, directories[d]);
        }
      } catch (DirectoryServiceException ex) {
        children = null;
        throw ex;
      }
    }

    /**
     * @see TreeNode
     */
    public TreeNode getChildAt(int childIndex) {
      return (TreeNode)children[childIndex];
    }

    /**
     * @see TreeNode
     */
    public int getChildCount() {
      if (children == null) {
        return 0;
      }
      return children.length;
    }

    /**
     * @see TreeNode
     */
    public TreeNode getParent() {
      return parent;
    }

    /**
     * @see TreeNode
     */
    public int getIndex(TreeNode node) {
      return Arrays.binarySearch(children, node);
    }

    /**
     * @see TreeNode
     */
    public boolean getAllowsChildren() {
      return !isLeaf();
    }

    /**
     * @see TreeNode
     */
    public boolean isLeaf() {
      return directory.isLeaf();
    }

    /**
     * @see TreeNode
     */
    public Enumeration children() {
      return Collections.enumeration(Arrays.asList(children));
    }
  }

  /**
   * The listener to tree expansion events.
   *
   * @see TreeWillExpandListener
   */
  private class Listener implements TreeWillExpandListener {

    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {

      DirectoryServiceNode node = (DirectoryServiceNode)event.getPath().getLastPathComponent();
      if (!node.childrenLoaded()) {
        try {
          tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          tree.setEnabled(false);

          node.loadChildren();
          model.nodeStructureChanged(node);
        } catch (DirectoryServiceException ex) {
          JOptionPane.showMessageDialog(DirectoryServiceGUI.this, ex.getMessage());
          throw new ExpandVetoException(event);
        } finally {
          tree.setEnabled(true);
          tree.setCursor(Cursor.getDefaultCursor());
        }
      }
    }

    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
    }
  }

  /**
   * Entrance to this complex demonstration.
   */
  public static void main(String[] args) {

    DirectoryService    service    = new UnreliableService(new LatencyService(new FileService()));
    DirectoryServiceGUI serviceGUI = new DirectoryServiceGUI((DirectoryService)Spin.off(service));

    JFrame frame = new JFrame("Directory Service");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(serviceGUI);
    frame.pack();
    frame.setVisible(true);
  }
}