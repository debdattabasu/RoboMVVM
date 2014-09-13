RoboMVVM - MVVM Framework For Android
=====================================

RoboMVVM is an open source library that facilitates the use of the [MVVM](http://en.wikipedia.org/wiki/Model_View_ViewModel) pattern in Android apps. Those familiar with the .NET world can appreciate the ability of the MVVM pattern to simplify building, testing and refactoring UI heavy applications. The MVVM pattern heavily utilizes [Data Binding](http://en.wikipedia.org/wiki/Data_binding), the ability to make property changes in one component reflect in its observers. This requires a mechanism to notify observers of property changes in a component. The MVVM pattern also allows Action Binding, which is the binding of events in a component to actions in its observers. 


RoboMVVM will save countless hours of your time by providing you with the tools you need to quickly setup bindings between your views and your data models. At the core of the RoboMVVM library is the Component class, which is a container for events, properties, and actions. The Binding class is used to bind properties to properties and events to actions between a source component and a target component. Bindings can be one way or two way as specified by a BindMode, and a ValueConverter is used to convert between source and target properties 

Because Android does not have a standard property change and event notification system, an MVVM library must wrap Android classes in its own adapters to allow for Data and Action binding. RoboMVVM provides the ComponentAdapter base class for this purpose. It also provides many ComponentAdapter subclasses that act as adapters for most commonly used Android classes.

Please refer to the [Javadoc](http://debdattabasu.github.io/RoboMVVM/javadoc/) for more information.


TextSync - An Enlightening Use Case
===================================

Android code is notoriously ugly to write and maintain. For example, consider the code we need to write to keep the text property of two [EditText](http://developer.android.com/reference/android/widget/EditText.html)s in sync. You can also find this sample in the repository[here]().

```java
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
```

That's an awful lot of code for such a simple task. You will notice that the actual business logic is only a handful of lines. The rest of the code is just boilerplate to conform to how android works. This is repetitive code that must be written a countess number times in every android project, and in many cases, this obfuscates the real intent of the code. 

Now consider the same example with data binding in RoboMVVM. You can also find this sample in the repository[here]().

```java
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
        setContentView(new MainActivityViewModel(this).getView());
    }
}
```

It is immediately evident that the code is a lot shorter. What is more important, however, is that the code is now almost entirely business logic. The goal of RoboMVVM is to make your code about your actual data models and business logic, and less about managing Android's idiosyncrasies.


Property Binding Mechanism
===========================

When a one-way binding is set up from a source property to a target property, an EventListener is added to the source component that listens for changes to the specified property. When that property changes, The new value is retreived from the source object using the source component's getProperty function. The value is then passed through the ValueConverter, if any, and the final value is assigned to the target property using the target component's setProperty function. 

The getProperty function calls a getter function with a corresponding name and return type. The setProperty function chooses a setter function in the target component based on the property name and the type of the supplied argument. For example, if the name of the property is foo, and the supplied argument is of type int, then the following function calls are attempted:

```java
void setFoo(int arg);  
int getFoo(); 
```

The same mechanism is replicated in the other direction for two-way bindings. 


Action Binding Mechanism
========================

When a binding is set up between an event in the source component to an action in the target component, an EventListener is added to the source component that listens for events of the specified type. When such an event is raised, an action is called using the target component's invokeAction function. This function calls all functions in the component whose names match the supplied action name, have a void return type, and have either a single argument of a type compatible with the supplied event arg, or no arguments. 

For example, when an event of type ClickEventArg, which is derived from EventArg, is assigned to an action named doSomething, all of the following functions are called: 

```java
void doSomething(ClickEventArg arg); 

void doSomething(EventArg arg); 

void doSomething(); 
```


Binding ViewModel Lists to Adapter Views
=========================================

RoboMVVM lets you create lists of arbitrary View Models and bind them to [AdapterViews](http://developer.android.com/reference/android/widget/AdapterView.html). This means that you can add items of vastly different look and feel to the same AdapterView. This can be used to easily create rich dynamic item lists. 


Item List Sample
================

This app lets you add, remove and modify string items in a ListView. It also has an options menu where you can view a description of this app. It demonstrates the binding of View Model Collections, as well as handling of menus. 


License
=======

This library uses the [3-Clause BSD License](http://opensource.org/licenses/BSD-3-Clause).

	Project RoboMVVM(https://github.com/debdattabasu/RoboMVVM)
	Copyright (c) 2014, Debdatta Basu
	All rights reserved.

	Redistribution and use in source and binary forms, with or without modification, are permitted 
	provided that the following conditions are met:

		1. Redistributions of source code must retain the above copyright notice, this list of 
		   conditions and the following disclaimer.

		2. Redistributions in binary form must reproduce the above copyright notice, this list of 
		   conditions and the following disclaimer in the documentation and/or other materials 
		   provided with the distribution.

		3. Neither the name of the copyright holder nor the names of its contributors may be used 
		   to endorse or promote products derived from this software without specific prior 
		   written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
	EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
	OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 

	IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
	INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
	PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
	INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
	LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
	OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.