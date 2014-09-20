package org.dbasu.robomvvm.samples.textsync;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import org.dbasu.robomvvm.annotation.SetLayout;
import org.dbasu.robomvvm.binding.BindMode;
import org.dbasu.robomvvm.viewmodel.ViewModel;


public class MainActivity extends Activity {

    @SetLayout(R.layout.activity_main)
    public static class MainActivityViewModel extends ViewModel {

        public MainActivityViewModel(Context context) {
            super(context);
        }

        private String text = "Hello World!";

        public String getText() {
            return  text;
        }

        public void setText(String text) {
            this.text = text;
            raisePropertyChangeEvent("text");
        }

        @Override
        protected void bind() {
            bindProperty("text", R.id.edit_text_0, "text", BindMode.BIDIRECTIONAL);
            bindProperty("text", R.id.edit_text_1, "text", BindMode.BIDIRECTIONAL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainActivityViewModel(this).createView());
    }
}
