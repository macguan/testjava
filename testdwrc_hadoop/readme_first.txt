-------------------NO.1 CHAPTER how to start------------------------------
1.vi conf/jdbc.properties

2../run.sh
[machinemonitor@slave1 testdwrc]$ ./run.sh 
....NOW INITIAL...
root_dir is :/home/machinemonitor/localdata/ungit/testjava/testdwrc/
pagesize is :10
debuglevel is :0
....INITIAL COMPLETE...
------------------------------------------------
|--------   dwsoft dwrc cli            --------|
|--------   common version          -----------|
|--------   ver 1.0                 -----------|
|--------   jdk 1.6/1.7/1.8             -------|
------------------------------------------------
Usage: [cmd] param1 [value]
cmd: testconn/jdbcconfig/showtables/execsql/testinsert/exit/quit/help
Examples:
	xxx param1 value
.2 CHAPTER execsql and export sql---------------------------

	testconn
	jdbcconfig
	showtables
	execsql
	importfile
	exportsql
	help
	exit



3. input tessconn
dwsoft> testconn
connect success
dwsoft> 



---------------------NO.2 CHAPTER execsql and export sql---------------------------





---------------------NO.3 CHAPTER import---------------------------
1. vi import/template/xxx.xml
this is import cfg file

2. ./importexample.sh
this is a example commad

chinemonitor@slave1 testdwrc]$ cat importexample.sh 
#！/bin/sh
java -classpath ./bin:./lib/* cn.com.dwsoft.main.Bootstrap -import ./import/stu2.txt import_cfg_001.properties



----------------------NO.4 how to program -----------------------------------------------
1. vi src/xxx.java

2. ./gocompile.sh

3. ./run.sh       or          ./importexample.sh




