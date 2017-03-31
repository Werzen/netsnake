package server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.example.http.websocketx.server.WebSocketIndexPageHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import server.handler.WebSocketServerHandler;
import server.logic.Room;
import server.logic.Snake;

import java.util.HashSet;
import java.util.Set;


public class WebSocketServer {
    private final int PORT;
    public final static int width = 30;
    public final static int height = 30;
    public static Room game;
    public volatile static Set<Integer> ids = new HashSet<>();
    public volatile static Set<Snake> snakes = new HashSet<>();
    public volatile static int[][] matrix = new int[width][height];
    public static ChannelGroup recipients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public WebSocketServer(int PORT) {
        this.PORT = PORT;
    }

    public static void main(String[] args) throws Exception {
        new WebSocketServer(11111).run();
    }
    public static void addSnakes(Snake snake) {
        snakes.add(snake);
    }
    public static String stringMatrix() {
        String result="1-";
        int[][] m = matrix;
        if (m!=null){
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[0].length; j++) {
                    if (j<m[0].length-1){
                        result+=m[i][j]+",";
                    } else result+=m[i][j]+";";
                }
            }
        }else result="null";
        return result;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception{
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new HttpObjectAggregator(65536));
                    pipeline.addLast(new WebSocketServerCompressionHandler());
                    pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));
                    pipeline.addLast(new WebSocketIndexPageHandler("/ws"));
                    pipeline.addLast(new WebSocketServerHandler());
                }
            });

            ChannelFuture f = b.bind(PORT).sync();
            f.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


}
