#!/usr/bin/env bash
if g++ -c -Wall -Werror -fPIC ns_utils_natives__NL.c -I /usr/java/jdk-10.0.2/include -I /usr/java/jdk-10.0.2/include/linux
then    if ! g++ -shared -o libnl.so ns_utils_natives__NL.o
			then echo "Error while linking the native library" >&2
		fi
else    echo "Error while compiling the C native code" >&2
fi