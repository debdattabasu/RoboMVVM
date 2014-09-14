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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.common.base.Preconditions;

import org.dbasu.robomvvm.binding.BindMode;
import org.dbasu.robomvvm.binding.Binding;
import org.dbasu.robomvvm.binding.ValueConverter;
import org.dbasu.robomvvm.componentmodel.ComponentAdapter;
import org.dbasu.robomvvm.componentmodel.EventArg;
import org.dbasu.robomvvm.util.ObjectTagger;
import org.dbasu.robomvvm.util.ThreadUtil;

/**
 * View model used for creating and binding menus.
 */
public class MenuViewModel extends BaseViewModel {

    private static final String VIEW_MODEL = "robomvvm_view_model";

    private Menu menu = null;

    /**
     * Construct a MenuViewModel with a supplied context.
     * @param context
     *          The supplied context.
     */
    public MenuViewModel(Context context) {
        super(context);
    }


    private int getNumItems(Menu menu) {
        int ret = 0;

        while (true) {
            try {
                menu.getItem(ret);
            } catch (IndexOutOfBoundsException e) {
                return ret;
            }

            ret ++;
        }
    }


    private MenuItem getItemAt(Menu menu, int index) {
        try {
            return menu.getItem(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


    /**
     * Inflate the menu corresponding to this view model. The View Model is stored as a tag on the root MenuItem using
     * {@link org.dbasu.robomvvm.util.ObjectTagger}. This makes sure that the View Model is kept alive as long as the
     * MenuItem is alive.
     * @param menu
     *      The menu to inflate to.
     */
    public void inflate(Menu menu) {

        int currNumItems = getNumItems(menu);
        MenuInflater inflater = new MenuInflater(context);
        int menuId = getLayoutId();

        inflater.inflate(menuId, menu);

        MenuItem root = getItemAt(menu, currNumItems);

        if(root != null) {
            ObjectTagger.setTag(root, VIEW_MODEL, this);
            this.menu = menu;
            bind();
            this.menu = null;
        }
    }


    /**
     * Bind a property of this view model to a property of a menu item in its layout.
     * @param property
     *          The property of the view model
     * @param menuId
     *          The id of the target menu item.
     * @param menuProperty
     *          The property of the target menu item.
     * @param valueConverter
     *          The value converter to use for conversion.
     * @param bindMode
     *          The bind mode to use.
     * @return
     *          The created binding.
     */
    @Override
    protected final Binding bindProperty(String property, int menuId, String menuProperty, ValueConverter valueConverter, BindMode bindMode) {

        Preconditions.checkArgument(ThreadUtil.isUiThread(), "MenuViewModel.bindProperty can only be called from the UI thread");
        Preconditions.checkNotNull(property);
        Preconditions.checkNotNull(menuProperty);
        Preconditions.checkNotNull(valueConverter);
        Preconditions.checkNotNull(bindMode);

        MenuItem targetItem = menu.findItem(menuId);
        ComponentAdapter adapter = ComponentAdapter.get(targetItem);
        Binding binding = Binding.bindProperty(this, property, adapter, menuProperty, valueConverter, bindMode);

        return binding;
    }


    /**
     * Binds an event of this view model to an action of a menu item in its layout.
     * @param menuId
     *          The id of the target menu item.
     * @param eventType
     *          The event arg class to bind.
     * @param action
     *          The action of the target menu item.
     * @return
     *          The created binding.
     */
    @Override
    protected Binding bindAction(int menuId, Class<? extends EventArg> eventType, String action) {

        Preconditions.checkArgument(ThreadUtil.isUiThread(), "MenuViewModel.bindAction can only be called from the UI thread");
        Preconditions.checkNotNull(eventType);
        Preconditions.checkNotNull(action);

        MenuItem targetItem = menu.findItem(menuId);
        ComponentAdapter adapter = ComponentAdapter.get(targetItem);
        Binding binding =  Binding.bindAction(adapter, this, eventType, action);

        return binding;
    }
}
