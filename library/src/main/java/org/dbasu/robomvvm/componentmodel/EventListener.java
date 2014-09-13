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

/**
 * An event listener that listens to events of a particular {@link org.dbasu.robomvvm.componentmodel.EventArg} class.
 */
public abstract class EventListener {

    final Class<? extends EventArg> eventType;

    /**
     * Construct an event listener of a particular event arg class.
     *
     * @param eventType
     *          The event arg class to listen for.
     * @param <T>
     *          Generic parameter representing the event arg class to listen for.
     */
    public <T extends EventArg> EventListener(Class<T> eventType) {

        this.eventType = Preconditions.checkNotNull(eventType);
    }

    /**
     * Gets the event arg class that this event listener listens for.
     *
     * @return
     *          The event arg class.
     */
    public Class<? extends EventArg> getEventType() {
        return eventType;
    }

    /**
     * Abstract function that is called when an event of the associated event arg class is invoked.
     * Override in subclasses to implement custom listener behavior.
     * @param args
     *          The event arg instance.
     */
    public abstract void invoke(EventArg args);
}
