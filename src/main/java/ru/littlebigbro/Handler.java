package ru.littlebigbro;

import java.io.File;
import java.util.List;

public interface Handler {
    void startAlgorithm(File file, File newFile, String searchedString);
    List<Integer> searchByPattern(String pattern);
    String getErrorMessage();
    boolean getDone();
    String getNewFilePath();
}
