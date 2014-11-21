LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := hello-jni

LOCAL_SRC_FILES := hello-jni.cpp

APP_CPPFLAGS += -frtti
APP_CPPFLAGS += -fexceptions

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog

include $(BUILD_SHARED_LIBRARY)
