#include "ns_utils_natives__NL.h"
#include <jni.h>
// Just a test for now. There will be more functionality in the native side of the program, through its going to be
// harder to debug
JNIEXPORT jint JNICALL Java_ns_utils_natives__1NL_add
  (JNIEnv *env, jclass jclass, jint a, jint b)
  {
    return a + b;
  }