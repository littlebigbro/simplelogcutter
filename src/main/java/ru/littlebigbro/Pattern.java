package ru.littlebigbro;

public class Pattern {
    Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void executeHandler() {
        handler.startAlgorithm();
    }
}
