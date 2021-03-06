package com.yl.distribute.scheduler.client;
import com.yl.distribute.scheduler.client.handler.NettyClientHander;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyChannelPoolHandler implements ChannelPoolHandler {
    public void channelReleased(Channel ch) throws Exception {
        System.out.println("channelReleased. Channel ID: " + ch.id());
    }


    public void channelAcquired(Channel ch) throws Exception {
        System.out.println("channelAcquired. Channel ID: " + ch.id());
    }

    public void channelCreated(Channel ch) throws Exception {
        System.out.println("channelCreated. Channel ID: " + ch.id());
        SocketChannel channel = (SocketChannel) ch;
        channel.config().setKeepAlive(true);
        channel.config().setTcpNoDelay(true);
        channel.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                .addLast(new LengthFieldPrepender(4))
                //使用netty自己的encoder和decoder,根据需要可以使用core中的kryo或protobuf
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))                
                .addLast(new NettyClientHander());

    }
}