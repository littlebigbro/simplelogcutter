package ru.littlebigbro;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileActionTest {
    private String filePath;

    @Test (expected = NullPointerException.class)
    public void readFileToListFilePathIsNull() throws IOException {
        filePath = null;
        FileAction.readFileToList(new File(filePath));
    }

    @Test (expected = FileNotFoundException.class)
    public void readFileToListFileNotExist() throws IOException {
        filePath = "H:\\test0.txt";
        FileAction.readFileToList(new File(filePath));
    }

    @Test
    public void newFileNameGenerator() {
        Assert.assertEquals("ERROR", FileAction.newFileNameGenerator(null,null));

        File file = new File("C:\\Users\\Home\\Desktop\\");
        Assert.assertEquals("default_name.txt", FileAction.newFileNameGenerator(file,null));

        file = new File ("H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\");
        Assert.assertEquals("test(1).txt", FileAction.newFileNameGenerator(file,"test.txt"));
    }

    @Test
    public void writeToNewFileWithNullParams() throws IOException {
        List<String> fileInList = new ArrayList<>();
        fileInList.add("YES");
        List<Integer> trBegin = new ArrayList<>();
        trBegin.add(0);
        List<Integer> trEnd = new ArrayList<>();
        trEnd.add(0);
        File newFile = new File("H:\\1.txt");
        Assert.assertEquals("ERROR", FileAction.writeToNewFile(null,null,null,null));
        Assert.assertEquals("ERROR", FileAction.writeToNewFile(fileInList,null,null,null));
        Assert.assertEquals("ERROR", FileAction.writeToNewFile(fileInList,trBegin,null,null));
        Assert.assertEquals("ERROR", FileAction.writeToNewFile(fileInList,trBegin,trEnd,null));
        Assert.assertEquals("SUCCESS", FileAction.writeToNewFile(fileInList,trBegin,trEnd, newFile));
    }
}