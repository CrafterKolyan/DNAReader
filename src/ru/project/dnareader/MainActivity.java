package ru.project.dnareader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	TextView tv = new TextView(this);
        tv.setText(stringFromJNI());
        setContentView(tv);
    }
    
    static{
    	System.loadLibrary("hello-jni");
	}
    
    public native static String stringFromJNI();
}