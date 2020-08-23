package ru.littlebigbro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegExHandler implements Handler {
    private List<String> logInList;
    private String errorMessage = "FALSE";
    private boolean done = false;
    private File newFile;

    @Override
    public void startAlgorithm(File log, File newLog, String searchedPattern) {
        logInList = FileAction.readFileToList(log);
        if (logInList.isEmpty()) {
            errorMessage = "Обрабатываемый файл пуст";
            return;
        }
        //находим начала всех переходов в файле

        String STANDARD_PATTERN = "(.*)Лог перехода(.*)для документа(.*)";
        List<Integer> stdPatternHits = searchByPattern(STANDARD_PATTERN);
        if (stdPatternHits.isEmpty()) {
            errorMessage = "Выбранный файл не pay.log";
            return;
        }
        //находим строки соответствующие регулярному выражению
        List<Integer> searchHits = searchByPattern(searchedPattern);
        if (searchHits.isEmpty()) {
            errorMessage = "По регулярному выражению \"" + searchedPattern + "\" ничего не найдено.";
            return;
        }
        List<Integer> transitionBegin = new ArrayList<>();
        List<Integer> transitionEnd = new ArrayList<>();
        //находим границы переходов соответсвующих регулярному выражению
        for (int j = 0; j < stdPatternHits.size(); j++) {
            if (j + 1 < stdPatternHits.size()) {
                if (checkInTransition(searchHits, stdPatternHits.get(j), stdPatternHits.get(j + 1))){
                    transitionBegin.add(stdPatternHits.get(j));
                    transitionEnd.add(stdPatternHits.get(j + 1) - 1);
                }
            } else {
                transitionBegin.add(stdPatternHits.get(j));
                transitionEnd.add(logInList.size() - 1);
            }
        }
        String defaultName = "REGEX.txt";
        String newFileName = FileAction.newFileNameGenerator(newLog, defaultName);
        String newFilePath = newLog.getPath() + File.separator + newFileName;
        newFile = new File(newFilePath);
        FileAction.writeToNewFile(logInList, transitionBegin, transitionEnd, newFile);
        done = true;
    }

    @Override
    public List<Integer> searchByPattern(String pattern) {
        List<Integer> hitRows = new ArrayList<>();
        String line;
        for (int i = 0; i < logInList.size(); i++) {
            line = logInList.get(i);
            if (line.matches(pattern)) {
                hitRows.add(i);
            }
        }
        return hitRows;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean getDone() {
        return done;
    }

    @Override
    public String getNewFilePath() {
        return newFile.getAbsolutePath();
    }

    private boolean checkInTransition(List<Integer> searchHits, int begin, int end) {
        for (Integer row : searchHits) {
            if (row >= begin && row <= end ){
                return true;
            }
        }
        return false;
    }
}
