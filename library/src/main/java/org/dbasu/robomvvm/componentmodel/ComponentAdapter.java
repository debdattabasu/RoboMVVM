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

package org.dbasu.robomvvm.componentmodel;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.common.base.Preconditions;

import org.dbasu.robomvvm.componentadapter.adapterview.AdapterViewAdapter;
import org.dbasu.robomvvm.componentadapter.compoundbutton.CompoundButtonViewAdapter;
import org.dbasu.robomvvm.componentadapter.edittext.EditTextViewAdapter;
import org.dbasu.robomvvm.componentadapter.imageview.ImageViewAdapter;
import org.dbasu.robomvvm.componentadapter.listview.ListViewAdapter;
import org.dbasu.robomvvm.componentadapter.menuitem.MenuItemAdapter;
import org.dbasu.robomvvm.componentadapter.progressbar.ProgressBarViewAdapter;
import org.dbasu.robomvvm.componentadapter.ratingbar.RatingBarViewAdapter;
import org.dbasu.robomvvm.componentadapter.seekbar.SeekBarViewAdapter;
import org.dbasu.robomvvm.componentadapter.textview.TextViewAdapter;
import org.dbasu.robomvvm.componentadapter.view.ViewAdapter;
import org.dbasu.robomvvm.util.ObjectTagger;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An adapter to make components from arbitrary objects. Make associations between object classes and component adapters
 * classes using {@link ComponentAdapter.Associations}. Create or retrieve the component adapter
 * of an object of one of the associated types or their subtypes using {@link #get(Object)}.
 */
public class ComponentAdapter extends Component {

    private static final String COMPONENT_ADAPTER = "robomvvm_component_adapter";

    /**
     * Can make associations between arbitrary classes and the {@link ComponentAdapter} subclasses that are used to make
     * components out of them.
     */
    public static class Associations {

        static {
            adapterTypes = new HashMap<Class<?>, Class<? extends ComponentAdapter>>();
            set(MenuItem.class, MenuItemAdapter.class);
            set(View.class, ViewAdapter.class);
            set(ImageView.class, ImageViewAdapter.class);
            set(TextView.class, TextViewAdapter.class);
            set(EditText.class, EditTextViewAdapter.class);
            set(CompoundButton.class, CompoundButtonViewAdapter.class);
            set(ProgressBar.class, ProgressBarViewAdapter.class);
            set(SeekBar.class, SeekBarViewAdapter.class);
            set(RatingBar.class, RatingBarViewAdapter.class);
            set(AdapterView.class, AdapterViewAdapter.class);
            set(ListView.class, ListViewAdapter.class);
        }

        private static final Map<Class<?>, Class<? extends ComponentAdapter>> adapterTypes;

        /**
         * Get the associated component adapter class associated with a class. If no
         * component adapter class is associated with the supplied class, then its super classes and interfaces
         * are searched until a component adapter association is found.
         * @param objectType
         *          The class of the object to find associations for.
         * @return
         *          The associated component adapter class, or null if none is found.
         */
        public synchronized static Class<? extends ComponentAdapter> get(Class<?> objectType) {
            Preconditions.checkNotNull(objectType);

            Class<? extends ComponentAdapter> adapterType = null;

            Class<?> myObjectType = objectType;

            List<Class<?>> reachableObjectTypes = new ArrayList<Class<?>>();

            while (myObjectType!= null) {

                reachableObjectTypes.add(myObjectType);
                reachableObjectTypes.addAll(Arrays.asList(myObjectType.getInterfaces()));

                myObjectType = myObjectType.getSuperclass();
            }

            for(Class<?> klazz : reachableObjectTypes) {
                adapterType = adapterTypes.get(klazz);
                if(adapterType != null) return  adapterType;

            }

            return null;
        }


        /**
         * Associates a component adapter class with an arbitrary class.
         * @param objectType
         *          The class to make the association for. If an association already exists for this class, then it is overwritten.
         * @param adapterType
         *          The component adapter class to associate with the supplied class.
         *          If this is null, then any existing association of the supplied class is removed.
         */
        public synchronized static void set(Class<?> objectType, Class<? extends ComponentAdapter> adapterType) {
            Preconditions.checkNotNull(objectType);

            if(adapterType != null) {
                adapterTypes.put(objectType, adapterType);
            }
            else {
                adapterTypes.remove(objectType);
            }
        }
    }


    /**
     * Create or retrieve the component adapter for the supplied object. Once created, the component adapter is stored as a tag
     * on the supplied object using {@link org.dbasu.robomvvm.util.ObjectTagger}, which stores weak references to the supplied object to
     * allow for Garbage Collection. Any component adapter associated with an object that has been garbage collected is not stored
     * internally by the library.
     *
     * @param targetObject
     *          The object to get the component adapter for.
     * @return
     *          The component adapter for the supplied object.
     *
     * @throws java.lang.RuntimeException
     *          When no component adapter class is associated with
     *          the class of the supplied object.
     */
    public synchronized static ComponentAdapter get(Object targetObject) {
        Preconditions.checkNotNull(targetObject);

        ComponentAdapter componentAdapter = (ComponentAdapter) ObjectTagger.getTag(targetObject, COMPONENT_ADAPTER);
        if (componentAdapter != null) return componentAdapter;

        Class<? extends ComponentAdapter> adapterType = Associations.get(targetObject.getClass());

        if(adapterType == null) {

            throw new RuntimeException("No ComponentAdapter Subclass Associated With " + targetObject.getClass().getName() +
                    ". Call ComponentAdapter.Associations.set(Class<?> objectType, Class<? extends ComponentAdapter> adapterType) " +
                    "To Make The Association.");
        }

        try {
            componentAdapter = adapterType.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        componentAdapter.init(targetObject);

        ObjectTagger.setTag(targetObject, COMPONENT_ADAPTER, componentAdapter);

        return componentAdapter;
    }

    protected Object targetObject;

    protected ComponentAdapter() {

    }

    /**
     * Gets the target object of this component adapter.
     * @return
     *          Returns the target object.
     */
    public Object getTargetObject() {
        return targetObject;
    }

    private void init(Object targetObject) {
        this.targetObject = targetObject;
        adapt();

    }

    /**
     * Call this function in subclasses to set up the component adapter.
     */
    protected void adapt() {

    }
}
