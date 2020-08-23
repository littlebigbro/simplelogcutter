package ru.littlebigbro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PayLogHandler implements Handler {

    private List<String> logInList;
    private String errorMessage = "FALSE";
    private boolean done = false;
    private File newFile;


    @Override
    public void startAlgorithm(File log, File newLog, String searchedString) {

        logInList = FileAction.readFileToList(log);
        if (logInList.isEmpty()) {
            errorMessage = "Обрабатываемый файл пуст";
            return;
        }
        String standardPattern = "(.*)Лог перехода(.*)для документа(.*)";
        String searchPattern = "(.*)Лог перехода(.*)для документа " + searchedString + "(.*)";
        List<Integer> stdPatternHits = searchByPattern(standardPattern); //находим начала всех переходов в логе
        if (stdPatternHits.isEmpty()){
            errorMessage = "Выбранный файл не pay.log";
            return;
        }
        List<Integer> transitionBegin = searchByPattern(searchPattern); //находим начала переходов по гуиду
        if (transitionBegin.isEmpty()) {
            errorMessage = "По значению \"" + searchedString + "\" ничего не найдено.";
            return;
        }
        List<Integer> transitionEnd = transitionEndCalc(stdPatternHits, transitionBegin); //находим концы переходов по гуиду
        String newFilePath = newLog.getPath() + File.separator + FileAction.newFileNameGenerator(newLog, searchedString + ".txt");
        newFile = new File(newFilePath);
        FileAction.writeToNewFile(logInList, transitionBegin, transitionEnd, newFile);
        done = true;
    }

    @Override
    public List<Integer> searchByPattern(String pattern) {
        List<Integer> hitRows = new ArrayList<>();
        //быстро, но больше памяти жрёт
        for (int i = 0; i < logInList.size(); i++) {
            if (Pattern.matches(pattern, logInList.get(i))) {
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

    private List<Integer> transitionEndCalc(List<Integer> stdPatternHits, List<Integer> transitionBegin) {
        List <Integer> transitionEnd = new ArrayList<>();
        for (Integer row : transitionBegin) {
            for (int j = 0; j < stdPatternHits.size(); j++) {
                if (row.intValue() == stdPatternHits.get(j).intValue()) {
                    if (j + 1 < stdPatternHits.size()) {
                        transitionEnd.add(stdPatternHits.get(j + 1) - 1);
                    } else {
                        transitionEnd.add(logInList.size() - 1);
                    }
                }
            }
        }
        return transitionEnd;
    }
}
