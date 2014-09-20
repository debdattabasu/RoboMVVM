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
package org.dbasu.robomvvm.samples.itemlist;

import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.PopupWindow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Main activity for a basic use case of RoboMVVM(https://github.com/debdattabasu/RoboMVVM). This app lets you add,
 * remove and modify string items in a ListView. It also has an options menu where you can view a description of this app.
 */
public class MainActivity extends Activity {

    private static final String PREFERENCES_ID = "robomvvm_prefs";
    private static final String ITEMS_PREF = "items";

    private PopupWindow aboutPopup;
    private View rootView;
    private MainActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * Set MainActivityViewModel as the view model of this
         * activity.
         */
        viewModel = new MainActivityViewModel(this);
        rootView = viewModel.createView();
        setContentView(rootView);

        /**
         * Set AboutPopupViewModel as the view model for
         * the popup window.
         */

        AboutPopupViewModel aboutPopupViewModel = new AboutPopupViewModel(this);
        aboutPopup = new PopupWindow(aboutPopupViewModel.createView(), LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        /**
         * Restore state.
         */

        SharedPreferences prefs = getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE);

        Set<String> stringItems = prefs.getStringSet(ITEMS_PREF, null);

        if(stringItems != null) {
            for(String item : stringItems) {
                viewModel.getStrings().add(new StringViewModel(this, item));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /**
         * Set MainMenuViewModel as the view model
         * for the options menu.
         */
        MainMenuViewModel mainMenu = new MainMenuViewModel(this);
        mainMenu.inflate(menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        SharedPreferences.Editor prefs = getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE).edit();

        List<StringViewModel> items = viewModel.getStrings().getBackingStore();
        HashSet<String> stringItems = new HashSet<String>();

        for(StringViewModel item : items) {
            stringItems.add(item.getString());
        }

        prefs.putStringSet(ITEMS_PREF, stringItems);
        prefs.commit();
    }

    public void showAboutPopup() {
        aboutPopup.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    public void hideAboutPopup() {
        aboutPopup.dismiss();
    }
}
