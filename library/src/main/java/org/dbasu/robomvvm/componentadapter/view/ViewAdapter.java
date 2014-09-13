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

package org.dbasu.robomvvm.componentadapter.view;

import android.graphics.drawable.Drawable;
import android.view.View;

import android.widget.AdapterView;

import org.dbasu.robomvvm.componentmodel.ComponentAdapter;


/**
 * A component adapter to adapt views.
 */
public class ViewAdapter extends ComponentAdapter {


    /**
     *  Set the background to a given resource, or remove the background.
     * @param resId
     *              The identifier of the resource to use, or 0 to remove the background.
     */
    public void setBackground(int resId) {
        View view = (View) targetObject;
        view.setBackgroundResource(resId);
    }


    /**
     * Set the background to a given Drawable, or remove the background.
     * @param background
     *              The Drawable to use as the background, or null to remove the background.
     */
    public void setbackground(Drawable background) {
        View view = (View) targetObject;
        view.setBackground(background);
    }

    /**
     * Sets the background color for this view.
     * @param color
     *              The color of the background.
     */
    public void setBackgroundColor(int color) {
        View view = (View) targetObject;
        view.setBackgroundColor(color);
    }

    /**
     * Set the enabled state of this view.
     * @param enabled
     *              True if this view is enabled. False otherwise.
     */
    public void setEnabled(boolean enabled) {
        View view = (View) targetObject;
        view.setEnabled(enabled);
    }

    /**
     * Set the visibility state of this view.
     * @param visibility
     *              One of View.VISIBLE, View.INVISIBLE, or View.GONE.
     */
    public void setVisible(int visibility) {
        View view = (View) targetObject;
        view.setVisibility(visibility);
    }

    @Override
    protected void adapt() {

        View view = (View) targetObject;
        if(view.isFocusable()) {

            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    raiseEvent(hasFocus ? new FocusEventArg(ViewAdapter.this) : new FocusLostEventArg(ViewAdapter.this));
                }
            });
        }

        if(view instanceof AdapterView) return;

        if(view.isClickable()) {


            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    raiseEvent(new LongClickEventArg(ViewAdapter.this));
                    return true;
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    raiseEvent(new ClickEventArg(ViewAdapter.this));
                }
            });

        }

    }
}
