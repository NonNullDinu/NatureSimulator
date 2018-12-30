# Maintainer: Dinu <dinu.blanovschi001@gmail.com>
pkgname=ns
pkgver=1.3.2
pkgrel=1
epoch=
pkgdesc="Nature Simulator"
arch=(x86_64)
#For now it's just a LAN url
url="http://192.168.1.3/ns"
license=('GPL')
groups=()
depends=('java-runtime>=11')
makedepends=()
checkdepends=()
optdepends=()
provides=()
conflicts=()
replaces=()
backup=()
options=()
install=NS.install
changelog=
source=("NS-1.3.2.prealpha.tar.xz")
noextract=()
sha512sums=("d2ca446a72391273579d104c650b5bf884c6cd756c5191bdd0096f0ee67b38eb5809f0dc91c79946cc6f49cff584468f6373908ab7a40419e12f410b31688a32")
validpgpkeys=("F165B36E6560241A8533CBC9D060643848A7470B")

package(){
	mkdir -p "$pkgdir"/usr/share/java/ns/ "$pkgdir"/usr/bin "$pkgdir"/usr/share/ns/saveData
	cp NS.jar "$pkgdir"/usr/share/java/ns/
	cp ns.script "$pkgdir"/usr/bin/ns
	chmod +x "$pkgdir"/usr/bin/ns
	cp gameData lib "$pkgdir"/usr/share/ns/ -r
	printf "0" > "$pkgdir"/usr/share/ns/saveData/saves.dir
}
