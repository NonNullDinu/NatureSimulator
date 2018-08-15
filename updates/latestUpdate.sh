#!/bin/bash
#This will be ran from the NS install dir
install_dir="$(pwd)"
if [[ ! -f version ]] ;
then
	echo "version not found"
	installed_version=""
else
	installed_version="$(cat version)"
fi
wget -O temp-version https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/version
if [[ ${installed_version} == "$(cat temp-version)" ]] ;
then
	echo "Nothing to update, latest version found"
	exit 0
fi
echo "Updating..."
if [[ -d updateTmp ]] ; #Updated before
then #Clean up
echo "Older update found, cleaning"
for file in updateTmp/*; do
	if [[ -d ${file} ]] ; then
		rm -r ${file}
	else
		rm ${file}
	fi
done
else
mkdir updateTmp
fi
cd updateTmp
mkdir toJar
mkdir toGameData

cd toJar
wget -O toJar.tar.gz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/updates/toJar.tar.gz
tar -xvzf toJar.tar.gz
rm toJar.tar.gz
jar uf ${install_dir}/NatureSimulator.jar ./* ./*/* ./*/*/* ./*/*/*/* #Best attempt Ive come up with, but, i know,
																	  #its not good to do it like this
cd ..

cd toGameData
wget -O toGameData.tar.gz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/updates/toGameData.tar.gz
tar -xvzf toGameData.tar.gz
rm toGameData.tar.gz
mv -uf ./* ${install_dir}/gameData
cd ..

cd ..
rm -rf updateTmp
cat temp-version > version