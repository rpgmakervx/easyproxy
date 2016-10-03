![logo](https://github.com/rpgmakervx/easyproxy/raw/master/resources/ep_logo.png)
>EasyProxy 是一个基于非阻塞IO的内存级反向代理、负载均衡中间件。中间件整体用Java实现，部署和配置非常简单，便于用户快速搭建一套负载均衡架构

## 中间件功能：
* 基于http协议的反向代理/负载均衡
* redis作为缓存介质
* 四种负载均衡策略:轮询，带权轮询，源地址哈希，最小链接选择
* 简单的http server
* ip黑名单，图片防盗链，请求数据gzip压缩

## 起步：

### 目录结构说明:

* bin:  存放easyproxy的启动和停机脚本以及easyproxy核心代码。
* conf: easyproxy相关配置文件。
* lib:  依赖的第三方库或其他模块。
* tmp:  存放临时文件
* resources： 存放静态资源
* logs: 存放日志
