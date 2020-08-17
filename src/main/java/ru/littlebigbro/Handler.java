package ru.littlebigbro;

import java.util.List;

public interface Handler {
    void startAlgorithm();
    List<String> readFileAndRewriteInList();
    List<Integer> searchByPattern(String pattern);
    String getErrorMessage();
    boolean getDone();
}
