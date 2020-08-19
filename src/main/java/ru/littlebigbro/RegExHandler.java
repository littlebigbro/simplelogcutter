package ru.littlebigbro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegExHandler implements Handler {
    private final File LOG;
    private final File NEW_LOG;
    private final String STANDARD_PATTERN = "(.*)Лог перехода(.*)для документа(.*)";
    private final String SEARCH_PATTERN;
    private List<String> logInList;
    private String errorMessage = "FALSE";
    private boolean done = false;

    public RegExHandler(File log, File saveLog, String searchString){
        this.LOG = log;
        this.SEARCH_PATTERN = searchString;
        if (saveLog.getPath().isEmpty()) {
            String absolutePath = log.getAbsolutePath();
            String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
            NEW_LOG = new File(filePath);
        } else {
            this.NEW_LOG = saveLog;
        }
    }

    @Override
    public void startAlgorithm() {
        logInList = FileAction.readFileToList(LOG);
        if (logInList.isEmpty()) {
            errorMessage = "Обрабатываемый файл пуст";
        } else {
            List<Integer> stdPatternHits = searchByPattern(STANDARD_PATTERN); //находим начало переходов
            List<Integer> searchHits = searchByPattern(SEARCH_PATTERN); //находим строки соответствующие шаблону
            if (searchHits.isEmpty()) {
                errorMessage = "По регулярному выражению \"" + SEARCH_PATTERN + "\" ничего не найдено.";
            } else {
                List<Integer> transitionBegin = new ArrayList<>();
                List<Integer> transitionEnd = new ArrayList<>();
                //находим границы переходов
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
                String newFileName = FileAction.newFileNameGenerator(NEW_LOG, defaultName);
                String newFilePath = NEW_LOG.getPath() + File.separator + newFileName;
                File newFile = new File(newFilePath);
                FileAction.writeToNewFile(logInList, transitionBegin, transitionEnd, newFile);
                done = true;
            }
        }
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

    private boolean checkInTransition(List<Integer> searchHits, int begin, int end) {
        for (Integer row : searchHits) {
            if (row >= begin && row <= end ){
                return true;
            }
        }
        return false;
    }
}
