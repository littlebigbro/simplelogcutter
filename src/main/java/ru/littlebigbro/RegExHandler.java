package ru.littlebigbro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegExHandler implements Handler {
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
            newLog = new File(localNewFilePath);
        }

        if (localSearchString == null || localSearchString.trim().isEmpty()) {
            errorMessage = "Поисковой запрос пуст";
            return;
        } else {
            try {
                Pattern.compile(localSearchString.trim());
                searchedString = localSearchString.trim();
            }
            catch (PatternSyntaxException e) {
                errorMessage = "Указанное регулярное выражение имеет неверный формат";
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
        //находим начала всех переходов в файле
        String STANDARD_PATTERN = "(.*)Лог перехода(.*)для документа(.*)";
        List<Integer> stdPatternHits = searchByPattern(STANDARD_PATTERN);
        if (stdPatternHits.isEmpty()) {
            errorMessage = "Выбранный файл не pay.log";
            return;
        }
        //находим строки соответствующие регулярному выражению
        List<Integer> searchHits = searchByPattern(searchedString);
        if (searchHits.isEmpty()) {
            errorMessage = "По регулярному выражению \"" + searchedString + "\" ничего не найдено.";
            return;
        }

        List<Integer> transitionBegin = new ArrayList<>();
        List<Integer> transitionEnd = new ArrayList<>();
        //находим границы переходов в которых есть совпадения по регулярному выражению
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
        if (!newFilePath.equals("ERROR")) {
            newFile = new File(newFilePath);
        } else {
            errorMessage = "Невозможно создать файл по указанному пути";
            return;
        }
        if(FileAction.writeToNewFile(logInList, transitionBegin, transitionEnd, newFile).equals("SUCCESS")) {
            done = true;
        } else {
            errorMessage = "Ошибка записи в файл";
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

    @Override
    public String getNewFilePath() {
        return newFile == null ? "FALSE" : newFile.getAbsolutePath();
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
