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

package org.dbasu.robomvvm.componentadapter.listview;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.dbasu.robomvvm.componentadapter.adapterview.AdapterViewAdapter;
import org.dbasu.robomvvm.componentmodel.EventArg;
import org.dbasu.robomvvm.componentmodel.EventListener;
import org.dbasu.robomvvm.viewmodel.ViewModelCollection;
import org.dbasu.robomvvm.viewmodel.ItemCheckedEventArg;

/**
 * AdapterView adapter to adapt a ListView.
 */
public class ListViewAdapter extends AdapterViewAdapter {


    private ViewModelCollection itemSource;


    private final EventListener checkedChangeListener = new EventListener(ItemCheckedEventArg.class) {
        @Override
        public void invoke(EventArg args) {
            ItemCheckedEventArg checked = (ItemCheckedEventArg) args;
            ListView listView = (ListView) targetObject;
            listView.setItemChecked(checked.getPosition(), checked.getValue());

        }
    };

    /**
     * Set the source view model collection.
     * @param source
     *              The source view model collection.
     */
    @Override
    public void setSource(ViewModelCollection source) {

        super.setSource(source);
        if(itemSource != null) {
            itemSource.getEventSource().removeEventListener(checkedChangeListener);
        }

        itemSource = source;

        if(itemSource != null) {
            itemSource.getEventSource().addEventListener(checkedChangeListener);
        }

    }

    @Override
    protected void adapt() {
        super.adapt();

        ListView listView = (ListView) targetObject;


        if(listView.isClickable()) {

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    raiseEvent(new ItemClickEventArg(ListViewAdapter.this, position));
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    raiseEvent(new ItemLongClickEventArg(ListViewAdapter.this, position));
                    return true;
                }
            });

        }
    }
}
