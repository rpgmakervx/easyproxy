package org.easyproxy.handler.http.server;/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:28
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.constants.Const;

import java.util.Set;


/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:28
 */

public class BaseServerChildHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        boolean isLogOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ConfigEnum.LOG_OPEN.key));
        
        boolean isProxy = Boolean.valueOf(ConfigFactory.getConfig().getString(Const.ISPROXY));
        boolean isAntileechOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ConfigEnum.ANTILEECH_OPEN.key));
        boolean hasIPFilter = ConfigFactory.getConfig().getBoolean(ConfigEnum.FIREWALL_OPEN.key);
        System.out.println("has ip filter:"+hasIPFilter);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("compress", new HttpContentCompressor(9));
        pipeline.addLast("decompress", new HttpContentDecompressor());
        pipeline.addLast("aggregator", new HttpObjectAggregator(1024000));
        if (hasIPFilter){
            pipeline.addLast(new IPFilterHandler(getForbiddenList()));
        }
        if (isLogOpen) {
            pipeline.addLast(new AccessLogHandler());
        }
        if (isAntileechOpen) {
            pipeline.addLast(new AntiLeechHandler());
        }
        pipeline.addLast(new PersonalPageHandler());
        if (isProxy) {
            pipeline.addLast(new GetRequestHandler());
            pipeline.addLast(new PostRequestHandler());
            pipeline.addLast(new PutRequestHandler());
            pipeline.addLast(new DeleteRequestHandler());
        }
    }

    private IpSubnetFilterRule[] getForbiddenList() {
        Set<String> forbiddenHosts = ConfigFactory.getConfig().getForbiddenHosts();
        IpSubnetFilterRule[] rules = new IpSubnetFilterRule[forbiddenHosts.size()];
        int index = 0;
        for (String host:forbiddenHosts) {
            rules[index] = new IpSubnetFilterRule(host, 32, IpFilterRuleType.REJECT);
            index++;
        }
        return rules;
    }
}
