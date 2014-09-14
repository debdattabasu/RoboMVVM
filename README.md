RoboMVVM - MVVM Framework For Android
=====================================

RoboMVVM is an open source library that facilitates the use of the [MVVM](http://en.wikipedia.org/wiki/Model_View_ViewModel) pattern in Android apps. Those familiar with the .NET world can appreciate the ability of the MVVM pattern to simplify the building, testing and refactoring of UI applications. The MVVM pattern heavily utilizes [Data Binding](http://en.wikipedia.org/wiki/Data_binding), the ability to make property changes in one component reflect in its observers.

RoboMVVM will save countless hours of your time by providing you with the tools you need to quickly setup bindings between your views and your data models. 

At the core of RoboMVVM is the [Component](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentmodel/Component.java) class, which is a container for events, properties, and actions. The [Binding](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/binding/Binding.java) class is used to bind properties and actions between components. Property bindings can be one way or two way as specified by a [BindMode](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/binding/BindMode.java), and a [ValueConverter](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/binding/ValueConverter.java) is used to convert between source and target properties 

Because Android does not have a standard property change and event notification system, an MVVM library must wrap Android classes in its own adapters to allow for Data and Action binding. RoboMVVM provides the [ComponentAdapter](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentmodel/ComponentAdapter.java) class for this purpose. It also provides many ComponentAdapter subclasses that act as adapters for most commonly used Android classes.

Please refer to the [Javadoc](http://debdattabasu.github.io/RoboMVVM/javadoc/) for more information.


TextSync - An Enlightening Use Case
===================================

Android code is notoriously ugly to write and maintain. For example, consider the code we need to write to keep the text property of two [EditTexts](http://developer.android.com/reference/android/widget/EditText.html) in sync. You can find this sample in the repository [here](https://github.com/debdattabasu/RoboMVVM/tree/master/sample_textsync_no_mvvm).

```java
public class MainActivity extends Activity {

    EditText text0, text1;

    private final TextWatcher watcher0 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, 
        	int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, 
        	int before, int count) {

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
        public void beforeTextChanged(CharSequence s, int start, 
        	int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, 
        	int before, int count) {

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

Now consider the same example with data binding in RoboMVVM. You can find this sample in the repository [here](https://github.com/debdattabasu/RoboMVVM/tree/master/sample_textsync).

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
            bindProperty("text", R.id.edit_text_0, "text", 
            	BindMode.BIDIRECTIONAL);

            bindProperty("text", R.id.edit_text_1, "text", 
            	BindMode.BIDIRECTIONAL);
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

When a one-way binding is set up from a source property to a target property, an [EventListener](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentmodel/EventListener.java) is added to the source component that listens for changes to the specified property. When that property changes, the new value is retreived using the source component's getProperty method. The value is then passed through the ValueConverter, if any, and the final value is assigned to the target property using the target component's setProperty method. 

The getProperty method calls a getter function with a corresponding name. The setProperty function chooses a setter function based on the property name and the type of the supplied argument. For example, if the name of the property is foo, and the supplied argument is of type int, then the following function calls are attempted:

```java
/**
 * Function call attempted when trying to get a property named "foo". 
 * Only one getter is allowed per property. 
 */
int getFoo(); 

/**
 * Function call attempted when trying to set a property named "foo" 
 * to a value of type int. 
 */
void setFoo(int arg);  

/**
 * Multiple overloaded setters are allowed per property. This function
 * call is attempted when trying to set a property named "foo" 
 * to a value of type String.
 */
void setFoo(String arg);  
```

The same mechanism is replicated in the other direction for two-way bindings.

For example, to bi-directionally bind the "helloText" property of HelloViewModel to the text property of an edit text in its layout, you would do the following: 

```java
@SetLayout(R.layout.hello_layout)
public class HelloViewModel extends ViewModel {
	
	private String helloText = "Hello World!";

	//Getter for property helloText
    public String getHelloText() {
        return helloText;
    }

    //Setter for property helloText
    public void setHelloText(String helloText) {
        this.helloText = helloText;
        raisePropertyChangeEvent("helloText");
    }

    @Override
	protected void bind() {

		/**
		 * Bind the helloText Property of this view model to the text property 
		 * of an EditText with id R.id.edit_text. 
		 */
		bindProperty("helloText", R.id.edit_text, "text", BindMode.BIDIRECTIONAL);
	}
}
```

Binding Direction
=================

The Binding direction is specified by the [BindMode](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/binding/BindMode.java) enum, which can be either of the following: 

```java
/**
 * Changes in the source property are reflected in the target property but not vice-versa.
 * The source needs to have a property change notifier and a getter. The target needs to
 * have a setter.
 */
BindMode.SOURCE_TO_TARGET;

/**
 * Changes in the target property are reflected in the source property but not vice-versa.
 * The target needs to have a property change notifier and a getter. The source needs to
 * have a setter.
 */
BindMode.TARGET_TO_SOURCE;

/** 
 * The properties are kept completely in sync. The setter, getter, and change notifier 
 * need to be present for both the source and the target properties. 
 */
BindMode.BIDIRECTIONAL;
```


Value Conversion
================

Value conversion is carried out by the [ValueConverter](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/binding/ValueConverter.java) interface. Implement this interface to carry out custom conversion and validation. The interface provides one function for each direction of the binding. One-way bindings need only one of these functions, while two-way bindings need both.   

```java
public interface ValueConverter {
	
	/**
	 * This function needs to be implemented for source to target 
	 * and bidirectional bindings.
	 */ 
    public Object convertToTarget(Object value);

    /**
     * This function needs to be implemented for target to source 
     * and bidirectional bindings. 
     */
    public Object convertToSource(Object value);
}
``` 


Action Binding Mechanism
========================

When a binding is set up between an event in the source component to an action in the target component, an [EventListener](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentmodel/EventListener.java) is added to the source component that listens for events of the specified type. When such an event is raised, an action is called using the target component's invokeAction function. This function calls all functions in the component whose names match the supplied action name, have a void return type, and have either a single argument of a type compatible with the supplied event arg, or no arguments. 

So, when an event of type [ClickEventArg](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentadapter/view/ClickEventArg.java), which is derived from [EventArg](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentmodel/EventArg.java), is bound to an action named doSomething, all of the following action functions are called: 

```java
/**
 * Has a matching name and an argument of type ClickEventArg, which is
 * the same as the raised event. 
 */
void doSomething(ClickEventArg arg); 

/**
 * Has an argument of type EventArg, which is a base class of (and hence compatible with) 
 * the raised event of type ClickEventArg
 */
void doSomething(EventArg arg); 

/**
 * Has a matching name and no arguments.
 */
void doSomething(); 
```

For example, to bind the Click event of a Button to an action called "send", you would do the following: 

```java
@SetLayout(R.layout.send_layout)
public class SendViewModel extends ViewModel {
	
	public void send() {
		//handle sending logic here.
	}

	@Override
	protected void bind() {
		bindAction(R.id.button_send, ClickEventArg.class, "send");
	}
}
```


Binding ViewModel Lists to Adapter Views
=========================================

RoboMVVM lets you create lists of arbitrary View Models and bind them to [AdapterViews](http://developer.android.com/reference/android/widget/AdapterView.html). This means that you can add items of vastly different look and feel to the same AdapterView. This can be used to easily create rich dynamic item lists. For example, to bind a collection of HelloViewModels(defined above) to a [ListView](http://developer.android.com/reference/android/widget/ListView.html), you would do the following: 

```java
@SetLayout(R.layout.list_layout)
public class ListViewModel extends ViewModel {

	private ViewModelCollection<HelloViewModel> greetingCollection = 
		new ViewModelCollection<HelloViewModel>(); 

	public ViewModelCollection<HelloViewModel> getGreetingCollection() {
		return greetingColection;
	}

	@Override
	protected void bind() {
		bindProperty("greetingCollection", R.id.list_view, "source");
	}
}
```


Adapting Third Party Classes
============================

The [ComponentAdapter](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentmodel/ComponentAdapter.java) class is provided to adapt third party classes to RoboMVVM. This class is in use internally to adapt various android classes. 

For example, the adapter for [EditText](http://developer.android.com/reference/android/widget/EditText.html) is implemented in the library by the [EditTextViewAdapter](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentadapter/edittext/EditTextViewAdapter.java) class. This class subclasses [TextViewAdapter](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/componentadapter/textview/TextViewAdapter.java) which wraps a [TextView](http://developer.android.com/reference/android/widget/TextView.html).

The code looks like this:

```java
public class TextViewAdapter extends ViewAdapter {

    public void setText(String text) {
        TextView textView = (TextView) targetObject;
        textView.setText(text);
    }

    public void setTextColor(int color) {
        TextView textView = (TextView) targetObject;
        textView.setTextColor(color);
    }
}

public class EditTextViewAdapter extends TextViewAdapter {

    public String getText() {
        EditText editText = (EditText) targetObject;
        return editText.getText().toString();
    }

    @Override
    protected void adapt() {
        super.adapt();

        final EditText editText = (EditText) targetObject;


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, 
            	int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, 
            	int before, int count) {

                raiseEvent(new TextChangeEventArg(EditTextViewAdapter.this, 
                	s, start, before, count));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
```

As you can see, this adapter uses RoboMVVM style getter/setter methods and raises events. The targetObject is the object that is being wrapped. Wrappers for specific classes need to be registered with the ComponentAdapter.Associations class like this: 

```java
/**
 * Use the ViewAdapter class to adapt Views. 
 */
ComponentAdapter.Associations.set(View.class, ViewAdapter.class);

/**
 * Use the TextViewAdapter class to adapt TextViews. 
 */
ComponentAdapter.Associations.set(TextView.class, TextViewAdapter.class);

/**
 * Use the EditTextViewAdapter class to adapt EditTexts.
 */
ComponentAdapter.Associations.set(EditText.class, EditTextViewAdapter.class);
```

The associations need to be made at initialization time in order to make sure that the expected adapters are used in subsequent code.


Memory Management
=================

The [Binding](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/binding/Binding.java) class maintains weak references to both the source and the target Component. This allows the components to be garbage collected even when they are involved in a Binding. When either the source or target component is garbage collected, the binding is automatically unbound. 

When a view or a menu is created from a view model, the library strongly associates the view model with the created root view or menu item. This means that the view model is kept alive as long as any view or menu that uses it is alive. However, the existence of view models and their bindings does not prevent a view from being garbage collected. 


View Caching And Reuse
======================

The [ViewModel](https://github.com/debdattabasu/RoboMVVM/blob/master/library/src/main/java/org/dbasu/robomvvm/viewmodel/ViewModel.java) class allows for re-use of previously created views. A view that has been created by a certain ViewModel instance can be unbound from its ViewModel instance and bound to another ViewModel instance of the same class without needing to re-create the view. The convertView function attempts to do this, and returns the adapted view if it succeeds, or null if it fails. 

Use this feature when you have a large number of ViewModels of the same type, only some of which have Views that are visible to the user. When one ViewModel's view becomes invisible, its View can be adapted for use by another ViewModel.     


Item List Sample
=================

This app lets you add, remove and modify string items in a ListView. It also has an options menu where you can view a description of this app. It demonstrates the binding of View Model Collections, as well as handling of menus. You can find this sample in the repository [here](https://github.com/debdattabasu/RoboMVVM/tree/master/sample_itemlist). 


License
=======

This library uses the [3-Clause BSD License](http://opensource.org/licenses/BSD-3-Clause).

	Project RoboMVVM(https://github.com/debdattabasu/RoboMVVM)
	Copyright (c) 2014, Debdatta Basu
	All rights reserved.

	Redistribution and use in source and binary forms, with or without modification, 
	are permitted provided that the following conditions are met:

		1. Redistributions of source code must retain the above copyright notice, 
		   this list of conditions and the following disclaimer.

		2. Redistributions in binary form must reproduce the above copyright notice,
		   this list of conditions and the following disclaimer in the documentation
		   and/or other materials provided with the distribution.

		3. Neither the name of the copyright holder nor the names of its contributors
		   may be used  to endorse or promote products derived from this software 
		   without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 

	IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
	INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
	BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
	OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
	WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
	ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
	OF SUCH DAMAGE.