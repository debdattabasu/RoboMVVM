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

package org.dbasu.robomvvm.binding;

/**
 * Specifies the direction of a {@link org.dbasu.robomvvm.binding.Binding}.
 */
public enum BindMode {

    /**
     * Makes a one-way binding where the target property is set whenever the source property changes.
     */
    SOURCE_TO_TARGET(true, false),

    /**
     * Makes a one-way binding where the source property is set whenever the target property changes.
     */
    TARGET_TO_SOURCE(false, true),

    /**
     * Makes a two-way binding that keeps the source property and target property in complete sync.
     */
    BIDIRECTIONAL(true, true);


    /**
     * Returns whether this bind mode can bind the source property to the target property.
     * @return
     * Returns true for {@link #SOURCE_TO_TARGET} and {@link #BIDIRECTIONAL}.
     */
    public boolean canBindSourceToTarget() {
        return sourceToTarget;
    }

    /**
     * Returns whether this bind mode can bind the target property to the source property.
     * @return
     * Returns true for {@link #TARGET_TO_SOURCE} and {@link #BIDIRECTIONAL}.
     */
    public boolean canBindTargetToSource() {
        return targetToSource;
    }

    private boolean sourceToTarget, targetToSource;

    BindMode(boolean sourceToTarget, boolean targetToSource) {
        this.sourceToTarget = sourceToTarget;
        this.targetToSource = targetToSource;

    }
}
