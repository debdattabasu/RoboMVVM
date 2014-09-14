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

import org.dbasu.robomvvm.componentmodel.PropertyChangeEventArg;

/**
 * Raised by a {@link org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter} to notify listeners when
 * its scroll state has changed.
 */
public class ScrollStateChangeEventArg extends PropertyChangeEventArg {



    private final int scrollState;

    /**
     * Construct a ScrollStateChangeEventArg
     * @param sender
     *              The ListViewAdapter that raises this event.
     * @param scrollState
     *              The scroll state. One of {@link org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter#SCROLL_STATE_IDLE},
     *              {@link org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter#SCROLL_STATE_TOUCH_SCROLL},
     *              or {@link org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter#SCROLL_STATE_FLING}.
     */
    public ScrollStateChangeEventArg(ListViewAdapter sender, int scrollState) {
        super(sender, "scrollState");
        this.scrollState = scrollState;
    }

    /**
     * Get the Scroll State.
     * @return
     *              One of {@link org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter#SCROLL_STATE_IDLE},
     *              {@link org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter#SCROLL_STATE_TOUCH_SCROLL},
     *              or {@link org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter#SCROLL_STATE_FLING}.
     */
    public int getScrollState() {
        return scrollState;
    }
}
