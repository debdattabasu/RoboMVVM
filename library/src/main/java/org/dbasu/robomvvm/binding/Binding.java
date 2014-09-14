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

package org.dbasu.robomvvm.binding;

import com.google.common.base.Preconditions;

import org.dbasu.robomvvm.componentmodel.Component;
import org.dbasu.robomvvm.componentmodel.GarbageCollectionEventArg;
import org.dbasu.robomvvm.componentmodel.EventListener;
import org.dbasu.robomvvm.componentmodel.EventArg;

import java.lang.ref.WeakReference;

/**
 * Allows binding of properties and actions between {@link org.dbasu.robomvvm.componentmodel.Component}s.
 * Maintains weak references to both the source component and the target component, allowing them to be
 * garbage collected even when the binding is alive. When either the source component or the target component
 * is garbage collected, the binding is automatically unbound.
 */
public class Binding {

    /**
     * Binds an event in the source component to an action in the target component. When an event of type eventType
     * is raised by the source component, {@link org.dbasu.robomvvm.componentmodel.Component#invokeAction} is called
     * on the target component.
     *
     * @param source
     *          The source component.
     * @param target
     *          The target component.
     * @param eventType
     *          The event type in the source component that triggers the action.
     * @param action
     *          The action method name in the target component. The action method may take an argument of the supplied
     *          eventType, or may take no arguments.
     * @return
     *          The binding created by this call.
     */
    public static Binding bindAction(Component source, Component target, Class<? extends EventArg> eventType, String action) {

        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(eventType);
        Preconditions.checkNotNull(action);

        Binding ret = new ActionBinding(source, target, eventType, action);
        ret.bind();
        return ret;

    }


    /**
     * Binds a property in the source component to a property in the target component. The directionality of the binding is specified
     * using {@link org.dbasu.robomvvm.binding.BindMode}, and conversions between the source value and the target value are carried
     * out using a {@link org.dbasu.robomvvm.binding.ValueConverter}.
     *
     * @param source
     *          The source component.
     * @param sourceProperty
     *          The source property name.
     * @param target
     *          The target component.
     * @param targetProperty
     *          The target property name.
     * @param valueConverter
     *          The value converter to convert between source and target properties.
     * @param bindMode
     *          The bind mode to be used for this binding.
     * @return
     *          The binding created by this call.
     */

    public static Binding bindProperty(Component source, String sourceProperty, Component target, String targetProperty, ValueConverter valueConverter, BindMode bindMode) {

        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(sourceProperty);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(targetProperty);
        Preconditions.checkNotNull(valueConverter);
        Preconditions.checkNotNull(bindMode);

        Binding ret = new PropertyBinding(source, sourceProperty, target, targetProperty, valueConverter, bindMode);
        ret.bind();
        return ret;
    }

    /**
     * Binds a property in the source component to a property in the target component. Changes to the source property
     * are reflected in the target property, but not the other way round. This corresponds to
     * {@link org.dbasu.robomvvm.binding.BindMode#SOURCE_TO_TARGET}. A {@link org.dbasu.robomvvm.binding.DefaultValueConverter} is used.
     *
     * @param source
     *          The source component.
     * @param sourceProperty
     *          The source property name.
     * @param target
     *          The target component.
     * @param targetProperty
     *          The target property name.
     * @return
     *          The binding created by this call.
     */

    public static Binding bindProperty(Component source, String sourceProperty, Component target, String targetProperty) {

        return bindProperty(source, sourceProperty, target, targetProperty, new DefaultValueConverter(), BindMode.SOURCE_TO_TARGET);

    }

    /**
     * Binds a property in the source component to a property in the target component. The directionality of the binding is specified
     * using {@link org.dbasu.robomvvm.binding.BindMode}. No conversions between the source and the target values is
     * performed.
     *
     * @param source
     *          The source component.
     * @param sourceProperty
     *          The source property name.
     * @param target
     *          The target component.
     * @param targetProperty
     *          The target property name.
     * @param bindMode
     *          The bind mode to be used for this binding.
     * @return
     *          The binding created by this call.
     */

    public static Binding bindProperty(Component source, String sourceProperty, Component target, String targetProperty, BindMode bindMode) {

        return bindProperty(source, sourceProperty, target, targetProperty, new DefaultValueConverter(), bindMode);

    }

    /**
     * Unbind this binding. This method is automatically
     * called when the source or the target component is garbage collected.
     */
    public void unbind() {
        Component source = getSource();
        Component target = getTarget();

        if(source != null) {
            source.removeEventListener(garbageCollectionListener);
        }

        if(target != null) {
            target.removeEventListener(garbageCollectionListener);
        }

        bound = false;
    }

    /**
     * Returns whether the binding is currently bound.
     * @return
     *          True is the binding is currently bound. False otherwise.
     */
    public boolean isBound() {
        return bound;
    }

    /**
     * Gets the source component
     * @return
     *          The source component.
     */
    public Component getSource() {
        return weakSourceReference.get();
    }

    /**
     * Gets the target component
     * @return
     *          The target Component.
     */
    public Component getTarget() {
        return weakTargetReference.get();
    }

    private final WeakReference<Component> weakSourceReference;
    private final WeakReference<Component> weakTargetReference;

    private final EventListener garbageCollectionListener = new EventListener(GarbageCollectionEventArg.class) {
        @Override
        public void invoke(EventArg args) {
            unbind();
        }
    };

    private boolean bound = false;

    protected Binding(Component source, Component target) {
        this.weakSourceReference = new WeakReference<Component>(source);
        this.weakTargetReference = new WeakReference<Component>(target);

    }

    protected void bind() {

        Component source = getSource();
        Component target = getTarget();

        if(source != null) {
            source.addEventListener(garbageCollectionListener);
        }

        if(target != null) {
            target.addEventListener(garbageCollectionListener);
        }
        bound = true;
    }
}
