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

import android.app.Activity;
import android.view.View;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package private class used for setting and getting properties by {@link org.dbasu.robomvvm.componentmodel.Component}.
 */
class PropertyManager {

    private static class PropertyDescriptor {
        private final Method getter;
        private final Collection<Method> setters;
        private final String name;
        private final Class objectType;

        PropertyDescriptor(Class objectType, String name, Collection<Method> setters, Method getter) {
            this.name = name;
            this.objectType = objectType;
            this.setters= setters;
            this.getter = getter;
        }
    }

    private static final ThreadLocal<PropertyManager> instance = new ThreadLocal<PropertyManager>() {

        @Override protected synchronized PropertyManager initialValue() {
            return new PropertyManager();
        }
    };

    static PropertyManager get() {

        return instance.get();
    }

    private PropertyManager() { }

    private final Map<String, PropertyDescriptor> propertyMap = new HashMap<String, PropertyDescriptor>();

    private PropertyDescriptor getPropertyDescriptor(Class objectType, String name) {

        String hash = objectType.getName() + "." + name;

        PropertyDescriptor ret = propertyMap.get(hash);

        if(ret != null) return ret;

        ret = reallyGetPropertyDescriptor(objectType, name);

        if(ret == null) return null;

        propertyMap.put(hash, ret);

        return ret;
    }

    private PropertyDescriptor reallyGetPropertyDescriptor(Class<?> objectType, String name) {


        final String setName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);

        List<Method> allMethods = Arrays.asList(objectType.getMethods());

        Collection<Method> setMethodCollection = Collections2.filter(allMethods, new Predicate<Method>() {

            @Override
            public boolean apply(Method m) {
                Class[] params = m.getParameterTypes();
                return (m.getReturnType().equals(Void.TYPE) && m.getName().equals(setName) && params.length == 1);
            }
        });

        String getName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        Method getMethod = null;

        try {
            getMethod = objectType.getMethod(getName);
        } catch (NoSuchMethodException e) {
            getName = "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            try {
                getMethod = objectType.getMethod(getName);
            } catch (NoSuchMethodException e1) {

            }
        }

        if(getMethod == null && setMethodCollection.size() == 0) return null;
        return new PropertyDescriptor(objectType, name, setMethodCollection, getMethod);
    }

    private void reallySetProperty(Method setter, Object targetObject,  Object value) {

        try {
            setter.invoke(targetObject, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setProperty(final Object targetObject, String name, final Object value) {

        Class targetType = targetObject.getClass();

        final PropertyDescriptor desc = getPropertyDescriptor(targetType, name);
        if(desc == null || desc.setters.size() == 0) {
            throw new RuntimeException("No Settable Property By Name " + name + " Found In Class " + targetType.getName());
        }

        final Class valueClass = value.getClass();


        final Method setter = Iterables.find(desc.setters, new Predicate<Method>() {
            @Override
            public boolean apply(Method m) {
                return ClassUtils.isAssignable(valueClass, m.getParameterTypes()[0], true);
            }
        }, null);

        if(setter == null) {
            throw new RuntimeException("Type Mismatch: Can Not Assign Value Of Type " + valueClass.getName()
                    + " To Property " + name + " In Class " + targetType.getName());
        }

        if(targetObject instanceof View) {

            Activity activity = (Activity) ((View) targetObject).getContext();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reallySetProperty(setter, targetObject, value);
                }
            });
        }
        else {
            reallySetProperty(setter, targetObject, value);
        }
    }

    Object getProperty(Object targetObject, String name) {

        Class targetType = targetObject.getClass();

        PropertyManager.PropertyDescriptor desc = getPropertyDescriptor(targetType, name);
        if(desc == null || desc.getter == null) {
            throw new RuntimeException("No Gettable Property By Name " + name + " Found In Class " + targetType.getName());
        }

        Object ret = null;

        try {
            ret = desc.getter.invoke(targetObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

}
