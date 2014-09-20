package org.dbasu.robomvvm.samples.tipcalc;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TipCalcViewModel(this).createView());
    }
}
