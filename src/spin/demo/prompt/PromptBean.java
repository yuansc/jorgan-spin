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

/**
 * A common interface for a prompt.
 */
public interface PromptBean {

  /**
   * Size.
   *
   * @return  the size
   */
  public int size();

  /**
   * Get a value.
   *
   * @param index   index of value
   */
  public String get(int index);

  /**
   * Process.
   *
   * @param index   index of value to process
   */
  public void process(int index);

  /**
   * Process.
   *
   * @param prompt    the prompt to use for processing
   */
  public void process(Prompt prompt);

}
