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

package org.dbasu.robomvvm.util;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Associates arbitrary tags with a supplied object. The tags are indexed using a {@link java.lang.String} key. Stores weak references to the supplied
 * objects to allow them to be garbage collected. Automatically removes associated tags when its object is garbage collected.
 */
public class ObjectTagger {

    private static class Tag {

        private HashMap<String, Object> tags = new HashMap<String, Object>();

        void addTag(String key, Object value) {
            tags.put(key, value);
        }

        Object removeTag(String key) {
            return tags.remove(key);
        }

        Object get(String key) {
            return tags.get(key);
        }

        boolean isEmpty() {
            return  tags.isEmpty();
        }
    }

    private static WeakHashMap<Object, Tag> tagMap = new WeakHashMap<Object, Tag>();

    /**
     * Set a tag associated with an object.
     * @param object
     *          The object to set the tag for.
     * @param key
     *          The {@link java.lang.String} index for the tag.
     * @param value
     *          The actual tag.
     */
    public static synchronized void setTag(Object object, String key, Object value) {

        Tag tag = tagMap.get(object);

        if(tag == null) {
            tag = new Tag();
            tagMap.put(object, tag);
        }

        tag.addTag(key, value);
    }

    /**
     * Removes a tag associated with an object.
     * @param object
     *      The object to remove the tag from.
     * @param key
     *      The {@link java.lang.String} index of the tag to remove.
     * @return
     *      The tag that has been removed. Null if no such tag exists.
     */
    public static synchronized Object removeTag(Object object, String key) {
        Tag tag = tagMap.get(object);

        if(tag == null) {
            return null;
        }

        Object ret =  tag.removeTag(key);

        if(tag.isEmpty()) {
            tagMap.remove(object);
        }

        return  ret;
    }

    /**
     * Gets a tag associated with an object.
     * @param object
     *      The object to get the tag of.
     * @param key
     *      The {@link java.lang.String} index of the tag to get.
     * @return
     *      The associated tag. Null if no such tag exists.
     */
    public static synchronized Object getTag(Object object, String key) {
        Tag tag = tagMap.get(object);

        if(tag == null) {
            return null;
        }

        return tag.get(key);
    }
}
