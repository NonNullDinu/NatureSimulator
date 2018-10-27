---
layout: default
---

![NS_ICON](https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/gameData/textures/ns_icon.png)

## Description
This application is an open-source attempt to simulate nature.
Still has a long way to go before the objective is reached

System requirements
Only requires JRE 10.0.2 or later to be installed to run

Developed on Fedora 28

## How to update?
### Linux and MacOS
Just run the following and the application will update:
```bash
sh ~/.ns-install/update.sh
```

### Windows
No auto-update, have to download the new version.

<table>
	<caption>OS Dependent Features</caption>
	<tr>
		<th>Feature</th>
		<th>Linux</th>
		<th>MacOS</th>
		<th>Windows</th>
	</tr>
	<tr>
		<td>BASH-based update</td>
		<td>✓</td>
		<td>✓</td>
		<td></td>
	</tr>
	<tr>
		<td>Dependency native libraries</td>
		<td>✓</td>
		<td>@FIXME</td>
		<td>✓</td>
	</tr>
</table>

Update log
* NS v.1.3.3.-alpha
	* BASH4.1-based updating engine v.1.0(can be seen [here](../install/update.sh))
		* Downloads update script
		* Runs update script
		* After it's done, deletes the update script
		* Update script contains the following instructions:
			* Compare current version to version in cloud. If it is the same exit
			* Download the patch data
			* Apply the patch
			* Delete the patch data
			* Update current version

![Octocat](https://assets-cdn.github.com/images/icons/emoji/octocat.png)
