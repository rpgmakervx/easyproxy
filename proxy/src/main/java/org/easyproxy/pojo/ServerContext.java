package org.easyproxy.pojo;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * Description :
 * Created by xingtianyu on 16-12-9
 * 上午9:36
 */

public class ServerContext {

    private ChannelHandlerContext ctx;

    private Object msg;

    private InetSocketAddress allocatedAddress;

    public ServerContext(ChannelHandlerContext ctx, InetSocketAddress allocatedAddress, Object msg) {
        this.ctx = ctx;
        this.msg = msg;
        this.allocatedAddress = allocatedAddress;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public InetSocketAddress getAllocatedAddress() {
        return allocatedAddress;
    }

    public void setAllocatedAddress(InetSocketAddress allocatedAddress) {
        this.allocatedAddress = allocatedAddress;
    }
}
