#ifndef __ANDROID_LOG_H__
#define __ANDROID_LOG_H__

#include <android/log.h>

#define __LOG_TAG__ "TAGNAME"

#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, __LOG_TAG__, __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, __LOG_TAG__, __VA_ARGS__))
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, __LOG_TAG__, __VA_ARGS__))
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, __LOG_TAG__, __VA_ARGS__))
#define LOGV(...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, __LOG_TAG__, __VA_ARGS__))

#endif
