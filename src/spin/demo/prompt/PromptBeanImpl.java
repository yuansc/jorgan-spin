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
package spin.demo.prompt;

import java.util.ArrayList;
import java.util.List;

import spin.demo.Assert;

/**
 * Implementation of a prompt.
 */
public class PromptBeanImpl implements PromptBean {

  private List values = new ArrayList();

  /**
   * Constructor.
   */
  public PromptBeanImpl() {
    values.add("value1");
    values.add("value2");
    values.add("value3");
    values.add("value4");
    values.add("value5");
    values.add("value6");
  }

  /**
   * Size.
   *
   * @return  the size
   */
  public int size() {

    Assert.isNotEDT();

    return values.size();
  }

  /**
   * Get a value.
   *
   * @param index   index of value
   */
  public String get(int index) {

    Assert.isNotEDT();

    return (String)values.get(index);
  }

  /**
   * Process.
   *
   * @param index   index of value to process
   */
  public void process(int index) {

    Assert.isNotEDT();

    try {
      synchronized (this) {
        wait(2000);
      }
    } catch (InterruptedException ex) {
      // ignore
    }
  }

  /**
   * Process.
   *
   * @param prompt    the prompt to use
   */
  public void process(Prompt prompt) {

    Assert.isNotEDT();
    
    for (int i = values.size() - 1; i >= 0; i--) {
      String value = (String)values.get(i);
      if (prompt.prompt(value)) {
        try {
          synchronized (this) {
            wait(2000);
          }
        } catch (InterruptedException ex) {
          // ignore
        }
      }
    }
  }
}
