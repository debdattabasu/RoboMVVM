package org.dbasu.robomvvm.samples.textsync_no_mvvm;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;



public class MainActivity extends Activity {

    EditText text0, text1;

    private final TextWatcher watcher0 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            text1.removeTextChangedListener(watcher1);
            text1.setText(text0.getText().toString());
            text1.addTextChangedListener(watcher1);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private final TextWatcher  watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            text0.removeTextChangedListener(watcher0);
            text0.setText(text1.getText().toString());
            text0.addTextChangedListener(watcher0);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text0 = (EditText) findViewById(R.id.edit_text_0);
        text1 = (EditText) findViewById(R.id.edit_text_1);

        text0.addTextChangedListener(watcher0);
        text1.addTextChangedListener(watcher1);
    }
}
