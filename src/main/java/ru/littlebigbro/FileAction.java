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

    public static List<String> readFileToList(File file) throws IOException {
        List<String> readFile;
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
        catch (IOException ioException) {
            ioException.printStackTrace();
            throw ioException;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                    inputStreamReader.close();
                    fileInputStream.close();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        return readFile;
    }

    public static List<String> readFileToList(InputStream inputStream) {
        List<String> readFile;
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        InputStream fileInputStream = null;
        try {
            fileInputStream = inputStream;
            inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            Stream<String> stream = reader.lines();
            readFile = stream.collect(Collectors.toList());
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                    inputStreamReader.close();
                    fileInputStream.close();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
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
        File[] filesList;
        if (filePath == null || filePath.getAbsolutePath().isEmpty()) {
            return "ERROR";
        } else {
            filesList= filePath.listFiles();
        }
        if (defaultName == null || defaultName.isEmpty()) {
            defaultName = "default_name.txt";
        }

        if (filesList != null && filesList.length != 0) {
            List <String> fileNames = new ArrayList<>();
            for (File file : filesList) {
                //обрезает путь до последнего сепаратора, поэтому чтобы он был добавляется "+ 1"
                fileNames.add(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1));
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

    public static String writeToNewFile(List<String> fileInList, List<Integer> transitionBegin, List<Integer> transitionEnd, File newFileName) throws IOException {
        String alert = "ERROR";
        if (fileInList == null || transitionBegin == null ||
                transitionEnd == null || newFileName == null ||
                newFileName.getAbsolutePath().isEmpty()) {
            return alert;
        }
        BufferedWriter writer = null;
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            fileOutputStream = new FileOutputStream(newFileName);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            writer = new BufferedWriter(outputStreamWriter);
            for (int i = 0; i < transitionBegin.size(); i++) {
                for (int j = transitionBegin.get(i); j <= transitionEnd.get(i); j++){
                    writer.write(fileInList.get(j) + "\n");
                }
            }
            alert = "SUCCESS";
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
            throw ioException;
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
        return alert;
    }
}
