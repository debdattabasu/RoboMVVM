/**
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

package org.dbasu.robomvvm.sample_itemlist;


import android.content.Context;

import org.dbasu.robomvvm.annotation.SetLayout;
import org.dbasu.robomvvm.binding.BindMode;
import org.dbasu.robomvvm.viewmodel.ViewModel;
import org.dbasu.robomvvm.viewmodel.ViewModelCollection;
import org.dbasu.robomvvm.binding.TypedValueConverter;
import org.dbasu.robomvvm.componentadapter.view.ClickEventArg;
import org.dbasu.robomvvm.componentadapter.listview.ItemClickEventArg;
import org.dbasu.robomvvm.componentadapter.listview.ItemLongClickEventArg;
import java.util.ArrayList;
import java.util.List;

/**
 *  View model for the main activity.
 */
@SetLayout(R.layout.activity_main)
public class MainActivityViewModel  extends ViewModel {

    private final ViewModelCollection<StringViewModel> strings = new ViewModelCollection<StringViewModel>(getContext());
    private String text = "Hello World";

    private final StringViewModel emptyViewModel = new StringViewModel(getContext(), "List is empty. Click Add to add items.");


    public StringViewModel getEmptyViewModel() {
        return emptyViewModel;
    }

    public MainActivityViewModel(Context context) {
        super(context);
    }


    public ViewModelCollection<StringViewModel> getStrings() {
        return strings;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {

        this.text = text;
        raisePropertyChangeEvent("text");
    }

    public void itemClick(ItemClickEventArg arg) {

        if(strings.isItemChecked(arg.getPosition())) {
            strings.removeCheckedItem(arg.getPosition());
        } else {
            strings.setCheckedItem(arg.getPosition());
        }
        setText(strings.getItem(arg.getPosition()).getString());
    }

    public void itemLongClick(ItemLongClickEventArg arg) {

        if(strings.isItemChecked(arg.getPosition())) {
            strings.removeCheckedItem(arg.getPosition());
        } else {
            strings.addCheckedItem(arg.getPosition());
        }

        setText(strings.getItem(arg.getPosition()).getString());
    }

    public void addItem() {

        strings.add(new StringViewModel(getContext(), text));
        setText(strings.getItem(strings.getCount() - 1).getString());
        strings.setCheckedItem(strings.getCount() - 1);

    }

    public void deleteItem() {

        List<StringViewModel> dead = new ArrayList<StringViewModel>();

        for(Integer i : strings.getCheckedItems()) {
            dead.add(strings.getItem(i));
        }

        strings.removeAll(dead);
        strings.clearCheckedItems();

    }

    public void modifyItem() {

       for(Integer i : strings.getCheckedItems()) {
           strings.getItem(i).setString(text);
       }
    }

    @Override
    public void bind() {

        /**
         * Bind properties and actions.
         */
        bindAction(R.id.delete_button, ClickEventArg.class, "deleteItem");
        bindAction(R.id.add_button, ClickEventArg.class, "addItem");
        bindAction(R.id.modify_button, ClickEventArg.class, "modifyItem");
        bindAction(R.id.list_view, ItemClickEventArg.class, "itemClick");
        bindAction(R.id.list_view, ItemLongClickEventArg.class, "itemLongClick");


        bindProperty("text", R.id.text_main, "text", new TypedValueConverter(String.class, CharSequence.class), BindMode.BIDIRECTIONAL);
        bindProperty("strings", R.id.list_view, "source");
        bindProperty("emptyViewModel", R.id.list_view, "emptyViewModel");
    }
}
