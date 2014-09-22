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

package org.dbasu.robomvvm.samples.tipcalc;

import android.content.Context;

import org.dbasu.robomvvm.annotation.SetLayout;
import org.dbasu.robomvvm.binding.BindMode;
import org.dbasu.robomvvm.binding.TypedValueConverter;
import org.dbasu.robomvvm.binding.ValueConverter;
import org.dbasu.robomvvm.viewmodel.ViewModel;

/**
 * TipCalc Sample. Calculates tip based on subtotal and generosity. Demonstrates the use of
 * two-way bindings with value conversion.
 */
@SetLayout(R.layout.tipcalc_layout)
public class TipCalcViewModel extends ViewModel {

    private float subTotal = 0.0f;
    private float generosity = 10.0f ;

    private float tip = 0.0f;

    public TipCalcViewModel(Context context) {
        super(context);
    }

    public  float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
        raisePropertyChangeEvent("subTotal");
        recalculate();
    }

    public float getGenerosity() {
        return generosity;
    }

    public void setGenerosity(float generosity) {
        this.generosity = generosity;
        raisePropertyChangeEvent("generosity");
        recalculate();

    }

    public float getTip() {
        return tip;
    }

    public void setTip(float tip) {
        this.tip = tip;
        raisePropertyChangeEvent("tip");
    }

    private void recalculate() {
        setTip(getSubTotal() * getGenerosity() / 100);
    }


    @Override
    protected void bind() {

        bindProperty("subTotal", R.id.subtotal_edit_text, "text", new ValueConverter() {

            @Override
            public Object convertToTarget(Object value) {
                return value.toString();
            }

            @Override
            public Object convertToSource(Object value) {
                try {
                    return Float.parseFloat((String) value);
                } catch(NumberFormatException e) {
                    return 0;
                }
            }
        }, BindMode.BIDIRECTIONAL);



        bindProperty("generosity", R.id.generosity_seek_bar, "progress", new ValueConverter() {
            @Override
            public Object convertToTarget(Object value) {
                float floatVal = (Float) value;

                int ret = (int)(floatVal * 100);

                return ret;
            }

            @Override
            public Object convertToSource(Object value) {
                int intVal = (Integer) value;

                float ret = ((float)intVal) / 100f;

                return ret;
            }
        }, BindMode.BIDIRECTIONAL);


        bindProperty("generosity", R.id.generosity_text_view, "text",
                new TypedValueConverter(Float.class, String.class), BindMode.SOURCE_TO_TARGET);



        bindProperty("tip", R.id.tip_text_view, "text",  new TypedValueConverter(Float.class, String.class), BindMode.SOURCE_TO_TARGET);

    }

}
