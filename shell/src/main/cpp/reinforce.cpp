//
// Created by kb_jay on 2019-07-31.
//

#include "reinforce.h"
#include <fstream>

extern "C" {
JNIEXPORT void JNICALL Java_com_example_reinforce_shell_SdkShell_init
        (JNIEnv *env, jclass obj, jobject context, jobject classLoader) {
    jclass classContext = env->GetObjectClass(context);

    //1: gitDir
    jobject rootFile = env->CallObjectMethod(context, env->GetMethodID(classContext,
                                                                       "getDir",
                                                                       "(Ljava/lang/String;I)Ljava/io/File;"),
                                             env->NewStringUTF("init"), 0);
    jstring rootPath = static_cast<jstring>(env->CallObjectMethod(rootFile,
                                                                  env->GetMethodID(
                                                                          env->GetObjectClass(
                                                                                  rootFile),
                                                                          "getAbsolutePath",
                                                                          "()Ljava/lang/String;")));
    const char *dst = env->GetStringUTFChars(rootPath, NULL);
    strcat(const_cast<char *>(dst), "/real.dex");
    //2：copy
    jobject assetManager = env->CallObjectMethod(context,
                                                 env->GetMethodID(classContext, "getAssets",
                                                                  "()Landroid/content/res/AssetManager;"));
    AAssetManager *aam = AAssetManager_fromJava(env, assetManager);
    AAsset *aasset = AAssetManager_open(aam, "resource.txt", AASSET_MODE_UNKNOWN);
    //密码xireinforce 长度11
    off_t len = AAsset_getLength(aasset) - 11;
    char *buf = (char *) malloc(static_cast<size_t>(len));
    AAsset_read(aasset, buf, static_cast<size_t>(len));

    std::ofstream os(dst);
    os.write(buf, len);
    os.close();

    free(buf);
    AAsset_close(aasset);
    //3:load

    jclass classDexClassLoader = env->FindClass("dalvik/system/DexClassLoader");
    jmethodID methodDexLoaderInit = env->GetMethodID(classDexClassLoader, "<init>",
                                                     "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/ClassLoader;)V");
    jobject dexClassLoader = env->NewObject(classDexClassLoader,
                                            methodDexLoaderInit,
                                            env->NewStringUTF(dst),
                                            rootPath,
                                            NULL,
                                            classLoader);


    jclass classBaseDexloader = env->FindClass("dalvik/system/BaseDexClassLoader");
    jfieldID fieldIdPathList = env->GetFieldID(classBaseDexloader, "pathList",
                                               "Ldalvik/system/DexPathList;");
    jobject myPathListObject = env->GetObjectField(dexClassLoader,
                                                   fieldIdPathList);

    jclass classDexPathList = env->FindClass("dalvik/system/DexPathList");
    jfieldID fieldIdElements = env->GetFieldID(classDexPathList, "dexElements",
                                               "[Ldalvik/system/DexPathList$Element;");
    jobjectArray myElements = static_cast<jobjectArray >(env->GetObjectField(myPathListObject,
                                                                             fieldIdElements));

    jobject pathListObject = env->GetObjectField(classLoader,
                                                 fieldIdPathList);

    jobjectArray systemElements = static_cast<jobjectArray>(env->GetObjectField(pathListObject,
                                                                                fieldIdElements));


    jsize myLength = env->GetArrayLength(myElements);
    jsize systemLength = env->GetArrayLength(systemElements);

    jclass classElement = env->FindClass("dalvik/system/DexPathList$Element");
    jobjectArray newElementsArray = env->NewObjectArray(myLength + systemLength, classElement,
                                                        NULL);

    int i;
    for (i = 0; i < myLength; i++) {
        env->SetObjectArrayElement(newElementsArray, i, env->GetObjectArrayElement(myElements, i));
    }

    int j;
    for (j = 0; j < systemLength; j++, i++) {
        env->SetObjectArrayElement(newElementsArray, i,
                                   env->GetObjectArrayElement(systemElements, j));
    }

    env->SetObjectField(pathListObject, fieldIdElements, newElementsArray);
}
}
