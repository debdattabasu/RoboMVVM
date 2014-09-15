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
import android.widget.ArrayAdapter;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import org.dbasu.robomvvm.componentmodel.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Refactoring of {@link org.dbasu.robomvvm.viewmodel.ViewModelCollection}.
 */
public class ViewModelCollection2<T extends ViewModel> extends Component {

    private final Context context;
    private final List<T> backingStore;
    private final List<Integer> checkedItems = new ArrayList<Integer>();
    private final ArrayAdapter<T> arrayAdapter;

    public ViewModelCollection2(Context context) {
        this.context = context;
        backingStore = new ArrayList<T>();
        arrayAdapter = new ViewModelArrayAdapter<T>(context, backingStore);
    }

    public ViewModelCollection2(Context context, List<T> backingStore) {
        this.context = context;
        this.backingStore = backingStore;
        arrayAdapter = new ViewModelArrayAdapter<T>(context, backingStore);
    }

    public List<T> getBackingStore() {
        return backingStore;
    }

    public ArrayAdapter<T> getArrayAdapter() {
        return arrayAdapter;
    }

    public void add(T item) {
        arrayAdapter.add(item);
    }

    public void addAll(Collection<T> collection) {
        arrayAdapter.addAll(collection);
    }

    public void addAll(T... items) {
        arrayAdapter.addAll(items);
    }

    public void insert(T item, int index) {
        arrayAdapter.insert(item, index);
    }

    public boolean contains(T item) {
        return backingStore.contains(item);
    }

    public boolean remove(T item) {
        boolean hasItem = contains(item);

        if(hasItem) {
            arrayAdapter.remove(item);
        }

        return hasItem;
    }

    public boolean removeAll(Collection<T> items) {
        boolean ret = false;

        for(T item : items) {
            ret |= remove(item);
        }

        return ret;
    }

    public boolean removeAll(T... items) {
        return removeAll(Arrays.asList(items));
    }

    public void clear() {
        arrayAdapter.clear();
    }

    public int getCount() {
        return arrayAdapter.getCount();
    }

    public T getItem(int position) {
        return arrayAdapter.getItem(position);
    }

    public int getPosition(T item) {
        return arrayAdapter.getPosition(item);
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
            raiseEvent(new ItemCheckedEventArg(this, position, false));
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
            raiseEvent(new ItemCheckedEventArg(this, position, true));
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
        raiseEvent(new ItemCheckedEventArg(this, position, true));
    }

    /**
     * Clear all items from the the list of checked items.
     */
    public void clearCheckedItems() {
        for(Integer i : checkedItems) {
            raiseEvent(new ItemCheckedEventArg(this, i, false));
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
