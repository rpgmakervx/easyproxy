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

    private static final String DECODER = "decoder";
    private static final String ENCODER = "encoder";
    private static final String COMPRESS = "compress";
    private static final String DECOMPRESS = "decompress";
    private static final String AGGREGATOR = "aggregator";

    private static final String LOGHANDLER = "accessLogHandler";
    private static final String FILTERHANDLER = "ipFilterHandler";
    private static final String ANTILEECHhHANDLER = "antiLeechHandler";
    private static final String PERSONALPAGEHANLDER = "personalPageHandler";
    private static final String GETHANDLER = "getHandler";
    private static final String POSTHANDLER = "postHandler";
    private static final String PUTHANDLER = "putHandler";
    private static final String DELETEHANDLER = "deleteHandler";


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        boolean isLogOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ConfigEnum.LOG_OPEN.key));
        
        boolean isProxy = Boolean.valueOf(ConfigFactory.getConfig().getString(Const.ISPROXY));
        boolean isAntileechOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ConfigEnum.ANTILEECH_OPEN.key));
        boolean hasIPFilter = ConfigFactory.getConfig().getBoolean(ConfigEnum.FIREWALL_OPEN.key);
        System.out.println("has ip filter:"+hasIPFilter);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(DECODER, new HttpRequestDecoder());
        pipeline.addLast(ENCODER, new HttpResponseEncoder());
        pipeline.addLast(COMPRESS, new HttpContentCompressor(9));
        pipeline.addLast(DECOMPRESS, new HttpContentDecompressor());
        pipeline.addLast(AGGREGATOR, new HttpObjectAggregator(1024000));
        if (hasIPFilter){
            pipeline.addLast(FILTERHANDLER,new IPFilterHandler(getForbiddenList()));
        }
        if (isLogOpen) {
            pipeline.addLast(LOGHANDLER,new AccessLogHandler());
        }
        if (isAntileechOpen) {
            pipeline.addLast(ANTILEECHhHANDLER,new AntiLeechHandler());
        }
        pipeline.addLast(PERSONALPAGEHANLDER,new PersonalPageHandler());
        if (isProxy) {
            pipeline.addLast(GETHANDLER,new GetRequestHandler());
            pipeline.addLast(POSTHANDLER,new PostRequestHandler());
            pipeline.addLast(PUTHANDLER,new PutRequestHandler());
            pipeline.addLast(DELETEHANDLER,new DeleteRequestHandler());
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
