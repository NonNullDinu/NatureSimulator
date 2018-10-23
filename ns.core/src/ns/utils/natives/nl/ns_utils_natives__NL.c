#include "ns_utils_natives__NL.h"
#include <jni.h>
#include <stdlib.h>
#include <stdio.h>

JNIEXPORT jint JNICALL Java_ns_utils_natives__1NL_add
  (JNIEnv *env, jclass jclass, jint a, jint b)
  {
    return a + b;
  }