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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import org.dbasu.robomvvm.componentmodel.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A collection of {@link org.dbasu.robomvvm.viewmodel.ViewModel}s. Can be used as an array adapter in adapter views.
 * Has an event source {@link org.dbasu.robomvvm.componentmodel.Component} to notify listeners of changing item checked states
 * using {@link org.dbasu.robomvvm.viewmodel.ItemCheckedEventArg}.
 */
public class ViewModelCollection<T extends ViewModel> extends ArrayAdapter<T> {

    private int selectedItem = -1;
    private final List<Integer> checkedItems = new ArrayList<Integer>();
    private final Component eventSource = new Component();


    /**
     * Get the event source component that belongs to this view model collection.
     * @return
     *          The event source component.
     */
    public Component getEventSource() {
        return eventSource;
    }


    /**
     * Construct a ViewModelCollection with a supplied context using an internal
     * list of view models as a backing store.
     *
     * @param context
     *              The supplied context.
     */
    public ViewModelCollection(Context context) {
        super(context, 0);
    }

    /**
     * Construct a ViewModelCollection with a supplied context using a supplied list of view models
     * as a backing store.
     *
     * @param context
     *              The supplied context.
     * @param objects
     *              The list of view models to use as a backing store.
     */
    public ViewModelCollection(Context context, List<T> objects) {
        super(context, 0, objects);
    }


    /**
     * Remove all the items contained in a collection from this view model collection.
     * @param items
     *              A collection representing the items to remove from this view model collection.
     */
    public void removeAll(Collection<T> items) {

        Preconditions.checkNotNull(items);
        for(T item : items) {
            remove(item);
        }
    }

    /**
     * Remove all the items in the argument list from this view model collection.
     * @param items
     *              The items to remove from this view model collection.
     */
    public void removeAll(T... items) {
        removeAll(Arrays.asList(items));
    }


    private View getCustomView(int position, View convertView, ViewGroup parent) {
        T viewModel = this.getItem(position);

        View view = viewModel.convertView(convertView);
        if(view != null) return view;

        return viewModel.getView(parent);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView (int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    /**
     * Returns a list of positions of checked items in this view model collection.
     * @return
     *              A list of positions of checked items in this view model collection.
     *              Empty list if nothing is checked.
     */
    public List<Integer> getCheckedItems() {
        return new ArrayList<Integer>(checkedItems);
    }

    /**
     * Checks if an item at the given position is checked.
     * @param position
     *              The position of the item.
     * @return
     *              True if the item at the given position is checked. False otherwise.
     */
    public boolean isItemChecked(int position) {

        return checkedItems.contains(Integer.valueOf(position));
    }

    /**
     * Remove the item at the given position from the list of checked items.
     * @param position
     *              The item position.
     * @return
     *              True if the item at the given position was checked. False otherwise.
     */
    public boolean removeCheckedItem(int position) {

        boolean removed = checkedItems.remove(Integer.valueOf(position));

        if(removed) {
            eventSource.raiseEvent(new ItemCheckedEventArg(eventSource, position, false));
        }

        return removed;
    }

    /**
     * Remove all the the items at positions given by a collection from the list of checked items.
     * @param positions
     *              A collection representing the positions of the checked items to remove.
     * @return
     *              True is any of the items given by the positions collection were checked.
     *              False otherwise.
     */
    public boolean removeCheckedItems(Collection<Integer> positions) {

        Preconditions.checkNotNull(positions);
        boolean ret = false;

        for(Integer position : positions) {
            boolean removed  = removeCheckedItem(position);
            ret |= removed;
        }

        return ret;

    }

    /**
     * Gets the position of the selected item.
     * @return
     *          The position of the selected item.
     */
    public int getSelectedItem() {
        return selectedItem;
    }


    /**
     * Remove all the the items at positions given by the argument list from the list of checked items.
     * @param positions
     *              The positions of the checked items to remove.
     * @return
     *              True is any of the items given by the positions were checked.
     *              False otherwise.
     */
    public boolean removeCheckedItems(int... positions) {
        return removeCheckedItems(Ints.asList(positions));
    }

    /**
     * Add the item at a give position to the list of checked items.
     * @param position
     *              The item position.
     */
    public void addCheckedItem(int position) {

        if(!isItemChecked(position)) {
            checkedItems.add(position);
            eventSource.raiseEvent(new ItemCheckedEventArg(eventSource, position, true));
        }
    }

    /**
     * Add all the items at positions given by a collection to the list of checked items.
     * @param positions
     *              A collection representing the item positions.
     */
    public void addCheckedItems(Collection<Integer> positions) {

        Preconditions.checkNotNull(positions);

        for(Integer position : positions) {
            addCheckedItem(position);
        }
    }

    /**
     * Add all the items at positions given by the argument list to the list of checked items.
     * @param positions
     *              The item positions.
     */
    public void addCheckedItems(int... positions) {
        addCheckedItems(Ints.asList(positions));
    }

    /**
     * Set the item at a give position as the only checked item. Remove any items that were previously checked
     * from the list of checked items.
     * @param position
     *              The item position.
     */
    public void setCheckedItem(int position) {
        clearCheckedItems();
        checkedItems.add(position);
        eventSource.raiseEvent(new ItemCheckedEventArg(eventSource, position, true));
    }

    /**
     * Clear all items from the the list of checked items.
     */
    public void clearCheckedItems() {
        for(Integer i : checkedItems) {
            eventSource.raiseEvent(new ItemCheckedEventArg(eventSource, i, false));
        }
        checkedItems.clear();
    }

    /**
     * Gets the most recently checked item.
     * @return
     *              Most recently checked item. -1 if nothing is checked.
     */
    public int getCheckedItem() {
        int index = checkedItems.size() -1;
        return index == -1? -1 : checkedItems.get(index);
    }

}
