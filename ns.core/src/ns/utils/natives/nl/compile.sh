#!/usr/bin/env bash
if g++ -c -Wall -Werror -fpic ns_utils_natives__NL.cpp -I /usr/java/jdk-10.0.2/include -I /usr/java/jdk-10.0.2/include/linux
then g++ -shared -o libnl.so ns_utils_natives__NL.o
fi