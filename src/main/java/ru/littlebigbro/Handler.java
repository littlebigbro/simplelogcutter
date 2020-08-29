package ru.littlebigbro;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Handler {
    void performingChecks(Map<String,String> params) throws IOException;
    void startAlgorithm() throws IOException;
    List<Integer> searchByPattern(String pattern);
    String getErrorMessage();
    boolean getDone();
    String getNewFilePath();
}
