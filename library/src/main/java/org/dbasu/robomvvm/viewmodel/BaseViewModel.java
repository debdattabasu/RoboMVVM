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

package org.dbasu.robomvvm.viewmodel;

import android.content.Context;

import com.google.common.base.Preconditions;

import org.dbasu.robomvvm.annotation.SetLayout;
import org.dbasu.robomvvm.binding.BindMode;
import org.dbasu.robomvvm.binding.Binding;
import org.dbasu.robomvvm.binding.DefaultValueConverter;
import org.dbasu.robomvvm.binding.ValueConverter;
import org.dbasu.robomvvm.componentmodel.Component;
import org.dbasu.robomvvm.componentmodel.EventArg;

/**
 * Abstract base class for view models. All view models are associated with a context
 * and must contain a {@link org.dbasu.robomvvm.annotation.SetLayout} annotation specifying the resource id to use
 * for its layout.
 */
public abstract class BaseViewModel extends Component {


    protected final Context context;


    /**
     * Construct a BaseViewModel from a context.
     * @param context
     *          The supplied context.
     */
    public BaseViewModel(Context context) {
        this.context = Preconditions.checkNotNull(context);
    }


    /**
     * Get the associated context.
     * @return
     *          The associated context.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Get the layout resource id associated with this view model. The id is
     * set using the {@link org.dbasu.robomvvm.annotation.SetLayout} annotation.
     * @return
     *          The associated resource Id.
     */
    public int getLayoutId() {
        Class thisClass = this.getClass();

        if(!thisClass.isAnnotationPresent(SetLayout.class)) {
            throw new RuntimeException("Missing InjectLayout annotation in " + thisClass.getName());
        }
        return ((SetLayout) thisClass.getAnnotation(SetLayout.class)).value();
    }


    /**
     * Override this in subclasses to set up the bindings between the view model and its layout.
     */
    protected void bind() {

    }

    /**
     * Bind a property of this view model to a property of an element in its layout.
     * @param property
     *          The property of the view model
     * @param elementId
     *          The id of the target element.
     * @param elementProperty
     *          The property of the target element.
     * @param valueConverter
     *          The value converter to use for conversion.
     * @param bindMode
     *          The bind mode to use.
     * @return
     *          The created binding.
     */
    protected abstract Binding bindProperty(String property, int elementId, String elementProperty, ValueConverter valueConverter, BindMode bindMode);


    /**
     * Binds an event of this view model to an action of an element in its layout.
     * @param elementId
     *          The id of the target element.
     * @param eventType
     *          The event arg class to bind.
     * @param action
     *          The action of the target element.
     * @return
     *          The created binding.
     */
    protected abstract Binding bindAction(int elementId, Class<? extends EventArg> eventType, String action);


    /**
     * Bind a property of this view model to a property of an element in its layout. Performs no conversion.
     * Uses {@link org.dbasu.robomvvm.binding.BindMode#SOURCE_TO_TARGET} which results in one-way
     * binding from this view model to the target element.
     *
     * @param property
     *          The property of the view model
     * @param elementId
     *          The id of the target element.
     * @param elementProperty
     *          The property of the target element.
     * @return
     *          The created binding.
     */
    protected final Binding bindProperty(String property, int elementId, String elementProperty) {

        return bindProperty(property, elementId, elementProperty, new DefaultValueConverter(), BindMode.SOURCE_TO_TARGET);
    }


    /**
     * Bind a property of this view model to a property of an element in its layout. Performs no conversion.
     * @param property
     *          The property of the view model
     * @param elementId
     *          The id of the target element.
     * @param elementProperty
     *          The property of the target element.
     * @param bindMode
     *          The bind mode to use.
     * @return
     *          The created binding.
     */
    protected final Binding bindProperty(String property, int elementId, String elementProperty, BindMode bindMode) {

        return bindProperty(property, elementId, elementProperty, new DefaultValueConverter(), bindMode);
    }
}
