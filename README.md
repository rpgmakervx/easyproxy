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

## 缓存：

目前版本只有`redis`作为缓存介质，redis相关配置不再介绍。

安装好后直接启动服务：`redis-server`.启动后使用`redis-cli`,输入命令`ping`,能够收到响应`pong`说明redis服务已经成功启动

## easyproxy启动：

首先请配置easyproxy的localhost，node等相关信息如下：
```xml
<proxy>
    <!--lb_strategy range:roundrobin,weight_roundrobin,ip_hash,less_connection-->
    <server proxy.server.listen="9524" proxy.server.localhost="127.0.0.1" proxy.server.lb_strategy="weight"/>
    <proxy_pass>
        <node proxy.server.nodes.ip="192.168.89.1" proxy.server.nodes.port="8080" proxy.server.nodes.weight="4"/>
        <node proxy.server.nodes.ip="192.168.89.1" proxy.server.nodes.port="8081" proxy.server.nodes.weight="6"/>
    </proxy_pass>
    <!-- cache_size=""-->
    <cache_strategy proxy.cache.ttl="20" proxy.cache.type="redis"/>
    <ehcache />
    <!--static_uri api_uri write your regex -->
    <resource proxy.resource.static_uri="/static/.*"
              proxy.resource.notfound_page="404page.html"
              proxy.resource.error_page="error.html"
              proxy.resource.forbidden_page="forbidden.html"
              proxy.resource.bad_request="badrequest.html"/>
    <log proxy.log.logopen="true" />
    <antileech proxy.antileech.open="false"/>
    <!--api is start? uri and param-->
    <api proxy.api.open="false"
         proxy.api.uri="/easyproxy.*"  />
    <ip_filter>
        <filter proxy.firewall.filter="192.168.117.1" />
        <filter proxy.firewall.filter="127.0.0.1" />
    </ip_filter>
</proxy>
```
配置信息详情见[用户指南](#用户指南)

进入`bin`目录，运行 `./startup` 启动服务

假设配置文件中`localhost`设置为**127.0.0.1**，打开浏览器输入[http://127.0.0.1:9524/static/index.html](http://127.0.0.1:9524/static/index.html),看到easyproxy的首页，说明服务启动成功。

此时确保你的两个tomcat都是启动的，浏览器输入：[http://127.0.0.1:9524](http://127.0.0.1:9524) 能看到tomcat的默认页面，说明反向代理效果成功。
多次请求这个地址会看到页面上的编号在发生变化，说明多次请求被路由到了不同的tomcat上，达到请求负载均衡的目的

# 用户指南

## 目录结构说明:

* bin:  存放easyproxy的启动和停机脚本以及easyproxy核心代码。
* conf: easyproxy相关配置文件。
* lib:  依赖的第三方库或其他模块。
* tmp:  存放临时文件
* resources： 存放静态资源
* logs: 存放日志

## 配置文件说明：
解压安装包后，进入conf目录，使用 `vim proxy.xml` 来编辑配置文件

`proxy.xml` 采用“类json”的方式进行配置，每个标签的attribute都不会重名，下面简单介绍xml的配置信息

**负载均衡器相关**：

* proxy.server.listen： easyproxy监听的端口。
* proxy.server.localhost: 设置主机ip地址或域名。
* proxy.server.lb_strategy: 负载均衡策略关键字（轮询：roundrobin,带权轮询：weight_roundrobin,源地址哈希：source_iphash,最小链接分配：least_connection）。

**真实节点相关**：

* proxy.server.nodes.ip: 真实节点ip地址
* proxy.server.nodes.port: 真实节点端口号
* proxy.server.nodes.weight: 真实节点的权重

**缓存相关**：

* proxy.cache.type： 缓存介质（目前只有redis）。
* proxy.cache.ttl:  缓存失效时间。

**静态资源相关**：

* proxy.resource.static_uri： 静态资源uri(标准正则表达式写法)。
* proxy.resource.notfound_page： 404页面文件名。
* proxy.resource.error_page： 50x页面文件名。
* proxy.resource.forbidden_page： 403页面文件名。
* proxy.resource.bad_request： 400页面文件名。

**日志相关**：

* proxy.log.logopen： 是否开启access.log记录。

**rest api相关**

* proxy.api.open： 是否开启easyproxy的rest api(api可以)。
* proxy.api.uri： rest api的正则表达式

**ip黑名单相关**

* proxy.firewall.filter：被拉黑的ip地址


## 2016.12.18 新增 properties 配置文件说明：

**负载均衡器相关**：
* proxy.server.listen: easyproxy监听的端口
* proxy.server.lb_strategy： 负载均衡策略关键字（轮询：roundrobin,带权轮询：weight_roundrobin,源地址哈希：source_iphash,最小链接分配：least_connection）。

**真实节点相关**：
* proxy.server.nodes.ip: 真实节点ip地址
* proxy.server.nodes.port: 真实节点端口号
* proxy.server.nodes.weight: 真实节点的权重

**缓存相关**：
* proxy.cache.open: 缓存是否开启
* proxy.cache.cache_ttl: 缓存失效时间
* proxy.cache.type: 缓存介质（目前只有redis）。

**静态资源相关**：
* proxy.resource.static_uri: 静态资源uri(标准正则表达式写法)。
* proxy.resource.notfound_page: 404页面
* proxy.resource.bad_request: 400页面
* proxy.resource.forbidden_page: 403页面
* proxy.resource.error_page: 50x页面

**日志相关**：
* proxy.log.logopen: 日志是否开启

**防盗链相关**：
* proxy.antileech.open=false

**应用防火墙相关**：
* proxy.firewall.open=false
* proxy.firewall.filter=192.168.117.1,192.168.0.110

## easyproxy控制台：

在`web`目录下执行脚本 `./startup` 便可运行easyproxy的控制台，端口 9000

打开浏览器访问页面： [http://localhost:9000/index](http://localhost:9000/index) 即可看到控制台界面。

操作指南

1.配置中心：修改负载均衡器的属性或相关配置信息,例如负载均衡策略，负载节点增减等

2.性能监控：监控负载均衡器所在节点或子节点健康状况等。（开发中）

3.日志分析：通过access log 分析每日请求量，来源地址和客户端等信息。（开发中）

## Thanks:
感谢 [大魔王](https://github.com/andilyliao) 提供技术思路和部分方案