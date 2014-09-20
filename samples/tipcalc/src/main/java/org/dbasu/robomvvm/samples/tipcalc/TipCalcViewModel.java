package org.dbasu.robomvvm.samples.tipcalc;

import android.content.Context;

import org.dbasu.robomvvm.annotation.SetLayout;
import org.dbasu.robomvvm.binding.BindMode;
import org.dbasu.robomvvm.binding.TypedValueConverter;
import org.dbasu.robomvvm.binding.ValueConverter;
import org.dbasu.robomvvm.viewmodel.ViewModel;

/**
 * @author Debdatta Basu
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
