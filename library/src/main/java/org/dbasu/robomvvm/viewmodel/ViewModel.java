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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;

import org.dbasu.robomvvm.binding.BindMode;
import org.dbasu.robomvvm.binding.Binding;
import org.dbasu.robomvvm.binding.ValueConverter;
import org.dbasu.robomvvm.componentmodel.ComponentAdapter;
import org.dbasu.robomvvm.componentmodel.EventArg;
import org.dbasu.robomvvm.util.ObjectTagger;
import org.dbasu.robomvvm.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * View model used for creating and binding views.
 */
public class ViewModel extends BaseViewModel {

    private static final String VIEW_MODEL = "robomvvm_view_model";
    private static final String VIEW_BINDINGS = "robomvvm_view_bindings";

    private View view = null;

    private final List<Binding> bindings = new ArrayList<Binding>();

    /**
     * Construct a ViewModel with a supplied context.
     * @param context
     *          The supplied context.
     */
    public ViewModel(Context context) {
        super(context);
    }


    /**
     * Attempt to unbind a pre-existing view from its view model and bind it to this
     * view model.
     * @param viewToConvert
     *      The view to convert.
     * @return
     *      Null if viewToConvert is null. Null if viewToConvert corresponds to a different view model class.
     *      The bound view otherwise.
     */
    public View convertView(View viewToConvert) {

        Preconditions.checkArgument(ThreadUtil.isUiThread(), "ViewModel.convertView can only be called from the UI thread");

        if(viewToConvert == null) return null;

        ViewModel otherViewModel = (ViewModel)  ObjectTagger.getTag(viewToConvert, VIEW_MODEL);


        if(otherViewModel == null || !otherViewModel.getClass().equals(this.getClass())) return null;


        List<Binding> otherViewBindings = (List<Binding>) ObjectTagger.getTag(viewToConvert, VIEW_BINDINGS);

        if(otherViewBindings != null) {

            for (Binding binding : otherViewBindings) {
                binding.unbind();
            }
        }

        this.view = viewToConvert;
        bind();

        ObjectTagger.setTag(viewToConvert, VIEW_MODEL, this);
        ObjectTagger.setTag(viewToConvert, VIEW_BINDINGS, new ArrayList<Binding>(bindings));
        this.view = null;
        bindings.clear();
        return viewToConvert;
    }


    /**
     * Create a view corresponding to this view model. Created with a null parent. The View Model is stored as a tag on the root View using
     * {@link org.dbasu.robomvvm.util.ObjectTagger}. This makes sure that the View Model is kept alive as long as the View is alive.
     * @return
     *          The created view.
     */
    public View createView() {
        return createView(null);
    }


    /**
     * Create a view corresponding to this view model. The View Model is stored as a tag on the root View using
     * {@link org.dbasu.robomvvm.util.ObjectTagger}. This makes sure that the View Model is kept alive as long as the View is alive.
     * @param parent
     *          Parent to attach the created view to.
     * @return
     *          The created view.
     */
    public View createView(ViewGroup parent) {

        Preconditions.checkArgument(ThreadUtil.isUiThread(), "ViewModel.createView can only be called from the UI thread");

        int layoutId = getLayoutId();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewToConvert = inflater.inflate(layoutId, parent, false);

        ObjectTagger.setTag(viewToConvert, VIEW_MODEL, this);

        return convertView(viewToConvert);
    }


    /**
     * Bind a property of this view model to a property of a view in its layout.
     * @param property
     *          The property of the view model
     * @param viewId
     *          The id of the target view.
     * @param viewProperty
     *          The property of the target view.
     * @param valueConverter
     *          The value converter to use for conversion.
     * @param bindMode
     *          The bind mode to use.
     * @return
     *          The created binding.
     */
    @Override
    protected final Binding bindProperty(String property, int viewId, String viewProperty, ValueConverter valueConverter, BindMode bindMode) {

        Preconditions.checkArgument(ThreadUtil.isUiThread(), "ViewModel.bindProperty can only be called from the UI thread");
        Preconditions.checkNotNull(property);
        Preconditions.checkNotNull(viewProperty);
        Preconditions.checkNotNull(valueConverter);
        Preconditions.checkNotNull(bindMode);

        View targetView = view.findViewById(viewId);
        ComponentAdapter adapter = ComponentAdapter.get(targetView);
        Binding binding = Binding.bindProperty(this, property, adapter, viewProperty, valueConverter, bindMode);
        bindings.add(binding);

        return binding;
    }


    /**
     * Binds an event of this view model to an action of a view in its layout.
     * @param viewId
     *          The id of the target view.
     * @param eventType
     *          The event arg class to bind.
     * @param action
     *          The action of the target view.
     * @return
     *          The created binding.
     */
    @Override
    protected Binding bindAction(int viewId, Class<? extends EventArg> eventType, String action) {

        Preconditions.checkArgument(ThreadUtil.isUiThread(), "ViewModel.bindAction can only be called from the UI thread");
        Preconditions.checkNotNull(eventType);
        Preconditions.checkNotNull(action);

        View targetView = view.findViewById(viewId);
        ComponentAdapter adapter = ComponentAdapter.get(targetView);
        Binding binding =  Binding.bindAction(adapter, this, eventType, action);
        bindings.add(binding);
        return binding;
    }

}
