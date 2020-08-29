package ru.littlebigbro;

import java.io.IOException;
import java.util.Map;

public class ProcessingTemplate {

    Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void executeHandler(Map<String,String> params) throws IOException {
        handler.performingChecks(params);
    }
}
