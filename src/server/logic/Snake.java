package server.logic;

import server.WebSocketServer;

import java.util.ArrayList;
import static server.WebSocketServer.game;

public class Snake {

    private SnakeDirection direction;
    private boolean isAlive;
    private ArrayList<SnakeSection> sections;
    private boolean isReady=false;
    private String idChanel;
    private int id;

    public Snake(int x, int y, String idChanel, int id) {
        this.sections = new ArrayList<SnakeSection>();
        this.sections.add(new SnakeSection(x, y));
        this.idChanel = idChanel;
        this.isAlive = true;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getX() {
        return sections.get(0).getX();
    }

    public int getY() {
        return sections.get(0).getY();
    }

    public String getIdChanel() {
        return idChanel;
    }

    public void setDirection(SnakeDirection direction) {
        if (direction == SnakeDirection.UP && this.direction == SnakeDirection.DOWN){
            return;
        }
        if (direction == SnakeDirection.DOWN && this.direction == SnakeDirection.UP){
            return;
        }
        if (direction == SnakeDirection.RIGHT && this.direction == SnakeDirection.LEFT){
            return;
        }
        if (direction == SnakeDirection.LEFT && this.direction == SnakeDirection.RIGHT){
            return;
        }
        this.direction = direction;
    }

    public ArrayList<SnakeSection> getSections() {
        return sections;
    }

    public void move() {
        if (!isAlive) return;

        if (direction == SnakeDirection.UP)
            move(0, -1);
        else if (direction == SnakeDirection.RIGHT)
            move(1, 0);
        else if (direction == SnakeDirection.DOWN)
            move(0, 1);
        else if (direction == SnakeDirection.LEFT)
            move(-1, 0);
    }

    private void move(int dx, int dy) {

        SnakeSection head = sections.get(0);
        head = new SnakeSection(head.getX() + dx, head.getY() + dy);

        checkBorders(head);
        if (!isAlive) return;

        checkBody(head);
        if (!isAlive) return;

        Apple apple = game.getApple();
        if (head.getX() == apple.getX() && head.getY() == apple.getY())
        {
            sections.add(0, head);
            game.eatApple();
        } else
        {
            sections.add(0, head);
            sections.remove(sections.size() - 1);
        }
    }

    private void checkBorders(SnakeSection head) {
        if ((head.getX() < 0 || head.getX() >= game.getWidth()) ||
                head.getY() < 0 || head.getY() >= game.getHeight()) {
            isAlive = false;
        }
    }

    private void checkBody(SnakeSection head) {
        for (Snake snake: WebSocketServer.snakes){
            if (snake.sections.contains(head)) {
                isAlive = false;
            }
        }
    }
}
