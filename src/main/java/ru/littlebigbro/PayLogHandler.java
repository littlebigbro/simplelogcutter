package ru.littlebigbro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PayLogHandler implements Handler {

    private File log;
    private File newLog;
    private String searchedString;
    private List<String> logInList;
    private String errorMessage = "FALSE";
    private boolean done = false;
    private File newFile;
    private final String FILE_PATH = "FILE_PATH";
    private final String NEW_FILE_PATH = "NEW_FILE_PATH";
    private final String SEARCH_STRING = "SEARCH_STRING";

    @Override
    public void performingChecks(Map<String, String> params) throws IOException {

        String localFilePath = params.get(FILE_PATH);
        String localNewFilePath = params.get(NEW_FILE_PATH);
        String localSearchString = params.get(SEARCH_STRING);

        if (localFilePath == null || localFilePath.isEmpty()) {
            errorMessage = "Не указан путь к обрабатываемому файлу";
            return;
        } else {
            log = new File(localFilePath);
            String logName = log.getName();
            if (logName.lastIndexOf('.')!= -1 && logName.lastIndexOf('.')!= 0) {
                String format = logName.substring(logName.lastIndexOf(".") + 1);
                if (!(format.equals("txt") || format.equals("log"))) {
                    errorMessage = "Ошибка. Неверный формат файла. Поддерживаемые форматы: .txt и .log";
                    return;
                }
            } else {
                errorMessage = "Ошибка. Неверный формат файла. Поддерживаемые форматы: .txt и .log";
                return;
            }
        }

        if (localNewFilePath == null || localNewFilePath.isEmpty()) {
            String newLogPath = log.getAbsolutePath().substring(0, log.getAbsolutePath().lastIndexOf(File.separator));
            newLog = new File(newLogPath);
        } else {
            newLog = new File(params.get(NEW_FILE_PATH));
        }

        if (localSearchString == null || localSearchString.trim().isEmpty()) {
            errorMessage = "Поисковой запрос пуст";
            return;
        } else {
            if (localSearchString.trim().matches("[0-9a-zA-Z]{8}[-][0-9a-zA-Z]{4}[-][0-9a-zA-Z]{4}[-][0-9a-zA-Z]{4}[-][0-9a-zA-Z]{12}")) {
                searchedString = localSearchString.trim();
            } else {
                errorMessage = "Указанный GUID имеет неверный формат";
                return;
            }
        }
        startAlgorithm();
    }

    @Override
    public void startAlgorithm() throws IOException {
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
        if (!newFilePath.equals("ERROR")) {
            newFile = new File(newFilePath);
        } else {
            errorMessage = "Невозможно создать файл по указанному пути";
            return;
        }
        if (FileAction.writeToNewFile(logInList, transitionBegin, transitionEnd, newFile).equals("SUCCESS")) {
            done = true;
        } else {
            errorMessage = "Ошибка записи в файл";
        }
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
        return newFile == null? "FALSE" : newFile.getAbsolutePath();
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
