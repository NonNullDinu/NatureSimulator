#!/bin/bash

install_dir="$0"
shift
if [[ install_dir == "" ]] ;
then read "Where should the game be installed?" install_dir
fi
if [[ ! -d ${install_dir} ]]; then
mkdir ${install_dir};
fi

cd ${install_dir}

wget -O install_fld.tar.xz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/install/1.3.2.tar.xz
tar -xvzf install_fld.tar.xz
rm install_fld.tar.xz

read "Create desktop shortcut?y/n" desktop_short
if [[ ${desktop_short} = "y" ]] ;
then printf "[Desktop Entry]\nExec=$install_dir/NatureSimulator.jar\nEncoding=UTF-8\nType=Application\nName=Nature Simulator\nIcon=$install_dir/gameData/textures/ns_icon.png\nTerminal=false" > ${HOME}/.local/share/applications/NatureSimulator.desktop
fi