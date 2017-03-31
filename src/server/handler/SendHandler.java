package server.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import server.WebSocketServer;
import server.logic.Snake;

public class SendHandler extends Thread{

    private int i;
    private Snake snake;

    public SendHandler(int i, Snake snake) {
        this.i = i;
        this.snake = snake;
    }

    @Override
    public void run() {
        if (i==2){
            String letter = "2-Поражение!";
            send(letter);
        }else if (i==3){
            String letter = "3-Победа!";
            send(letter);
        }else if (i==4){
            String letter = "4-"+snake.getId();
            send(letter);
        }
    }

    private void send(String letter){
        for (Channel channel: WebSocketServer.recipients){
            if (snake.getIdChanel().equals(channel.id().toString())){
                channel.writeAndFlush(new TextWebSocketFrame(letter));
            }
        }
    }
}
