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


package org.dbasu.robomvvm.componentadapter.seekbar;

import org.dbasu.robomvvm.componentmodel.PropertyChangeEventArg;

/**
 * Raised by a {@link SeekBarViewAdapter} to notify listeners
 * when the seek bar's progress changes.
 */
public class ProgressChangeEventArg extends PropertyChangeEventArg {

    private final int progress;
    private final boolean fromUser;

    /**
     * Construct a ProgressChangeEventArg.
     * @param sender
     *              The SeekBarViewAdapter that sends this event.
     * @param progress
     *              The new progress of the seek bar.
     * @param fromUser
     *              Whether the progress is changed by the user.
     */
    public ProgressChangeEventArg(SeekBarViewAdapter sender, int progress, boolean fromUser) {
        super(sender, "progress");
        this.progress = progress;
        this.fromUser = fromUser;
    }


    /**
     * Gets the  new progress of the seek bar.
     * @return
     *              The  new progress of the seek bar.
     */
    public int getProgress() {
        return progress;
    }


    /**
     * Gets whether the progress is changed by the user.
     * @return
     *              True if the progress is changed by the user. False otherwise.
     */
    public boolean isFromUser() {
        return fromUser;
    }
}
