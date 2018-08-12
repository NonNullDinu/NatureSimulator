#!/usr/bin/env bash
g++ -fPIC file.cpp -shared -o libProject.so -Wl,--whole-archive