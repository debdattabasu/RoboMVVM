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

package org.dbasu.robomvvm.componentadapter.menuitem;


import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import org.dbasu.robomvvm.componentmodel.ComponentAdapter;

/**
 * Component adapter to adapt a MenuItem.
 */
public class MenuItemAdapter extends ComponentAdapter {


    /**
     * Set the checked status of the menu item.
     * @param value
     *              The checked status of the menu item.
     */
    public void setChecked(boolean value) {
        MenuItem menuItem = (MenuItem) targetObject;
        menuItem.setChecked(value);
    }

    /**
     * Set the icon of the menu item, or remove the icon.
     * @param icon
     *              The Drawable to set as the icon of the menu item, or null to remove the icon.
     */
    public void setIcon(Drawable icon) {
        MenuItem menuItem = (MenuItem) targetObject;
        menuItem.setIcon(icon);
    }

    /**
     * Set the icon of the menu item, or remove the icon.
     * @param iconId
     *              The resource id of the Drawable to set as the icon of the menu item,
     *              or 0 to remove the icon.
     */
    public void setIcon(int iconId) {
        MenuItem menuItem = (MenuItem) targetObject;
        menuItem.setIcon(iconId);
    }

    /**
     * Set the enabled state of the menu item.
     * @param enabled
     *              The enabled state of the menu item.
     */
    public void setEnabled(boolean enabled) {
        MenuItem menuItem = (MenuItem) targetObject;
        menuItem.setEnabled(enabled);
    }

    /**
     * Set the expanded state of the menu item.
     * @param expanded
     *              The expanded state of the menu item.
     */
    public void setExpanded(boolean expanded) {
        MenuItem menuItem = (MenuItem) targetObject;
        if(expanded) {
            menuItem.expandActionView();
        } else {
            menuItem.collapseActionView();
        }
    }

    /**
     * Set the title of the menu item.
     * @param title
     *              The title of the menu item.
     */
    public void setTitle(String title) {
        MenuItem menuItem = (MenuItem) targetObject;
        menuItem.setTitle(title);
    }

    /**
     * Set the condensed title of the menu item.
     * @param condensedTitle
     *              The condensed title of the menu item.
     */
    public void setCondensedTitle(String condensedTitle) {
        MenuItem menuItem = (MenuItem) targetObject;
        menuItem.setTitleCondensed(condensedTitle);
    }

    /**
     * Set the visible state of the menu item.
     * @param visible
     *              The visible state of the menu item.
     */
    public void setVisible(boolean visible) {
        MenuItem menuItem = (MenuItem) targetObject;
        menuItem.setVisible(visible);
    }


    @Override
    protected void adapt() {

        MenuItem menuItem = (MenuItem) targetObject;

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                raiseEvent(new MenuItemClickEventArg(MenuItemAdapter.this));
                return true;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                raiseEvent(new MenuItemExpandedChangeEventArg(MenuItemAdapter.this, true));
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                raiseEvent(new MenuItemExpandedChangeEventArg(MenuItemAdapter.this, false));
                return true;
            }
        });
    }
}
