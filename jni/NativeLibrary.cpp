#include <jni.h>
#include <string>
#include <iostream>
#include "AndroidLog.hpp"

extern "C" {

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
jstring
Java_ru_project_dnareader_WorkingWithJni_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
    return env->NewStringUTF("Hello from JNI !");
}

}

