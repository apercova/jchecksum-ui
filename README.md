# jchecksum-ui
## Java Graphic User Interface (GUI) for [jchecksum-ui](https://github.com/apercova/jchecksum-ui).

> Uses QuickCLI to leverage CLI command configuration. Check it out [here](https://github.com/apercova/QuickCLI).  
> Uses jchecksum as CLI back service for GUI. Check it out [here](https://github.com/apercova/jchecksum).  
  
![main_view](https://raw.githubusercontent.com/apercova/imageio/master/jchecksum-ui/jchecksumui-01.png)

### Installation:
> As prerequisite you have to have [jchecksum](https://github.com/apercova/jchecksum) installed at ```~/.m2``` local maven repository. See instructions [here](https://github.com/apercova/jchecksum)

#### 1. Clone repository
```bash
$ git clone https://github.com/apercova/jchecksum-ui.git
Cloning into 'jchecksum-ui'...
remote: Enumerating objects: 105, done.
remote: Counting objects: 100% (105/105), done.
remote: Compressing objects: 100% (60/60), done.
remote: Total 105 (delta 21), reused 85 (delta 11), pack-reused 0
Receiving objects: 100% (105/105), 34.85 KiB | 2.18 MiB/s, done.
Resolving deltas: 100% (21/21), done.
```

#### 2. Package with maven 
> Installation at ```~/.m2``` local maven repository is not needed.
```bash
$ cd jchecksum-ui/
$ mvn clean package
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building jchecksumui 1.0.1904
[INFO] ------------------------------------------------------------------------
... [more log content] ...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 5.382 s
[INFO] Finished at: 2019-04-25T19:15:24-05:00
[INFO] Final Memory: 26M/230M
[INFO] ------------------------------------------------------------------------
```

#### 3. Identify different installation artifacts located inside ```dist``` directory:
- Javadoc jar: ```jchecksum-ui-1.0.1904-javadoc.jar```
- Sources jar: ```jchecksum-ui-1.0.1904-sources.jar```
- Executable jar: ```jchecksum-ui-1.0.1904.jar```
- Windows batch init script: ```checksumui.cmd```
- Linux bash init script: ```jchecksumui.sh```
```bash
$ ls dist/|grep -E "(jchecksum-ui|jchecksumui)"
jchecksum-ui-1.0.1904-javadoc.jar
jchecksum-ui-1.0.1904-sources.jar
jchecksum-ui-1.0.1904.jar
jchecksumui.cmd
jchecksumui.sh
```

- Library dir: ```/lib``` with dependencies
```bash
$ ls dist/lib/ |grep .
hamcrest-core-1.3.jar
jchecksum-1.0.1904.jar
junit-4.12.jar
quickcli-1.0.1904.jar
```

#### 4. Add ```dist``` dir to ```PATH``` enviroment variable and execute ```jchecksumui``` command. Look at the usage examples below.
 - On Windows:
```bash 
set PATH=C:\path\to\dist;%PATH%
jchecksumui
```
 - On Linux
```bash 
$ PATH=/path/to/dist:$PATH
$ jchecksumui
```
