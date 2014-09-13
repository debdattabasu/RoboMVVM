/**
 * @project RoboMVVM
 * @project RoboMVVM(https://github.com/debdattabasu/RoboMVVM)
 * @author Debdatta Basu
 *
 * @license 3-clause BSD license(http://opensource.org/licenses/BSD-3-Clause).
 *      Copyright (c) 2014, Debdatta Basu. All rights reserved.
 *
 *      Redistribution and use in source and binary forms, with or without modification, are permitted provided that
 *      the following conditions are met:
 *
 *          1. Redistributions of source code must retain the above copyright notice, this list of
 *             conditions and the following disclaimer.
 *
 *          2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *             conditions and the following disclaimer in the documentation and/or other materials
 *             provided with the distribution.
 *
 *          3. Neither the name of the copyright holder nor the names of its contributors may be used
 *             to endorse or promote products derived from this software without specific prior
 *             written permission.
 *
 *      THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 *      INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *      IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 *      OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 *      OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *      OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 */
package org.dbasu.robomvvm.componentmodel;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for events, properties, and actions. Add an {@link org.dbasu.robomvvm.componentmodel.EventListener} to listen
 * for events of a specific type. Use {@link org.dbasu.robomvvm.binding.Binding} to bind a property in a
 * source component to a property in a target component, or an event in a source component to an action in a
 * target component.
 */
public class Component  {

    private final List<EventListener> listenerList = new ArrayList<EventListener>();

    /**
     * Adds an event listener to this component.
     *
     * @param listener
     *          The listener to add.
     */
    public synchronized void addEventListener(EventListener listener) {

        Preconditions.checkNotNull(listener);

        if(!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    /**
     * Checks whether an event listener is already added to this component.
     *
     * @param listener
     *          The event listener to check for.
     * @return
     *          True if the listener has been already added. False otherwise.
     */
    public synchronized boolean hasEventListener(EventListener listener) {
        Preconditions.checkNotNull(listener);

        return listenerList.contains(listener);
    }

    /**
     * Removes an event listener from this component.
     *
     * @param listener
     *          The event listener to remove.
     * @return
     *          True if the listener existed and was removed. False otherwise.
     */
    public synchronized boolean removeEventListener(EventListener listener) {

        Preconditions.checkNotNull(listener);

        return listenerList.remove(listener);
    }

    /**
     * Gets all attached event listeners of a particular type.
     *
     * @param type
     *          The to event arg class to query for.
     * @param <T>
     *          Generic parameter representing the event arg class.
     * @return
     *          List of all event listeners corresponding to the given type, or an empty list
     *          if no corresponding listeners are found.
     */
    public synchronized <T extends EventArg> List<EventListener> getEventListenersOfType(final Class<T> type) {

        Preconditions.checkNotNull(type);

        Collection<EventListener> ret = Collections2.filter(listenerList, new Predicate<EventListener>() {
            @Override
            public boolean apply(EventListener input) {

                return ClassUtils.isAssignable(type, input.getEventType(), true);
            }
        });

        return new ArrayList<EventListener>(ret);
    }

    /**
     * Raise an event with an event arg. All event listeners corresponding to the type of the supplied event arg
     * are notified.
     *
     * @param args
     *          The supplied event arg.
     */
    public void raiseEvent(EventArg args) {
        Preconditions.checkNotNull(args);

        List<EventListener> listenerList = this.getEventListenersOfType(args.getClass());

        for(EventListener l : listenerList) {
            l.invoke(args);
        }
    }

    /**
     * Raises an event of type {@link org.dbasu.robomvvm.componentmodel.PropertyChangeEventArg}.
     *
     * @param propertyName
     *          The name of the changed property.
     */
    public void raisePropertyChangeEvent(String propertyName) {
        Preconditions.checkNotNull(propertyName);

        raiseEvent(new PropertyChangeEventArg(this, propertyName));
    }


    /**
     * Sets a property with a given name to a value. A setter function is chosen based on the supplied property name
     * and the type of the supplied value. For example, if the name of the property is "foo" and the supplied value is
     * of type int, this will attempt to call {@code setFoo(int)}.
     *
     * @throws java.lang.RuntimeException
     *          When a corresponding setter function is not found in this component.
     * @param name
     *          The name of the property.
     * @param value
     *          The value to set the property to.
     */
    public void setProperty(String name, Object value) {
        Preconditions.checkNotNull(name);

        PropertyManager.get().setProperty(this, name, value);
    }


    /**
     * Gets the value of a property with a given name. A getter function is called corresponding to the supplied
     * property name. For example, if the supplied property name is "foo", this will attempt to call {@code getFoo()}.
     *
     * @throws java.lang.RuntimeException
     *          When a corresponding getter function is not found in this component.
     * @param name
     *          The name of the property.
     * @return
     *          The value of the property obtained by the getter function.
     */
    public Object getProperty(String name) {
        Preconditions.checkNotNull(name);
        return PropertyManager.get().getProperty(this, name);
    }


    /**
     * Invoke an action with a supplied name and event arg. Calls all functions in this component whose names match the
     * supplied name, have a void return type, and have either a single argument of a type compatible with the supplied event arg,
     * or no arguments.
     *
     * @throws java.lang.RuntimeException
     *          When a corresponding action function is not found in this component.
     * @param name
     *          The name of the action function.
     * @param eventArg
     *          The supplied argument. All action functions with the given name and an argument type matching this eventArg's type
     *          are called.
     */
    public void invokeAction(String name, EventArg eventArg) {
        Preconditions.checkNotNull(name);
        ActionManager.get().invokeAction(this, name, eventArg);
    }


    /**
     * Invoke an action with a supplied name and no arguments. Calls a function in this component whose name matches the
     * supplied name, has a void return type, and has no arguments.
     *
     * @throws java.lang.RuntimeException
     *          When a corresponding action function is not found in this component.
     * @param name
     *          The name of the action function.
     */
    public void invokeAction(String name) {
        invokeAction(name, null);
    }

    @Override
    protected void finalize() throws Throwable {
        raiseEvent(new GarbageCollectionEventArg(this));
        super.finalize();
    }

}
