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

import com.google.common.base.Preconditions;

import org.apache.commons.lang3.ClassUtils;

/**
 * A value converter that attempts to convert between a source type and a target type.
 */
public class TypedValueConverter implements ValueConverter {

    protected final Class sourceType, targetType;

    /**
     * Constructs a typed value converter for converting values between types.
     * @param sourceType
     *          The type to convert the target value to in the function {@link #convertToSource(Object)}.
     * @param targetType
     *          The type to convert the source value to in the function {@link #convertToTarget(Object)}.
     */
    public TypedValueConverter(Class sourceType, Class targetType) {
        Preconditions.checkNotNull(sourceType);
        Preconditions.checkNotNull(targetType);

        this.sourceType = sourceType;
        this.targetType = targetType;
    }


    /**
     * Attempt to convert the value of the source property to the target type. If the type of the source property
     * is assignable to the target type, it performs a cast. If it is not, and the target type is {@link java.lang.String},
     * it calls the the source property's {@link Object#toString()} function.
     *
     * @param value
     *          The value of the source property.
     * @throws RuntimeException
     *           When the conversion fails.
     * @return
     *          The result of the conversion of the source property to the target type.
     */
    @Override
    public Object convertToTarget(Object value) {

        if(value == null) return null;

        Class valueType = value.getClass();


        if(ClassUtils.isAssignable(targetType, valueType, true)) {
            return targetType.cast(value);
        }

        if(ClassUtils.isAssignable(String.class, targetType, true)) {
            return targetType.cast(value.toString());

        }

        throw new RuntimeException("Unsupported Conversion From " + valueType.getName() + " To " + targetType.getName());

    }


    /**
     * Attempt to convert the value of the target property to the source type. If the type of the target property
     * is assignable to the source type, it performs a cast. If it is not, and the source type is {@link java.lang.String},
     * it calls the the target property's {@link Object#toString()} function.
     *
     * @param value
     *          The value of the target property.
     * @throws RuntimeException
     *          When the conversion fails.
     * @return
     *          The result of the conversion of the target property to the source type.
     */
    @Override
    public Object convertToSource(Object value) {

        if(value == null) return null;

        Class valueType = value.getClass();


        if(ClassUtils.isAssignable(sourceType, valueType, true)) {

            return sourceType.cast(value);
        }

        if(ClassUtils.isAssignable(String.class, sourceType, true)) {
            return sourceType.cast(value.toString());
        }

        throw new RuntimeException(this.getClass().getName() + " Does Not Support Conversion From " + valueType.getName() + " To " + sourceType.getName());

    }
}
