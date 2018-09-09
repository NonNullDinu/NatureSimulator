#!/bin/bash
install_dir="$(cat ~/.ns-install/target-dir)"
cd "$install_dir"
if [[ ! -f version ]] ;
then
	echo "version not found"
	installed_version=""
else
	installed_version="$(cat version)"
fi
wget -O temp-version https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/version
if [[ "$installed_version" = "$(cat temp-version)" ]] ;
then
	echo "Nothing to update, latest version found"
	exit 0
fi
echo "Updating..."
if [[ -d updateTmp ]] ; #Updated before
then #Clean up
echo "Older update found, cleaning"
for file in updateTmp/*; do
	if [[ -d "$file" ]] ; then
		rm -r "$file"
	else
		rm "$file"
	fi
done
else
mkdir updateTmp
fi
cd updateTmp
mkdir toJar
mkdir toGameData

cd toJar
wget -O toJar.tar.xz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/updates/jar.tar.xz
tar -xJf toJar.tar.xz
rm toJar.tar.xz
mv -uf jr.jar "$install_dir"/NatureSimulator.jar
cd ..

cd toGameData
wget -O toGameData.tar.xz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/updates/gameData.tar.xz
tar -xJf toGameData.tar.xz
rm toGameData.tar.xz
rm -rf "$install_dir"/gameData/*
for fl in ./* ; do
mv -uf "$fl" "$install_dir"/gameData
done
cd ..

cd ..
rm -rf updateTmp
cat temp-version > version
rm temp-version