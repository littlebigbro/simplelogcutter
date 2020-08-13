package ru.littlebigbro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegExHandler implements Handler {
    private final File LOG;
    private final File NEW_LOG;
    private final String STANDARD_PATTERN;
    private final String SEARCH_PATTERN;
    private String newFilePath;
    private List<String> logInList;
    private String errorMessage = "FALSE";
    private String done = "FALSE";

    public RegExHandler(File log, File saveLog, String stdPattern, String searchPattern){
        this.LOG = log;
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
        if (STANDARD_PATTERN.isEmpty()) {
            System.out.println("Поисковой запрос пуст");
        } else {
            logInList = readFileAndRewriteInList();
            if (logInList.isEmpty()) {
                System.out.println("Файл пуст");
            } else {
                List<Integer> transitionBegin = searchByPattern(STANDARD_PATTERN); //находим начало переходов
                List<Integer> searchHits = searchByPattern(SEARCH_PATTERN); //находим строки соответствующие шаблону
                if (transitionBegin.isEmpty()) {
                    System.out.println("По регулярному выражению \"" + STANDARD_PATTERN + "\" ничего не найдено.");
                } else {
                    List<Integer> transitionEnd = transitionEndCalculation(transitionBegin); //находим концы переходов
                    newFilePath = NEW_LOG.getPath() + File.separator + "regEx.txt";
                    for (int i = 0; i < transitionBegin.size(); i++) {
                        if(checkInTransition(searchHits, transitionBegin.get(i), transitionEnd.get(i))){
                            writeToNewFile(logInList, transitionBegin.get(i), transitionEnd.get(i));
                        }
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
            FileReader logReader = new FileReader(LOG);
            BufferedReader reader = new BufferedReader(logReader);
            String line;
            while ((line = reader.readLine()) != null) {
                readFile.add(line);
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println("Ошибка в логе: " + LOG.getName());
            ex.printStackTrace();
        }
        return readFile;
    }

    @Override
    public List<Integer> searchByPattern(String pattern) {
        List<Integer> hitRows = new ArrayList<>();
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
    public void writeToNewFile(List<String> oldLog, int begin, int end) {
        FileWriter logWriter;
        BufferedWriter writer;
        try {
            logWriter = new FileWriter(newFilePath,true);
            writer = new BufferedWriter(logWriter);
            for (int i = begin; i <= end ; i++){
                writer.write(oldLog.get(i) + "\n");
            }
            writer.close();
            logWriter.close();
        }
        catch (Exception ex){
            System.out.println("Ошибка записи в файл regEx.txt");
            ex.printStackTrace();
        }
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public String getDone() {
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
