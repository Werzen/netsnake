package server.handler;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import server.WebSocketServer;

public class DrawFrame extends Thread{

    ChannelGroup recipients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public DrawFrame(ChannelGroup recipients) {
        this.recipients = recipients;
    }

    @Override
    public void run() {

        while (!WebSocketServer.game.isGameEnd()){
            String rq = WebSocketServer.stringMatrix();

            recipients.writeAndFlush(new TextWebSocketFrame(rq));

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {}
        }

    }
}
