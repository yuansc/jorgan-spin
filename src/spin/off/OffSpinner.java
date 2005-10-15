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
package spin.off;

import javax.swing.SwingUtilities;

import spin.Invocation;
import spin.Spinner;

/**
 * A spinner for spin-off, i.e. all invocations are evaluated on another thread
 * then the EDT while further events are dispatched.
 * 
 * @see spin.off.Starter
 * @see spin.off.DispatcherFactory
 */
public class OffSpinner extends Spinner {

    /**
     * Default factory of dispatchers.
     */
    private static DispatcherFactory defaultDispatcherFactory = new AWTReflectDispatcherFactory();

    /**
     * Default starter for asynchronous evaluation.
     */
    private static Starter defaultStarter = new SimpleStarter();

    /**
     * The factory of dispatchers.
     */
    private DispatcherFactory dispatcherFactory;

    /**
     * The starter for asynchronous evaluation.
     */
    private Starter starter;

    /**
     * Create a spinner for spin-off using the default dispatcherFactory and starter.
     *
     * @see #setDefaultStarter(Starter)
     * @see #setDefaultDispatcherFactory(DispatcherFactory)
     */
    public OffSpinner() {
        this(defaultDispatcherFactory, defaultStarter);
    }

    /**
     * Create a spinner for spin-off using the default starter.
     * 
     * @param dispatcherFactory factory of dispatchers
     * @see #setDefaultStarter(Starter)
     */
    public OffSpinner(DispatcherFactory dispatcherFactory) {
        this(dispatcherFactory, defaultStarter);
    }

    /**
     * Create a spinner for spin-off using the default dispatcherFactory.
     * 
     * @param starter   starter
     * @see #setDefaultDispatcherFactory(DispatcherFactory)
     */
    public OffSpinner(Starter starter) {
        this(defaultDispatcherFactory, starter);
    }

    /**
     * Create a spinner for spin-off.
     *  
     * @param dispatcherFactory factory of dispatchers
     * @param starter           starter
     */
    public OffSpinner(DispatcherFactory dispatcherFactory, Starter starter) {
        this.dispatcherFactory = dispatcherFactory;
        this.starter = starter;
    }

    /**
     * Spin the given invocation off the EDT.
     * 
     * @param invocation   invocation to spin-off
     */
    public final void spin(final Invocation invocation) throws Throwable {

        if (SwingUtilities.isEventDispatchThread()) {
            final Dispatcher dispatcher = dispatcherFactory.createDispatcher();

            starter.start(new Runnable() {
                public void run() {
                    invocation.evaluate();

                    dispatcher.stop();
                }
            });

            dispatcher.start();

            if (!invocation.isEvaluated()) {
                throw new Error("dispatcher stopped prematurely");
            }
        } else {
            invocation.evaluate();
        }
    }

    /**
     * Get the default dispatcher factory.
     * 
     * @return  default factory of dispatchers 
     */
    public static DispatcherFactory getDefaultDispatcherFactory() {
        return defaultDispatcherFactory;
    }

    /**
     * Set the default dispatcher factory.
     * 
     * @param dispatcherFactory     the factory of dispatchers to use as default
     */
    public static void setDefaultDispatcherFactory(
            DispatcherFactory dispatcherFactory) {
        OffSpinner.defaultDispatcherFactory = dispatcherFactory;
    }

    /**
     * Get the default starter.
     * 
     * @return  default starter
     */
    public static Starter getDefaultStarter() {
        return defaultStarter;
    }

    /**
     * Set the default starter.
     * 
     * @param starter   the starter to use as default
     */
    public static void setDefaultStarter(Starter starter) {
        OffSpinner.defaultStarter = starter;
    }
}