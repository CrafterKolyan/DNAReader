#include <jni.h>
#include <NativeLibrary.h>
#include <string>
#include <iostream>
#include "AndroidLog.hpp"
#include <embossLIB/emboss.h>
#include <embossLIB/ajfile.h>

JNIEXPORT jstring JNICALL Java_ru_project_dnareader_WorkingWithJni_stringFromJNI
  (JNIEnv *env, jclass object){
	// ajFileNewinNameC("123.ab1");
	return env->NewStringUTF("fine!");
}

}

