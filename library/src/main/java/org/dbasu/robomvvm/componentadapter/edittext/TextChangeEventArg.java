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

package org.dbasu.robomvvm.componentadapter.edittext;

import org.dbasu.robomvvm.componentmodel.PropertyChangeEventArg;

/**
 * Raised by the {@link EditTextViewAdapter} when its
 * text property changes.
 */
public class TextChangeEventArg extends PropertyChangeEventArg {

    private final CharSequence charSequence;
    private final int startLocation;
    private final int oldCount;
    private final int newCount;


    /**
     * Get the char sequence that changed.
     * @return
     *              The char sequence that changed.
     */
    public CharSequence getCharSequence() {
        return charSequence;
    }

    /**
     * Get the start location of the change.
     * @return
     *              The start location of the change.
     */
    public int getStartLocation() {
        return startLocation;
    }


    /**
     * Get the new count of characters beginning from the start location.
     * @return
     *              The new count of characters beginning from the start location.
     */
    public int getNewCount() {
        return newCount;
    }


    /**
     * Get the old count of characters beginning from the start location.
     * @return
     *              The old count of characters beginning from the start location.
     */
    public int getOldCount() {
        return oldCount;
    }


    /**
     * Construct a TextChangeEventArg.
     * @param sender
     *              The edit text view adapter that raises this event.
     * @param charSequence
     *              The char sequence that changed.
     * @param startLocation
     *              The start location of the change.
     * @param oldCount
     *              The old count of characters beginning from the start location.
     * @param newCount
     *              The new count of characters beginning from the start location.
     */
    public TextChangeEventArg(EditTextViewAdapter sender, CharSequence charSequence, int startLocation, int oldCount, int newCount) {
        super(sender, "text");
        this.charSequence = charSequence;
        this.startLocation = startLocation;
        this.oldCount = oldCount;
        this.newCount = newCount;
    }
}
