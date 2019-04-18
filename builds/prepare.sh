#!/usr/bin/env bash
makeDirStructure() {
	cd "$NS_ROOT"
	mkdir -p builds/buildroot/lib/natives
	for fl in lib/natives/*.so; do
		cp $fl builds/buildroot/lib/"$(basename $fl)"
	done
	cp -r gameData builds/buildroot/
}

NS_ROOT="$(pwd)"
NS_BUILD_ROOT="$NS_ROOT/builds"
#clean up or prepare for new update
cd builds
if [[ -d buildroot ]]
then rm -rf buildroot/*
else mkdir buildroot
fi

VERSION="1.3.2"

cp "$NS_ROOT"/releases/alpha/NS"$VERSION".jar buildroot/NS.jar
printf "#!/bin/sh\nexec /usr/bin/java -jar \'/usr/share/java/ns/NS.jar\' -p /usr/share/ns/ -l /usr/share/ns/lib/ \"$@\"" > buildroot/ns.script
makeDirStructure
cd builds/buildroot
tar -cjvf NS-"$VERSION".prealpha.tar.xz ./* > /dev/null
cd "$NS_ROOT"
mv builds/buildroot/NS-"$VERSION".prealpha.tar.xz "$NS_ROOT"/
rm -rf builds/buildroot
unset NS_ROOT
