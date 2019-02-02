#!/usr/bin/env bash
VERSION=1.0
NS_ROOT="$(pwd)"
if [[ -d ns.nus/build/buildroot ]];
then rm -rf ns.nus/build/buildroot/*
else mkdir -p ns.nus/build/buildroot/
fi
cp out/artifacts/ns_nus_jar/ns.nus.jar ns.nus/build/buildroot/NS_UPDATE_NSU_ENGINE.jar
cd ns.nus/build/buildroot
printf "#/usr/bin/env bash\njava -jar /usr/share/java/ns/NSU.jar \"\$@\"" > ns_update.script
tar -cjvf NSU-"$VERSION".prealpha.tar.xz ./* > /dev/null
cp NSU-"$VERSION".prealpha.tar.xz ../