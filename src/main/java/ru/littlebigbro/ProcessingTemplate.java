package ru.littlebigbro;

import java.io.File;

public class ProcessingTemplate {

    Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void executeHandler(File file, File newFile, String searchedString) {
        handler.startAlgorithm(file, newFile, searchedString);
    }
}
