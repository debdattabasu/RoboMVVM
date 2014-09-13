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
import org.dbasu.robomvvm.componentmodel.EventArg;
import org.dbasu.robomvvm.componentmodel.EventListener;

/**
 * Package private class implementing the logic for creating an action binding.
 */
class ActionBinding extends Binding {

    private final Class<? extends EventArg> eventType;
    private final String action;
    private final EventListener eventListener;

    ActionBinding(Component source, Component target, Class<? extends EventArg> eventType, final String action) {
        super(source, target);

        this.eventType = eventType;
        this.action = action;

        eventListener = new EventListener(eventType) {
            @Override
            public void invoke(EventArg args) {

                Component target = getTarget();
                if(target == null) return;

                target.invokeAction(action, args);
            }
        };
    }

    @Override
    protected void bind() {

        Component source = getSource();
        if(source != null) {
            source.addEventListener(eventListener);
        }

        super.bind();
    }

    @Override
    public void unbind() {

        Component source = getSource();
        if(source != null) {
            source.removeEventListener(eventListener);
        }

        super.unbind();
    }
}
