![logo](https://github.com/rpgmakervx/easyproxy/raw/master/resources/ep_logo.png)
>EasyProxy 是一个基于非阻塞IO的内存级反向代理、负载均衡中间件。中间件整体用Java实现，部署和配置非常简单，便于用户快速搭建一套负载均衡架构

# 中间件功能：
* 基于http协议的反向代理/负载均衡
* redis作为缓存介质
* 四种负载均衡策略:轮询，带权轮询，源地址哈希，最小链接选择
* 简单的http server
* ip黑名单，图片防盗链，请求数据gzip压缩
* 基于xml的静态配置，以及运行后通过rest api的动态配置

# 起步：

##准备工作：
首先请确保你的操作系统是linux的任意distribution，并且安装了redis2.4以上和hotspot jdk7以上

* [JDK7](http://download.oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-i586.tar.gz)
* [redis](http://redis.io/download)

## 真实节点：
你要有两个或以上web服务器，它能够提供基础的http服务，这里我们那常用的tomcat来举例。这里先给出tomcat下载链接，建议使用7.0版本

* [tomcat7](http://tomcat.apache.org/download-70.cgi)

解压缩两个tomcat分别叫**tomcat1**和**tomcat2**，如果你只有一个节点，那么启动两台tomcat需要将他们配置成不同的端口。
这里我们假设一个端口是8080，一个是8081.tomcat默认端口是8080因此这里只需要配置**tomcat2**即可。

进入**tomcat2**的`conf`目录，使用命令vim server.xml,编辑tomcat配置文件

```xml
<Connector port="8081" protocol="HTTP/1.1"
   connectionTimeout="20000"
   redirectPort="8443" />
```

Connector的属性 `port`就是tomcat端口号，改成8081即可。

为了能够看到负载均衡的效果，建议在每个tomcat的welcome page上标注当前的tomcat。

进入每个tomcat的`/webapps/ROOT/`，打开`index.jsp`页面,在页面顶部添加tomcat **x**, **x**代表tomcat的编号，也代表了真实节点

##缓存：

目前版本只有`redis`作为缓存介质，redis相关配置不再介绍。

安装好后直接启动服务：`redis-server`.启动后使用`redis-cli`,输入命令`ping`,能够收到响应`pong`说明redis服务已经成功启动

##easyproxy启动：

首先请配置easyproxy的localhost，node等相关信息，详见[用户指南](#user_guid)

进入`bin`目录，运行 `./startup` 启动服务

假设配置文件中`localhost`设置为**127.0.0.1**，打开浏览器输入(127.0.0.1:9524),看到tomcat的首页，说明服务启动成功。

# <span id="user_guid">用户指南</span>

## 目录结构说明:

* bin:  存放easyproxy的启动和停机脚本以及easyproxy核心代码。
* conf: easyproxy相关配置文件。
* lib:  依赖的第三方库或其他模块。
* tmp:  存放临时文件
* resources： 存放静态资源
* logs: 存放日志

## 配置文件说明：
解压安装包后，进入conf目录，使用 `vim proxy.xml` 来编辑配置文件
```xml
<proxy>

    <server listen="9524" localhost="192.168.89.1" lb_strategy="weight"/>
    <proxy_pass>
        <node host="192.168.89.1" port="8081" weight="4"/>
        <node host="192.168.89.1" port="8080" weight="6"/>
    </proxy_pass>
    <cache_strategy cache_ttl="10" cache_type="redis"/>
    <resource personal_uri="/static/.*" notfound_page="404page.html" error_page="error.html"
            forbidden_page="forbidden.html" bad_request="badrequest.html"/>
    <log logopen="true" />
    <api apiopen="true" api_uri="/easyproxy.*"  />
    <ip_filter>
        <filter filtered_ip="192.168.117.1" />
        <filter filtered_ip="127.0.0.1" />
    </ip_filter>
</proxy>
```
`proxy.xml` 采用“类json”的方式进行配置，每个标签的attribute都不会重名，下面简单介绍xml的配置信息

**负载均衡器相关**：

* listen： easyproxy监听的端口。
* localhost: 设置主机ip地址或域名。
* lb_strategy: 负载均衡策略关键字（轮询：roundrobin,带权轮询：weight_roundrobin,源地址哈希：source_iphash,最小链接分配：less_connection）。

**真实节点相关**：

* host: 真实节点ip地址
* port: 真实节点端口号
* weight: 真实节点的权重

**缓存相关**：

* cache_type： 缓存介质（目前只有redis）。
* cache_ttl:  缓存失效时间。

**静态资源相关**：

* personal_uri： 静态资源uri(标准正则表达式写法)。
* notfound_page： 404页面文件名。
* error_page： 50x页面文件名。
* forbidden_page： 403页面文件名。
* bad_request： 400页面文件名。

**日志相关**：

* logopen： 是否开启access.log记录。

**rest api相关**

* apiopen： 是否开启easyproxy的rest api(api可以)。
* api_uri： rest api的正则表达式

**ip黑名单相关**

* filtered_ip：被拉黑的ip地址
