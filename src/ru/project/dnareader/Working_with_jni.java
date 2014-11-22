package ru.project.dnareader;

public class Working_with_jni {
	static{
    	System.loadLibrary("hello-jni");
	}
    
    public native static String stringFromJNI();
}
