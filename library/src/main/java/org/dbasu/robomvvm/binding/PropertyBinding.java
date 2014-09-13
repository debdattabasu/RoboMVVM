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

import org.dbasu.robomvvm.componentmodel.Component;
import org.dbasu.robomvvm.componentmodel.PropertyChangeEventArg;
import org.dbasu.robomvvm.componentmodel.EventArg;
import org.dbasu.robomvvm.componentmodel.EventListener;


/**
 * Package private class implementing the logic for creating a property binding.
 */
class PropertyBinding extends Binding {

    private final String sourceProperty;
    private final String targetProperty;
    private final ValueConverter valueConverter;
    private final BindMode bindMode;


    PropertyBinding(Component source, String sourceProperty, Component target, String targetProperty, ValueConverter converter, BindMode bindMode) {
        super(source, target);
        this.sourceProperty = sourceProperty;
        this.targetProperty = targetProperty;
        this.valueConverter = converter;
        this.bindMode = bindMode;

    }

    private final EventListener targetChangeListener = new EventListener(PropertyChangeEventArg.class) {
        @Override
        public void invoke(EventArg args) {
            String name = ((PropertyChangeEventArg) args).getPropertyName();

            if(!name.equals(targetProperty)) return;

            Component source = getSource();
            Component target = getTarget();

            if(source == null || target == null) return;


            boolean hasListener = source.removeEventListener(sourceChangeListener);

            Object value = target.getProperty(targetProperty);
            value = valueConverter.convertToSource(value);

            source.setProperty(sourceProperty, value);

            if(hasListener) {
                source.addEventListener(sourceChangeListener);
            }

        }
    };

    private final EventListener sourceChangeListener = new EventListener(PropertyChangeEventArg.class) {
        @Override
        public void invoke(EventArg args) {
            String name = ((PropertyChangeEventArg) args).getPropertyName();

            if(!name.equals(sourceProperty)) return;

            Component source = getSource();
            Component target = getTarget();

            if(source == null || target == null) return;

            boolean hasListener = target.removeEventListener(targetChangeListener);

            Object value = source.getProperty(sourceProperty);

            value = valueConverter.convertToTarget(value);

            target.setProperty(targetProperty, value);

            if(hasListener) {
                target.addEventListener(targetChangeListener);
            }
        }
    };


    @Override
    public void unbind() {

        Component source = getSource();
        Component target = getTarget();

        if(source != null) {
            source.removeEventListener(sourceChangeListener);
        }

        if(target != null) {
            target.removeEventListener(targetChangeListener);
        }

        super.unbind();
    }


    @Override
    protected void bind() {

        Component source = getSource();
        Component target = getTarget();


        if(source!= null && bindMode.canBindSourceToTarget()) {
            source.addEventListener(sourceChangeListener);
            sourceChangeListener.invoke(new PropertyChangeEventArg(source, sourceProperty));
        }

        if(target != null && bindMode.canBindTargetToSource()) {
            target.addEventListener(targetChangeListener);
            targetChangeListener.invoke(new PropertyChangeEventArg(target, targetProperty));
        }

        super.bind();
    }
}
