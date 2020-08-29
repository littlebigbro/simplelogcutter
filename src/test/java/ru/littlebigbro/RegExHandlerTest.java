package ru.littlebigbro;

import org.junit.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RegExHandlerTest {
    private Handler handler;
    private String errorMessage;
    private Map<String, String> params;

    @Before
    public void setUp() {
        handler = new RegExHandler();
        params = new HashMap<>();
        params.put("FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test.txt");
        params.put("NEW_FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_output");
        params.put("SEARCH_STRING", ".*d06da315-0565-472c-88e6-068619e4cfe8.*");
    }

    @After
    public void tearDown() {
        handler = null;
        params = null;
        errorMessage = null;
    }

    @Test
    public void performingChecksNullParamsTest() throws IOException {
        params.put("FILE_PATH", null);
        params.put("NEW_FILE_PATH", null);
        params.put("SEARCH_STRING", null);
        handler.performingChecks(params);
        Assert.assertEquals("Не указан путь к обрабатываемому файлу", handler.getErrorMessage());

        handler = new RegExHandler();
        params.put("FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test.txt");
        handler.performingChecks(params);
        Assert.assertEquals("Поисковой запрос пуст", handler.getErrorMessage());
    }

    @Test
    public void wrongInputFileFormatTest() throws IOException {
        params.put("FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\pay.log.2020-07-28-0");
        handler.performingChecks(params);
        Assert.assertEquals("Ошибка. Неверный формат файла. Поддерживаемые форматы: .txt и .log", handler.getErrorMessage());

        handler = new RegExHandler();
        params.put("FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test");
        handler.performingChecks(params);
        Assert.assertEquals("Ошибка. Неверный формат файла. Поддерживаемые форматы: .txt и .log", handler.getErrorMessage());
    }

    @Test
    public void wrongSearchStringTest() throws IOException {
        params.put("SEARCH_STRING", "*123");
        handler.performingChecks(params);
        Assert.assertEquals("Указанное регулярное выражение имеет неверный формат", handler.getErrorMessage());

        handler = new RegExHandler();
        String regEx = "    ";
        params.put("SEARCH_STRING", regEx);
        handler.performingChecks(params);
        Assert.assertEquals("Поисковой запрос пуст", handler.getErrorMessage());
    }

    @Test
    public void getDoneIsTrueTest() throws IOException {
        handler.performingChecks(params);
        Assert.assertTrue(handler.getDone());

        handler = new RegExHandler();
        params.put("SEARCH_STRING", "         .*MSC.*     ");
        handler.performingChecks(params);
        Assert.assertTrue(handler.getDone());
    }

    @Test
    public void getDoneIsFalseTest() throws IOException {
        params.put("SEARCH_STRING", "1asd1");
        handler.performingChecks(params);
        assertFalse(handler.getDone());
    }

    @Test
    public void errorMessageFileIsEmptyTest() throws IOException {
        params.put("FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test_empty.txt");
        handler.performingChecks(params);
        Assert.assertEquals("Обрабатываемый файл пуст", handler.getErrorMessage());
    }

    @Test
    public void errorMessageFileIsNotLogTest() throws IOException {
        params.put("FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test_not_log.txt");
        handler.performingChecks(params);
        Assert.assertEquals("Выбранный файл не pay.log", handler.getErrorMessage());
    }

    @Test
    public void errorMessageSearchStringNotFoundTest() throws IOException {
        params.put("SEARCH_STRING", ".*d06da315-0565-472c-88e6-068619e4cfe8.*");
        params.put("FILE_PATH", "H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test_nothing_found.txt");
        errorMessage = "По регулярному выражению \"" + params.get("SEARCH_STRING") + "\" ничего не найдено.";
        handler.performingChecks(params);
        Assert.assertEquals(errorMessage, handler.getErrorMessage());
    }
}