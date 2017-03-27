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
import org.easyproxy.config.ConfigFactory;

import java.util.List;

import static org.easyproxy.constants.Const.ISPROXY;


/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:28
 */

public class BaseServerChildHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        boolean isLogOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(LOG_OPEN.key));
        boolean isApiOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(API_OPEN.key));
        boolean isProxy = Boolean.valueOf(ConfigFactory.getConfig().getString(ISPROXY));
        boolean isAntileechOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ANTILEECH_OPEN.key));
        boolean hasIPFilter = ConfigFactory.getConfig().getForbiddenHosts().size() != 0;
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
        if (isApiOpen) {
            pipeline.addLast(new APIHandler());
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
        List<String> forbidden_hosts = ConfigFactory.getConfig().getForbiddenHosts();
        IpSubnetFilterRule[] rules = new IpSubnetFilterRule[forbidden_hosts.size()];
        for (int index = 0; index < forbidden_hosts.size(); index++) {
            rules[index] = new IpSubnetFilterRule(forbidden_hosts.get(index), 32, IpFilterRuleType.REJECT);
        }
        return rules;
    }
}
