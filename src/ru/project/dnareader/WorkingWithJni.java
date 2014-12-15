package ru.project.dnareader;

public class WorkingWithJni {
	static {
		System.loadLibrary("NativeLibrary");
	}

	public native static String stringFromJNI(String filename);
}
