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
import org.easyarch.netpet.web.server.App;
import org.easyproxy.api.app.handler.config.*;
import org.easyproxy.api.app.handler.log.ClientUsageHandler;
import org.easyproxy.api.app.handler.log.DailyActiveHandler;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.handler.http.server.BaseServerChildHandler;

import java.io.IOException;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:26
 */

public class ProxyServer {

    private App app;
    private Config config = ConfigFactory.getConfig();
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
                    .option(ChannelOption.TCP_NODELAY,config.getBoolean(ConfigEnum.NODELAY.key))
                    .option(ChannelOption.SO_REUSEADDR,config.getBoolean(ConfigEnum.REUSEADDR.key))
                    .option(ChannelOption.SO_KEEPALIVE,config.getBoolean(ConfigEnum.KEEPALIVE.key))
                    .option(ChannelOption.SO_LINGER,config.getInt(ConfigEnum.SOLINGER.key))
                    .option(ChannelOption.SO_SNDBUF,config.getInt(ConfigEnum.SNDBUF.key))
                    .option(ChannelOption.SO_RCVBUF,config.getInt(ConfigEnum.RCVBUF.key));
            f = b.bind(port).sync();
            System.out.println("服务已启动");
            initWebApp();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void initWebApp() throws IOException {
        boolean isApiOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ConfigEnum.API_OPEN.key));
        System.out.println("is api open?:"+isApiOpen);
        if (isApiOpen){
            app.post("/config",new ConfigHandler())
                    .get("/hosts",new HostHandler())
                    .get("/lbstrategy",new LBStrategyHandler())
                    .get("/firewall",new FireWallHandler())
                    .get("/params",new ParamsHandler())
                    .get("/dailyActive",new DailyActiveHandler())
                    .get("/client/usage",new ClientUsageHandler())
                    .start(config.getInt(ConfigEnum.APIPORT.key));
        }
    }
}
