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
import org.easyproxy.handler.http.server.BaseServerChildHandler;
import org.easyproxy.config.Config;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:26
 */

public class ProxyServer {


    public ProxyServer(String path) {
        if (Const.DEFAULT_CONFIGPATH.equals(path)){
            new Config(Config.class.getResourceAsStream(path));
        }else{
            new Config(path);
        }

    }

    public ProxyServer() {
        new Config(Config.class.getResourceAsStream(Const.DEFAULT_CONFIGPATH));
    }

    public void startup() {
        launch(Config.getInt(Const.LISTEN));
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
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
