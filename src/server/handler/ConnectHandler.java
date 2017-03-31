package server.handler;

import io.netty.channel.ChannelHandlerContext;
import server.WebSocketServer;
import server.logic.Snake;
import server.logic.SnakeDirection;
import static server.WebSocketServer.recipients;
import static server.WebSocketServer.ids;

public class ConnectHandler extends Thread{

    private ChannelHandlerContext ctx;

    public ConnectHandler(ChannelHandlerContext ctx) {
        this.ctx=ctx;
    }

    @Override
    public void run() {
        boolean check = false;
        int id = 0;
        while (!check){
            check = true;
            id = (int) (Math.random() * 100);
            for (int i:ids){
                if (id==i){
                    check = false;
                }
            }
        }
        ids.add(id);
        Snake snake = new Snake((int) (3 + Math.random() * (WebSocketServer.width - 6)),
                (int) (3 + Math.random() * (WebSocketServer.height - 6)), ctx.channel().id().toString(),id);
        snake.setDirection(SnakeDirection.values()[(int) (Math.random() * SnakeDirection.values().length)]);
        WebSocketServer.addSnakes(snake);
        recipients.add(ctx.channel());
        System.out.println("channel: " + snake.getIdChanel() + " id: " + snake.getId() +
                " x: " + snake.getX() + " y: " + snake.getY() + " " + snake.toString());
        new SendHandler(4,snake).start();
    }
}
