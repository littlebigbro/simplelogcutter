package ru.littlebigbro;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ControllerTest {
    private Controller controller;
    private ProcessingTemplate processingTemplate;
    private String searchString;
    private String filePath;
    private String saveDirectoryPath;
    private Handler handler;

    @Before
    public void initTest() {
        controller = new Controller();
        filePath = "./src/test/java/resources/test_files/test.txt";
        saveDirectoryPath = "./src/test/java/resources/test_output";
        processingTemplate = new ProcessingTemplate();
    }

    @After
    public void afterTest() {
        controller = null;
        handler = null;
        processingTemplate = null;
        filePath = null;
        saveDirectoryPath = null;
    }

    @Test
    public void ripTheDevilOutOfItPayLogHandlerSuccessTest() throws IOException {
        searchString = "d06da315-0565-472c-88e6-068619e4cfe8";

        Map<String, String> params = new HashMap<>();
        params.put("FILE_PATH", filePath);
        params.put("NEW_FILE_PATH", saveDirectoryPath);
        params.put("SEARCH_STRING", searchString.trim());

        handler = new PayLogHandler();
        processingTemplate.setHandler(handler);
        processingTemplate.executeHandler(params);

        Assert.assertEquals("FALSE", handler.getErrorMessage());
        Assert.assertNotNull(handler.getNewFilePath());
        Assert.assertTrue(handler.getDone());
    }

    @Test
    public void ripTheDevilOutOfItPayLogHandlerFailTest() throws IOException {
        searchString = "d06da315-0565-472c-88e6-068619e4cfe";

        Map<String, String> params = new HashMap<>();
        params.put("FILE_PATH", filePath);
        params.put("NEW_FILE_PATH", saveDirectoryPath);
        params.put("SEARCH_STRING", searchString.trim());

        handler = new PayLogHandler();
        processingTemplate.setHandler(handler);
        processingTemplate.executeHandler(params);

        Assert.assertNotEquals("FALSE", handler.getErrorMessage());
        Assert.assertEquals("Указанный GUID имеет неверный формат", handler.getErrorMessage());

        params = new HashMap<>();
        params.put("FILE_PATH", null);
        params.put("NEW_FILE_PATH", null);
        params.put("SEARCH_STRING", null);

        handler = new PayLogHandler();
        processingTemplate.setHandler(handler);
        processingTemplate.executeHandler(params);

        Assert.assertNotEquals("FALSE", handler.getErrorMessage());
        Assert.assertEquals("Не указан путь к обрабатываемому файлу", handler.getErrorMessage());
    }

    @Test
    public void ripTheDevilOutOfItRegExHandlerSuccessTest() throws IOException {
        searchString = ".*d06da315-0565-472c-88e6-068619e4cfe8.*";
        handler = new RegExHandler();

        Map<String,String> params = new HashMap<>();
        params.put("FILE_PATH", filePath);
        params.put("NEW_FILE_PATH", saveDirectoryPath);
        params.put("SEARCH_STRING", searchString.trim());

        processingTemplate.setHandler(handler);
        processingTemplate.executeHandler(params);

        Assert.assertEquals("FALSE", handler.getErrorMessage());
        Assert.assertNotNull(handler.getNewFilePath());
        Assert.assertTrue(handler.getDone());


    }

    @Test
    public void ripTheDevilOutOfItRegExHandlerFailTest() throws IOException {
        searchString = "*d06da315-0565-472c-88e6-068619e4cfe";

        Map<String, String> params = new HashMap<>();
        params.put("FILE_PATH", filePath);
        params.put("NEW_FILE_PATH", saveDirectoryPath);
        params.put("SEARCH_STRING", searchString.trim());

        handler = new RegExHandler();
        processingTemplate.setHandler(handler);
        processingTemplate.executeHandler(params);

        Assert.assertNotEquals("FALSE", handler.getErrorMessage());
        Assert.assertEquals("Указанное регулярное выражение имеет неверный формат", handler.getErrorMessage());

        handler = new RegExHandler();
        params = new HashMap<>();
        params.put("FILE_PATH", null);
        params.put("NEW_FILE_PATH", null);
        params.put("SEARCH_STRING", null);

        processingTemplate.setHandler(handler);
        processingTemplate.executeHandler(params);

        Assert.assertNotEquals("FALSE", handler.getErrorMessage());
        Assert.assertEquals("Не указан путь к обрабатываемому файлу", handler.getErrorMessage());
    }
}