package server.handler;

import static server.WebSocketServer.game;
import static server.WebSocketServer.snakes;
import static server.WebSocketServer.recipients;
import io.netty.channel.ChannelHandlerContext;
import server.WebSocketServer;
import server.logic.Room;
import server.logic.Snake;
import server.logic.SnakeDirection;

public class ReceiveHandler extends Thread {

    private String msg;
    private ChannelHandlerContext ctx;

    public ReceiveHandler(ChannelHandlerContext ctx, String msg) {
        this.ctx = ctx;
        this.msg = msg;
    }

    @Override
    public void run() {
        if (msg.equals("ready")){

            System.out.println("ready " + ctx.channel().toString());

            boolean start = true;
            for (Snake snake:snakes){
                if (snake.getIdChanel().equals(ctx.channel().id().toString())){
                    snake.setReady(true);
                }
            }
            for (Snake snake:snakes){
                if (!snake.isReady()){
                    start = false;
                }
            }
            if (snakes.size()<2){
                start = false;
            }
            if (start){
                game = new Room(WebSocketServer.width,WebSocketServer.height);
                game.start();
                new DrawFrame(recipients).start();
            }
        }else if (msg.equals("up")){

            System.out.println("up " + ctx.channel().toString());
            direction(SnakeDirection.UP);

        }else if (msg.equals("down")){

            System.out.println("down " + ctx.channel().toString());
            direction(SnakeDirection.DOWN);

        }else if (msg.equals("left")){

            System.out.println("left " + ctx.channel().toString());
            direction(SnakeDirection.LEFT);

        }else if (msg.equals("right")){

            System.out.println("right " + ctx.channel().toString());
            direction(SnakeDirection.RIGHT);

        }
    }
    private void direction(SnakeDirection direction){
        for (Snake snake:snakes){
            if (snake.getIdChanel().equals(ctx.channel().id().toString())){
                snake.setDirection(direction);
            }
        }
    }
}
