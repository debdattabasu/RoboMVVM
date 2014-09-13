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


package org.dbasu.robomvvm.componentadapter.ratingbar;

import org.dbasu.robomvvm.componentmodel.PropertyChangeEventArg;

/**
 * Raised by a {@link RatingBarViewAdapter} to notify when the
 * rating bar's rating has changed.
 */
public class RatingChangeEventArg extends PropertyChangeEventArg {

    private final float rating;
    private final boolean fromUser;

    /**
     * Gets the new rating of the rating bar.
     * @return
     *              The new rating of the rating bar.
     */
    public float getRating() {
        return rating;
    }

    /**
     * Gets whether the user changed the rating.
     * @return
     *              True if the user changed the rating. False otherwise.
     */
    public boolean isFromUser() {
        return fromUser;
    }

    /**
     * Construct a RatingChangeEventArg.
     * @param sender
     *              The RatingBarViewAdapter that sent this event.
     * @param rating
     *              The new rating of the rating bar.
     * @param fromUser
     *              Whether the user changed the rating.
     */
    public RatingChangeEventArg(RatingBarViewAdapter sender, float rating, boolean fromUser) {
        super(sender, "rating");
        this.rating = rating;
        this.fromUser = fromUser;
    }
}
