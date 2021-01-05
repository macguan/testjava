1.测试执行是importexample.sh

2.导入模板是import/template/import_cfg_001.properties

3.导入文件是import/stu2.txt

4.导入的库是188 mysql的dwrc库,里面的一个叫stu1表(一个三字段表)

5.java类在src/下

6.先vi java类,然后执行./gocompile.sh编译,然后执行./importexample.sh测试导入stu2.txt入stu1表

7.src/BootStrap.java是main方法类