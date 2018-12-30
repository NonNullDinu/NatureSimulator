Name:		NS
Version:	1.3.2.alpha
Release:        1%{?dist}
Summary:        The NatureSimulator rpm package

License:        GPLv3+
URL:            https://NonNullDinu.github.io/NatureSimulator
Source0:        https://www.github.com/NonNullDinu/NatureSimulator

Requires:       java-10-openjdk
BuildArch:      noarch

%description


%prep
#mkdir -p %{buildroot}/opt

%build
#cp "$HOME"/eclipse-workspace/NS/builds/buildroot/NS.tar %{buildroot}/opt/NS.tar

%install
mkdir -p %{buildroot}/opt
cp "$HOME"/eclipse-workspace/NS/builds/buildroot/NS.tar.bz2 %{buildroot}/opt/NS.tar.bz2

%post
cd /opt
mkdir NatSim
cd NatSim
tar -xjvf ../NS.tar.bz2 > /dev/null
cd ..
rm NS.tar.bz2
mv NatSim NS
cd NS
mkdir saveData

%files
/opt/NS.tar.bz2
%license LICENSE


%changelog
* Sat Dec  1 2018 NonNullDinu <dinu01blanovschi@gmail.com>
- Initial creation of RPM package
