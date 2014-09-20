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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.dbasu.robomvvm.componentadapter.adapterview.AdapterViewAdapter;
import org.dbasu.robomvvm.componentmodel.EventArg;
import org.dbasu.robomvvm.componentmodel.EventListener;
import org.dbasu.robomvvm.viewmodel.ItemCheckedEventArg;
import org.dbasu.robomvvm.viewmodel.ViewModelCollection;

/**
 * AdapterView adapter to adapt a ListView.
 */
public class ListViewAdapter extends AdapterViewAdapter {

    /**
     * The view is not scrolling. Note navigating the list using the trackball counts as being in the
     * idle state since these transitions are not animated.
     */
    public static final int SCROLL_STATE_IDLE = 0;

    /**
     * The view is not scrolling. Note navigating the list using the trackball counts
     * as being in the idle state since these transitions are not animated.
     */
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

    /**
     * The user had previously been scrolling using touch and had performed a fling. The animation
     * is now coasting to a stop.
     */
    public static final int SCROLL_STATE_FLING = 2;

    private int scrollState = 0;


    private void setScrollState(int scrollState) {
        this.scrollState = scrollState;
        raisePropertyChangeEvent("scrollState");
    }


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
        if(this.source != null) {
            this.source.removeEventListener(checkedChangeListener);
        }

        super.setSource(source);

        if(this.source != null) {
            super.source.addEventListener(checkedChangeListener);
        }
    }

    /**
     * Get the Scroll State.
     * @return
     *              One of {@link #SCROLL_STATE_IDLE}, {@link #SCROLL_STATE_TOUCH_SCROLL},
     *              or {@link #SCROLL_STATE_FLING}.
     */
    public int getScrollState() {
        return scrollState;
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                raiseEvent(new ScrollStateChangeEventArg(ListViewAdapter.this, scrollState));
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                raiseEvent(new ScrollEventArg(ListViewAdapter.this, firstVisibleItem,
                        visibleItemCount, totalItemCount));
            }
        });
    }
}
