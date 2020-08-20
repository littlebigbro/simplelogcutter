package ru.littlebigbro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PayLogHandler implements Handler {

    private final File LOG;
    private final File NEW_LOG;
    private final String SEARCHED_STRING;
    private List<String> logInList;
    private String errorMessage = "FALSE";
    private boolean done = false;
    private File newFile;

    public PayLogHandler(File log, File newLog, String searchString){
        this.LOG = log;
        this.SEARCHED_STRING = searchString;
        this.NEW_LOG = newLog;
    }

    @Override
    public void startAlgorithm() {
        logInList = FileAction.readFileToList(LOG);
        if (logInList.isEmpty()) {
            errorMessage = "Обрабатываемый файл пуст";
        } else {
            String standardPattern = "(.*)Лог перехода(.*)для документа(.*)";
            String searchPattern = "(.*)Лог перехода(.*)для документа " + SEARCHED_STRING + "(.*)";
            List<Integer> stdPatternHits = searchByPattern(standardPattern); //находим начала всех переходов в логе
            List<Integer> transitionBegin = searchByPattern(searchPattern); //находим начала переходов по гуиду
            if (transitionBegin.isEmpty()) {
                errorMessage = "По значению \"" + SEARCHED_STRING + "\" ничего не найдено.";
            } else {
                List<Integer> transitionEnd = transitionEndCalc(stdPatternHits, transitionBegin); //находим концы переходов по гуиду
                String newFilePath = NEW_LOG.getPath() + File.separator + FileAction.newFileNameGenerator(NEW_LOG,SEARCHED_STRING + ".txt");
                newFile = new File(newFilePath);
                FileAction.writeToNewFile(logInList, transitionBegin, transitionEnd, newFile);
                done = true;
            }
        }
    }

    @Override
    public List<Integer> searchByPattern(String pattern) {
        List<Integer> hitRows = new ArrayList<Integer>();
        for (int i = 0; i < logInList.size(); i++) {
            if (Pattern.matches(pattern, logInList.get(i))) {     //быстро, но больше памяти жрёт
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
        List <Integer> transitionEnd = new ArrayList<Integer>();
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
