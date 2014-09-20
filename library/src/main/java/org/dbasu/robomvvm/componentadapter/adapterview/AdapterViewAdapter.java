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

package org.dbasu.robomvvm.componentadapter.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.dbasu.robomvvm.componentadapter.view.ViewAdapter;
import org.dbasu.robomvvm.viewmodel.ViewModel;
import org.dbasu.robomvvm.viewmodel.ViewModelCollection;


/**
 *  A view adapter to adapt an AdapterView. Can bind {@link org.dbasu.robomvvm.viewmodel.ViewModelCollection}s to
 *  AdapterViews.
 */
public class AdapterViewAdapter extends ViewAdapter {

    protected ViewModelCollection source;

    /**
     * Set the source view model collection.
     * @param source
     *              The source view model collection.
     */
    public void setSource(ViewModelCollection source) {
        this.source = source;
        AdapterView adapterView = (AdapterView) targetObject;
        adapterView.setAdapter(this.source.getArrayAdapter());
    }



    /**
     * Set the view model to use when the source view model collection is empty.
     * @param value
     *              The empty view model.
     */
    public void setEmptyViewModel(ViewModel value) {
        AdapterView adapterView = (AdapterView) targetObject;
        View emptyView = value.createView();
        emptyView.setVisibility(View.GONE);
        ViewGroup parent = (ViewGroup)adapterView.getParent();
        int index = parent.indexOfChild(adapterView);
        parent.addView(emptyView, index + 1);
        adapterView.setEmptyView(emptyView);
    }


    /**
     * Set the layout to use when the source view model collection is empty.
     * @param layoutId
     *              The empty view layout id.
     */
    public void setEmptyViewLayout(int layoutId) {
        AdapterView adapterView = (AdapterView) targetObject;
        LayoutInflater inflater = (LayoutInflater) adapterView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View emptyView = inflater.inflate(layoutId, null);
        emptyView.setVisibility(View.GONE);
        ViewGroup parent = (ViewGroup)adapterView.getParent();
        int index = parent.indexOfChild(adapterView);
        parent.addView(emptyView, index + 1);
        adapterView.setEmptyView(emptyView);
    }


    private int selectedItem;


    private void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        raisePropertyChangeEvent("selectedItem");
    }

    /**
     * Get the position of the selected item.
     * @return
     */
    public int getSelectedItem() {
        return selectedItem;
    }

    @Override
    protected void adapt() {
        super.adapt();

        final AdapterView adapterView = (AdapterView) targetObject;

        if(adapterView.isFocusable()) {

            adapterView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setSelectedItem(position);
                    raiseEvent(new ItemSelectEventArg(AdapterViewAdapter.this, position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    setSelectedItem(-1);
                    raiseEvent(new ItemSelectEventArg(AdapterViewAdapter.this, -1));
                }
            });
        }
    }
}
