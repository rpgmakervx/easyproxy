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
import org.easyproxy.constants.Const;

import java.util.Set;


/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:28
 */

public class BaseServerChildHandler extends ChannelInitializer<SocketChannel> {

    public static final String DECODER = "decoder";
    public static final String ENCODER = "encoder";
    public static final String COMPRESS = "compress";
    public static final String DECOMPRESS = "decompress";
    public static final String AGGREGATOR = "aggregator";

    public static final String LOGHANDLER = "accessLogHandler";
    public static final String FILTERHANDLER = "ipFilterHandler";
    public static final String ANTILEECHhHANDLER = "antiLeechHandler";
    public static final String PERSONALPAGEHANLDER = "personalPageHandler";
    public static final String GETHANDLER = "getHandler";
    public static final String POSTHANDLER = "postHandler";
    public static final String PUTDELETEHANDLER = "putdeleteHandler";


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        boolean isProxy = Boolean.valueOf(ConfigFactory.getConfig().getString(Const.ISPROXY));

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(DECODER, new HttpRequestDecoder());
        pipeline.addLast(ENCODER, new HttpResponseEncoder());
        pipeline.addLast(COMPRESS, new HttpContentCompressor(9));
        pipeline.addLast(DECOMPRESS, new HttpContentDecompressor());
        pipeline.addLast(AGGREGATOR, new HttpObjectAggregator(1024000));
        pipeline.addLast(FILTERHANDLER,new FireWallHandler());
//        pipeline.addLast(LOGHANDLER,new AccessLogHandler());
        pipeline.addLast(ANTILEECHhHANDLER,new AntiLeechHandler());
        pipeline.addLast(PERSONALPAGEHANLDER,new PersonalPageHandler());
        if (isProxy) {
            pipeline.addLast(GETHANDLER,new GetRequestHandler());
            pipeline.addLast(POSTHANDLER,new PostRequestHandler());
            pipeline.addLast(PUTDELETEHANDLER,new PutAndDeleteRequestHandler());
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
