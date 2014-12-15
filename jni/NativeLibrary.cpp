#include <jni.h>
#include <NativeLibrary.h>
#include <string>
#include "AndroidLog.hpp"
#include <embossLIB/emboss.h>
#include <embossLIB/ajfile.h>

JNIEXPORT jstring JNICALL Java_ru_project_dnareader_WorkingWithJni_stringFromJNI (JNIEnv *env, jclass object, jstring filename2){
    const char *filename= env->GetStringUTFChars(filename2,0);       // Имя файла
    AjPFile file = ajFileNewInNameC(filename);               // Переменная файла
    AjPStr filenameS = ajStrNewS(ajFileGetPrintnameS(file)); // Имя файла в формате библиотеки (обычные string'и не берёт)
    LOGI("here Everything is good");

    if(!ajFilenameExists(filenameS)){                        // Проверка существует ли файл
        LOGI("No file");
        return env->NewStringUTF("No file");
    }

    LOGI("FILE IS HERE!!!!");

    if(!ajSeqABITest(file)){                                // Проверка есть ли последовательность ДНК в файле
        LOGI("no sequence");
        return env->NewStringUTF("No sequence in file");
    }

    LOGI("LOL IT WORKS");
    AjPStr nseq = ajStrNew();
    ajSeqABIReadSeq(file,18,21,&nseq);
    LOGI("HERE IS MAGIC!");

    return env->NewStringUTF(ajCharNewS(nseq));

    return env->NewStringUTF("fine!");
}

}

