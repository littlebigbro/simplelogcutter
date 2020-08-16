package ru.littlebigbro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayLogHandler implements Handler {

    private final File LOG;
    private final File NEW_LOG;
    private final String SEARCHED_STRING;
    private List<String> logInList;
    private String errorMessage = "FALSE";
    private boolean done = false;

    public PayLogHandler(File log, File newLog, String searchString){
        this.LOG = log;
        this.SEARCHED_STRING = searchString.toLowerCase();
        this.NEW_LOG = newLog;
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
                String standardPattern = "(.*)Лог перехода(.*)для документа(.*)";
                String searchPattern = "(.*)Лог перехода(.*)для документа " + SEARCHED_STRING + "(.*)";
                List<Integer> stdPatternHits = searchByPattern(standardPattern.toLowerCase()); //находим начала всех переходов в логе
                List<Integer> transitionBegin = searchByPattern(searchPattern.toLowerCase()); //находим начала переходов по гуиду
                if (transitionBegin.isEmpty()) {
                    errorMessage = "По значению \"" + SEARCHED_STRING + "\" ничего не найдено.";
                } else {
                    List<Integer> transitionEnd = transitionEndCalculation(stdPatternHits, transitionBegin); //находим концы переходов по гуиду
                    String fileName = newFileNameGenerator(SEARCHED_STRING + ".txt");
                    for (int i = 0; i < transitionBegin.size(); i++) {
                        writeToNewFile(logInList, transitionBegin.get(i), transitionEnd.get(i), fileName);
                    }
                    done = true;
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
        String line;
        for (int i = 0; i < logInList.size(); i++) {
            line = logInList.get(i).toLowerCase();
            if (line.matches(pattern)) {
                hitRows.add(i);
            }
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
    public void writeToNewFile(List<String> oldLog, int begin, int end, String fileName) {
        String newFilePath = NEW_LOG.getPath() + File.separator + fileName;
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(newFilePath, true), StandardCharsets.UTF_8));;
            for (int i = begin; i <= end ; i++){
                writer.write(oldLog.get(i) + "\n");
            }
            writer.close();
        }
        catch (Exception ex){
            errorMessage = "Ошибка записи в файл " + fileName;
            ex.printStackTrace();
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean getDone() {
        return done;
    }

    public String newFileNameGenerator(String defaultName) {
        File[] fileList = NEW_LOG.listFiles();
        if (fileList != null && fileList.length != 0) {
            List <String> fileNames = new ArrayList<>();
            for (File file : fileList) {
                fileNames.add(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+ 1 ));
            }
            boolean inList = false;
            for (String fileName : fileNames) {
                if (fileName.equals(defaultName)) {
                    inList = true;
                    break;
                }
            }
            if (!inList) {
                return defaultName;
            }
            String dot = ".";
            String name = defaultName.substring(0, defaultName.indexOf(dot));// отсечь потенциальный (i)
            String format = defaultName.substring(defaultName.indexOf(dot));
            Pattern nextCreationPattern = Pattern.compile("(" + name + "[(])(\\d)([)]" + format +")");
            Matcher matcher;
            int numberOfFile = 1;
            for (String fileName : fileNames) {
                matcher = nextCreationPattern.matcher(fileName);
                if (matcher.find()) {
                    numberOfFile = Integer.parseInt(matcher.group(2));
                    numberOfFile++;
                }
            }
            return name + "(" + numberOfFile + ")" + format;
        } else {
            return defaultName;
        }
    }
}
