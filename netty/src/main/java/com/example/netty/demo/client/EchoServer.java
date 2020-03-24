package com.example.netty.demo.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by jiaxing.zhang on 2020/3/14.
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() + " <port>");
        }
        int port = args.length != 1 ? 8080 : Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();

        //1 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //2 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    //3 指定所使用的 NIO 传输 Channel
                    .channel(NioServerSocketChannel.class)
                    //4 使用指定的 端口设置套 接字地址
                    .localAddress(new InetSocketAddress(port))
                    //5  添加一个 EchoServer- Handler 到子 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            // EchoServerHandler 被 标注为@Shareable，所 以我们可以总是使用 同样的实例
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            //6 异步地绑定服务器; 调用 sync()方法阻塞 等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            //7 获取 Channel 的 CloseFuture，并 且阻塞当前线 程直到它完成
            f.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            //8  关闭 EventLoopGroup， 释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
