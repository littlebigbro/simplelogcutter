package ru.littlebigbro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PayLogHandler implements Handler {

    private final File LOG;
    private final File NEW_LOG;
    private final String SEARCHED_STRING;
    private final String STANDARD_PATTERN;
    private final String SEARCH_PATTERN;
    private List<String> logInList;
    private String errorMessage = "FALSE";
    private String done = "FALSE";

    public PayLogHandler(File log, File saveLog, String searchString, String stdPattern, String searchPattern){
        this.LOG = log;
        this.SEARCHED_STRING = searchString;
        this.STANDARD_PATTERN = stdPattern;
        this.SEARCH_PATTERN = searchPattern;
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
        if (SEARCHED_STRING.isEmpty()) {
            errorMessage = "Поисковой запрос пуст";
        } else {
            logInList = readFileAndRewriteInList();
            if (logInList.isEmpty()) {
                errorMessage = "Файл пуст";
            } else {
                System.out.println(logInList.get(1));
                List<Integer> stdPatternHits = searchByPattern(STANDARD_PATTERN);
                List<Integer> transitionBegin = searchByPattern(SEARCH_PATTERN); //находим начало переходов
                if (transitionBegin.isEmpty()) {
                    errorMessage = "По значению \"" + SEARCHED_STRING + "\" ничего не найдено.";
                } else {
                    List<Integer> transitionEnd = transitionEndCalculation(stdPatternHits, transitionBegin); //находим концы переходов
                    for (int i = 0; i < transitionBegin.size(); i++) {
                        writeToNewFile(logInList, transitionBegin.get(i), transitionEnd.get(i));
                    }
                    done = "Готово!";
                }
            }
        }
    }

    @Override
    public List<String> readFileAndRewriteInList() {
        List<String> readFile = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(LOG), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                readFile.add(line);
            }
            reader.close();
        } catch (Exception ex) {
            errorMessage = "Ошибка в логе: " + LOG.getName();
            ex.printStackTrace();
        }
        return readFile;
    }

    @Override
    public List<Integer> searchByPattern(String pattern) {
        List<Integer> hitRows = new ArrayList<>();
//        String line;
//        for (int i = 0; i < logInList.size(); i++) {
//            line = logInList.get(i);
//            if (line.matches(pattern)) {
//                hitRows.add(i);
//            }
//        }
        String line;
        System.out.println(pattern);
        for (int i = 0; i < logInList.size(); i++) {
            line = logInList.get(i);
            if (line.matches(pattern)) {
                hitRows.add(i);
            }
        }
        for (Integer row: hitRows) {
            System.out.println(row.toString());
        }
        return hitRows;
    }

    private List<Integer> transitionEndCalculation(List<Integer> stdPatternHits, List<Integer> transitionBegin) {
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

    @Override
    public void writeToNewFile(List<String> oldLog, int begin, int end) {
        String newFilePath = NEW_LOG.getPath() + File.separator + SEARCHED_STRING + ".txt";
        try {
            //FileWriter logWriter = new FileWriter(newFilePath);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(newFilePath), StandardCharsets.UTF_8));;
            for (int i = begin; i <= end ; i++){
                writer.write(oldLog.get(i) + "\n");
            }
            writer.close();
        }
        catch (Exception ex){
            errorMessage = "Ошибка записи в файл " + SEARCHED_STRING;
            ex.printStackTrace();
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getDone() {
        return done;
    }
}
