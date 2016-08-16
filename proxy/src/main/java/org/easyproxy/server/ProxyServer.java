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
import org.easyproxy.constants.Const;
import org.easyproxy.handler.http.BaseServerChildHandler;
import org.easyproxy.util.Config;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:26
 */

public class ProxyServer {

    public ProxyServer(String path) {
        new Config(path);
    }

    public ProxyServer() {
        new Config("/proxy.xml");
    }

    public void startup(int port) {
        launch(port);
    }

    public void startup() {
        launch(Config.getInt(Const.LISTEN));
    }

    private void launch(int port) {
        System.out.println("正在启动服务。。。,服务端口:" + port);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        ChannelFuture f = null;
        try {
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new BaseServerChildHandler())
                    .option(ChannelOption.SO_BACKLOG, 256).childOption(ChannelOption.SO_KEEPALIVE, true);
            f = b.bind(port).sync();
            System.out.println("服务已启动");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
