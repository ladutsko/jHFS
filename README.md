# Java HTTP File Server

## Build

```
./gradlew clean build
```

## Install

Binaries are available in `build/distributions` folder:

|||
|-|-:|
|Red Hat, Fedora, CentOS, etc.|jhfs-*.noarch.rpm|
|Debian, Ubuntu, etc.|jhfs_*_all.deb|
|Windows|jhfs-*-windows.zip|

## How to use

By default jHFS is configured to share files from the folder `/var/jhfs/welcome` on Linux
and `C:\jhfs\welcome` on Windows.

So, just copy your files into appropriate folder, start the service and check a result in browser:

```
http://<hostname>:8000/
```

You can add some comments to any files in shared folder - add text or html file with the same name
plus `.comment` extension like the following:

```
MyPublicFile.txt
MyPublicFile.txt.comment
```

Edit the configuration file `/etc/jhfs/application.yml` on Linux
or `<JHFS_HOME>\config\application.yml` on Windows
to change shared folder list, port number and code page for comment files.

Profit!
