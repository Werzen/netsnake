package server.handler;

import io.netty.channel.ChannelHandlerContext;
import server.logic.Snake;

import static server.WebSocketServer.recipients;
import static server.WebSocketServer.snakes;

public class DisconnectHandler extends Thread{

    private ChannelHandlerContext ctx;

    public DisconnectHandler(ChannelHandlerContext ctx) {
        this.ctx=ctx;
    }

    @Override
    public void run() {
        recipients.remove(ctx.channel());
        Snake snakeDel=null;
        for (Snake snake:snakes) {
            if (snake.getIdChanel().equals(ctx.channel().id().toString())){
                snakeDel = snake;
            }
        }
        if (snakeDel != null){
            snakes.remove(snakeDel);
        }

        for (Snake snake:snakes){
            System.out.println(snake.getIdChanel());
        }
    }
}
