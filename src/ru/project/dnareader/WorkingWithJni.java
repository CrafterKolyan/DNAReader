package ru.project.dnareader;

public class WorkingWithJni {
	static{
    	System.loadLibrary("hello-jni");
	}
    public native static String stringFromJNI();
}
