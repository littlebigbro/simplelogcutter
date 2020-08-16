package ru.littlebigbro;

import java.util.List;

public interface Handler {
    void startAlgorithm();
    List<String> readFileAndRewriteInList();
    List<Integer> searchByPattern(String pattern);
    void writeToNewFile(List<String> oldLog, int begin, int end, String fileName);
    String getErrorMessage();
    boolean getDone();
}
