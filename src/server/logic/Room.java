package server.logic;

import server.WebSocketServer;
import server.handler.SendHandler;
import java.util.ArrayList;
import static server.WebSocketServer.snakes;
import static server.WebSocketServer.matrix;

public class Room extends Thread{

    private int width;
    private int height;
    private Apple apple;
    private boolean isGameEnd;

    public Room(int width, int height) {
        this.width = width;
        this.height = height;
        this.isGameEnd = false;
    }

    public Apple getApple() {
        return apple;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void run() {

        System.out.println("Game Start!");

        WebSocketServer.game.createApple();

        while (!isGameEnd){
            for (Snake snake: snakes){
                snake.move();
            }
            isGameEnd = isGameEnd();
            matrix=gameField();
            sleep();
        }

        System.out.println("Game End!");
    }
    public boolean isGameEnd(){
        boolean result = true;
        Snake snakeDel=null;
        for (Snake snake: snakes){
            if (snake.isAlive()){
                result = false;
            }else {
                snakeDel = snake;
            }
        }
        if (snakeDel != null){
            new SendHandler(2,snakeDel).start();
            snakes.remove(snakeDel);
        }
        if (snakes.size()==1){
            for (Snake snake:snakes) {
                new SendHandler(3,snake).start();
            }
            result = true;
        }
        return result;
    }

    private int[][] gameField() {

        int[][] matrix = new int[height][width];

        for (Snake snake: snakes) {
            ArrayList<SnakeSection> sections = new ArrayList<SnakeSection>(snake.getSections());
            for (SnakeSection snakeSection : sections) {
                matrix[snakeSection.getY()][snakeSection.getX()] = snake.getId()*10 + 1;
            }
            matrix[snake.getY()][snake.getX()] = snake.getId()*10 + 2;
        }
        matrix[apple.getY()][apple.getX()] = 3;
        return matrix;
    }

    public void eatApple() {
        createApple();
    }

    private void createApple() {
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        apple = new Apple(x, y);
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
    }
}
