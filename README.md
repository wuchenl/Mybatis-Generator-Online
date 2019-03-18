# 关于Springboot2-demo-generator 
    这是一个关于web页面在线生成dao,mapper文件等的实验型项目.核心原理是利用mybatis-generator-core进行生成.
    
## 支持的数据库版本,以及对应驱动

| 支持的数据库 | 数据库版本         | 驱动名称             | 驱动版本 |
|:---|:---|:---|:---|
| Mysql        | 5.5, 5.6, 5.7, 8.0 | mysql-connector-java | 8.0.13   |
| Oracle | 常见版本 | ojdbc6 | 11.2.0.3 |
| SqlServer | 2005,2008 | mssql-jdbc | 7.2.1.jre8 |


## 使用说明

目前是一个初始版本，为了简历好看点，提前放出来。基本功能有了。内部结构和部分插件会重写。

### 使用方式
clone本项目，打开并启动。启动后访问[http://localhost:2002/generator](http://localhost:2002/generator)即可

### 更新日志
- 2019-3-18加入SqlServer支持，修正这个版本错误问题


