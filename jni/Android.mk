LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := libacd
LOCAL_SRC_FILES := libacd.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libajax
LOCAL_SRC_FILES := libajax.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libajaxdb
LOCAL_SRC_FILES := libajaxdb.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libajaxg
LOCAL_SRC_FILES := libajaxg.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libeexpat
LOCAL_SRC_FILES := libeexpat.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libensembl
LOCAL_SRC_FILES := libensembl.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libepcre
LOCAL_SRC_FILES := libepcre.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libeplplot
LOCAL_SRC_FILES := libeplplot.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libezlib
LOCAL_SRC_FILES := libezlib.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := libnucleus
LOCAL_SRC_FILES := libnucleus.a

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := NativeLibrary
LOCAL_SRC_FILES := NativeLibrary.cpp

APP_CPPFLAGS += -frtti
APP_CPPFLAGS += -fexceptions

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_SRC_FILES += embossLIB
LOCAL_LDLIBS    := -llog
LOCAL_C_INCLUDES :=\
	include\
	include/epcre\
	include/eexpat\
	include/eplplot\
	include/ezlib\

LOCAL_STATIC_LIBRARIES := \
	libacd\
	libajax\
	libajaxdb\
	libajaxg\
	libeexpat\
	libensembl\
	libepcre\
	libeplplot\
	libezlib\
	libnucleus\

include $(BUILD_SHARED_LIBRARY)