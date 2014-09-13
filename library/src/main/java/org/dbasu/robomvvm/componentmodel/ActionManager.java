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

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Package private class used for invoking actions by {@link org.dbasu.robomvvm.componentmodel.Component}.
 */
class ActionManager {

    private static class ActionDescriptor {

        private final Collection<Method> methods;
        private final String name;
        private final Class objectType;


        private ActionDescriptor(Class objectType, String name, Collection<Method> methods) {
            this.methods = methods;
            this.name = name;
            this.objectType = objectType;
        }
    }

    private static final ThreadLocal<ActionManager> instance = new ThreadLocal<ActionManager>() {

        @Override protected synchronized ActionManager initialValue() {
            return new ActionManager();
        }
    };

    static ActionManager get() {

        return instance.get();
    }

    private ActionManager() {

    }

    private final Map<String, ActionDescriptor> actionMap = new HashMap<String, ActionDescriptor>();


    private void reallyInvokeAction(Collection<Method> handler, Object targetObject, Object eventArg) {
        try {
            for(Method m : handler) {
                if(m.getParameterTypes().length == 0) {
                    m.invoke(targetObject);
                } else {
                    m.invoke(targetObject, eventArg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void invokeAction(final Object targetObject, String actionName, final EventArg eventArg) {

        Class targetType = targetObject.getClass();
        final Class<? extends EventArg> eventType = eventArg!= null? eventArg.getClass() : null;

        final ActionDescriptor handlerDescription = getActionDescriptor(targetType, actionName);

        if(handlerDescription == null) {
            throw new RuntimeException("No Action By Name " + actionName +
                    " Found In Class " + targetType.getName());
        }

        final Collection<Method> relevantMethods = Collections2.filter(handlerDescription.methods, new Predicate<Method>() {
            @Override
            public boolean apply(Method m) {

                Class[] params = m.getParameterTypes();

                return params.length == 0 || (eventType != null && params.length == 1 && ClassUtils.isAssignable(eventType, params[0], true));

            }
        });

        if(relevantMethods.size() == 0) {

            throw new RuntimeException("Action By Name " + actionName + " Does Not Support Actions Of Type "
                    + ((eventType == null)? " {null} " : eventType.getName()));
        }

        if(targetObject instanceof View) {
            Activity activity = (Activity) ((View) targetObject).getContext();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reallyInvokeAction(relevantMethods, targetObject, eventArg);
                }
            });
        }
        else {
            reallyInvokeAction(relevantMethods, targetObject, eventArg);
        }
    }


    private ActionDescriptor reallyGetActionDescriptor(Class objectType, final String actionName) {

        Collection<Method> methodList = Collections2.filter(Arrays.asList(objectType.getMethods()), new Predicate<Method>() {
            @Override
            public boolean apply(Method m) {
                return m.getName().equals(actionName) && m.getReturnType().equals(Void.TYPE) && m.getParameterTypes().length <= 1;
            }
        });

        return new ActionDescriptor(objectType, actionName, methodList);
    }

    private ActionDescriptor getActionDescriptor(Class objectType, String actionName) {

        String hash = objectType.getName() + "." + actionName;

        ActionDescriptor ret = actionMap.get(hash);

        if(ret != null) return ret;

        ret = reallyGetActionDescriptor(objectType, actionName);

        if(ret == null) return null;

        actionMap.put(hash, ret);

        return ret;

    }
}
