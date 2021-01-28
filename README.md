# PM


项目简介
---------


功能特性
---------

环境依赖
---------
JDK1.7+

部署步骤
---------
下载版本的jar包。启动命令
<br>
```java 
java -jar 编译后的JAR.jar [-Dfile.encoding=utf-8 乱码情况下使用]
     --spring.datasource.username=数据库用户 默认sa
     --spring.datasource.password=数据库密码 默认123
     --druid.loginUsername=Druid登陆用户名 默认slo
     --druid.loginPassword=Druid登陆密码 默认slo
```


声明
---------


协议
---------
GPLv3
# PM


项目简介
---------


功能特性
---------

环境依赖
---------
JDK1.8+

部署步骤
---------
下载版本的jar包。启动命令
<br>
```java 
java -jar 编译后的JAR.jar [-Dfile.encoding=utf-8 乱码情况下使用]
     --spring.datasource.username=数据库用户 默认sa
     --spring.datasource.password=数据库密码 默认123
     --druid.loginUsername=Druid登陆用户名 默认slo
     --druid.loginPassword=Druid登陆密码 默认slo
```
乱码问题 需要加入 logback.xml 具体可以百度配置。

声明
---------


协议
---------
GPLv3

其他
---------
自行添加 sqljdbc.jar 在上方 附件 下载即可。执行命令：
mvn install:install-file -Dfile=sqljdbc4-4.0.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0　


