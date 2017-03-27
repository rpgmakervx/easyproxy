package org.easyproxy.api.kits;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by xingtianyu on 17-3-9
 * 下午12:02
 * description:
 */

public class ByteKits {

    public static ByteBuf toByteBuf(byte[] bytes){
        if (bytes == null||bytes.length == 0){
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        return buf;
    }
    public static ByteBuf toByteBuf(String content){
        return toByteBuf(content.getBytes());
    }
    public static byte[] toByteArray(ByteBuf buf){
        if (buf == null||buf.readableBytes() == 0){
            return new byte[0];
        }
        int readable = buf.readableBytes();
        byte[] bytes = new byte[readable];
        buf.readBytes(bytes);
        return bytes;
    }

    public static String toString(ByteBuf buf){
        return new String(toByteArray(buf));
    }
}
