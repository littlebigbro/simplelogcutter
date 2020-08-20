package ru.littlebigbro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAction {

    public static List<String> readFileToList(File file) {
        List<String> readFile = new ArrayList<String>();
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            Stream<String> stream = reader.lines();
            readFile = stream.collect(Collectors.toList());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                    inputStreamReader.close();
                    fileInputStream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return readFile;
    }
/**
 * Метод генерирует новое имя для файла в формате "fileName(i).format", 
 * если по предоставленному пути filepath есть файл с именем defaultName.
 * */
    public static String newFileNameGenerator(File filePath, String defaultName) {
        File[] filesList = filePath.listFiles();
        if (filesList != null && filesList.length != 0) {
            List <String> fileNames = new ArrayList<String>();
            for (File file : filesList) {
                fileNames.add(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1));//обрезает путь без сепаратора если убрать " + 1"
            }
            if (!fileNames.contains(defaultName)) {
                return defaultName;
            } else {
                char dot = '.';
                String name = defaultName.substring(0, defaultName.indexOf(dot));
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
            }
        } else {
            return defaultName;
        }
    }

    public static void writeToNewFile(List<String> fileInList, List<Integer> transitionBegin, List<Integer> transitionEnd, File fileName) {
        BufferedWriter writer = null;
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            writer = new BufferedWriter(outputStreamWriter);
            for (int i = 0; i < transitionBegin.size(); i++) {
                for (int j = transitionBegin.get(i); j <= transitionEnd.get(i); j++){
                    writer.write(fileInList.get(j) + "\n");
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                    outputStreamWriter.close();
                    fileOutputStream.close();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
