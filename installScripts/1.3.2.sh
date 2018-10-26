#!/bin/bash

install_dir="$1"
shift
if [[ "$install_dir" == "" ]] ;
then	echo -n "Specify a location for installation:"
	read install_dir
fi
if [[ ! -d "$install_dir" ]] ; then
	mkdir "$install_dir"
fi

cd "$install_dir"

curl -o install_fld.tar.xz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/install/1.3.2.tar.xz
tar -xJf install_fld.tar.xz
rm install_fld.tar.xz

echo -n "Create desktop shortcut?y/n:"
read desktop_short
if [[ "$desktop_short" = "y" || "$desktop_short" = "Y" ]] ;
then	printf "[Desktop Entry]\nExec=java -jar $install_dir/NatureSimulator.jar $install_dir\nEncoding=UTF-8\nType=Application\nName=Nature Simulator\nIcon=$install_dir/gameData/textures/ns_icon.png\nTerminal=false" > "$HOME"/.local/share/applications/NatureSimulator.desktop
fi

if [[ ! -d "$HOME/.ns-install" ]] ;
then	mkdir "$HOME/.ns-install"
fi

cd "$HOME/.ns-install"
printf "$install_dir" > target-dir
curl -o update.sh https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/install/update.sh
curl -o ns.sh https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/.data/ns-script.sh

cd "$HOME"
if [[ ! -d bin ]] ;
then mkdir bin
fi
cd bin
ln "$HOME/.ns-install/ns.sh" ns