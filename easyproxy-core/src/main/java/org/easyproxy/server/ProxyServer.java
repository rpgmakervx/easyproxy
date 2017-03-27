package org.easyproxy.server;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:26
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.handler.http.server.BaseServerChildHandler;
import org.easyproxy.api.app.handler.APIHandler;
import org.easyproxy.api.app.handler.IndexHandler;
import org.easyproxy.api.server.App;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:26
 */

public class ProxyServer {

    private App app;
    public void startup() {
        app = new App();
        launch(ConfigFactory.getConfig().getInt(ConfigEnum.LISTEN.key));
    }
    public void startup(int port) {
        launch(port);
    }

    private void launch(int port) {
        System.out.println("正在启动服务。。。,服务端口:" + port);
        EventLoopGroup bossGroup = new NioEventLoopGroup(32);
        EventLoopGroup workerGroup = new NioEventLoopGroup(32);
        ServerBootstrap b = new ServerBootstrap();
        ChannelFuture f = null;
        try {
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new BaseServerChildHandler())
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_REUSEADDR,true);
            f = b.bind(port).sync();
            System.out.println("服务已启动");
            initWebApp();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void initWebApp(){
        app.get("/api",new APIHandler())
                .get("/index",new IndexHandler())
                .start(7000);
    }
}
