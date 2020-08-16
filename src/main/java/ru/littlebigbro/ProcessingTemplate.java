package ru.littlebigbro;

public class ProcessingTemplate {

    Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void executeHandler() {
        handler.startAlgorithm();
    }
}
