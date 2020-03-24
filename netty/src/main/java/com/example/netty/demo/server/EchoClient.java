package com.example.netty.demo.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by jiaxing.zhang on 2020/3/14.
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(
                    "Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
        }

        String host = args.length != 2 ? "localhost" : args[0];
        int port = args.length != 2 ? 8080 : Integer.parseInt(args[1]);
        for (;;){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new EchoClient(host, port).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }


    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }


    }

}
