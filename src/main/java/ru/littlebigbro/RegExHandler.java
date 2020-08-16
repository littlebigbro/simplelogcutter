package ru.littlebigbro;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
        if (SEARCH_PATTERN.isEmpty()) {
            errorMessage = "Поисковой запрос пуст";
        } else {
            logInList = readFileAndRewriteInList();
            if (logInList.isEmpty()) {
                errorMessage = "Файл пуст";
            } else {
                List<Integer> transitionBegin = searchByPattern(STANDARD_PATTERN); //находим начало переходов
                List<Integer> searchHits = searchByPattern(SEARCH_PATTERN); //находим строки соответствующие шаблону
                if (transitionBegin.isEmpty()) {
                    errorMessage = "По регулярному выражению \"" + STANDARD_PATTERN + "\" ничего не найдено.";
                } else {
                    List<Integer> transitionEnd = transitionEndCalculation(transitionBegin); //находим концы переходов
                    String defaultName = "REGEX.txt";
                    String newFileName = newFileNameGenerator(defaultName);
                    for (int i = 0; i < transitionBegin.size(); i++) {
                        if(checkInTransition(searchHits, transitionBegin.get(i), transitionEnd.get(i))){
                            writeToNewFile(logInList, transitionBegin.get(i), transitionEnd.get(i), newFileName);
                        }
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
            line = logInList.get(i);
            if (line.matches(pattern)) {
                hitRows.add(i);
            }
        }
        return hitRows;
    }

    private List<Integer> transitionEndCalculation(List<Integer> stdPatternHits) {
        List <Integer> transitionEnd = new ArrayList<>();
        for (int j = 0; j < stdPatternHits.size(); j++) {
            if (j + 1 < stdPatternHits.size()) {
                transitionEnd.add(stdPatternHits.get(j + 1) - 1);
            } else {
                transitionEnd.add(logInList.size() - 1);
            }
        }
        return transitionEnd;
    }

    @Override
    public void writeToNewFile(List<String> oldLog, int begin, int end, String newFileName) {
        String newFilePath = NEW_LOG.getPath() + File.separator + newFileName;
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter (
                            new FileOutputStream(newFilePath, true), StandardCharsets.UTF_8));
            for (int i = begin; i <= end ; i++){
                writer.write(oldLog.get(i) + "\n");
            }
            writer.close();
        }
        catch (Exception ex){
            errorMessage = "Ошибка записи в файл " + newFileName;
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

    private boolean checkInTransition(List<Integer> searchHits, int begin, int end) {
        for (Integer row : searchHits) {
            if (row >= begin && row <= end ){
                return true;
            }
        }
        return false;
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
