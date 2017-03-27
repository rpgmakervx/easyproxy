package org.easyproxy.api.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.easyproxy.api.context.ActionHolder;
import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.server.handler.BaseChildHandler;

/**
 * Created by xingtianyu on 17-3-14
 * 下午4:18
 * description:
 */

final class Launcher {

    private HandlerContext context;
    private ActionHolder holder;

    public Launcher(HandlerContext context,ActionHolder holder){
        this.context = context;
        this.holder = holder;
    }
    public void start() {
        launch();
    }
    public void start(int port) {
        context.setRemotePort(port);
        launch();
    }
    private void launch() {
        System.out.println("正在启动服务。。。,服务端口:" + context.getRemotePort());
        EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 8);
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 8);
        ServerBootstrap b = new ServerBootstrap();
        ChannelFuture f = null;
        try {
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new BaseChildHandler(context,holder))
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .option(ChannelOption.TCP_NODELAY,true);
            f = b.bind(context.getRemotePort()).sync();
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
