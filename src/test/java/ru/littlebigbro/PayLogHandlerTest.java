package ru.littlebigbro;

import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;


public class PayLogHandlerTest {
    private File log;
    private File newLog;
    private String searchedString;
    private Handler handler;
    private String errorMessage;

    @Before
    public void initTest() {
        log = new File("H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test.txt");
        searchedString = "d06da315-0565-472c-88e6-068619e4cfe8";
        newLog = new File("H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_output");
        handler = new PayLogHandler();
    }

    @After
    public void afterTest() {
        log = null;
        searchedString = null;
        newLog = null;
        handler = null;
        errorMessage = null;
    }

    @Test
    public void getDoneIsTrueTest() {
        handler.startAlgorithm(log, newLog, searchedString);
        assertTrue(handler.getDone());
    }

    @Test
    public void getDoneIsFalseTest() {
        searchedString = "d06da315-0565-472c-88e6-068619e4cfe0";
        handler.startAlgorithm(log, newLog, searchedString);
        assertFalse(handler.getDone());
    }

    @Test
    public void errorMessageSearchStringNotFoundTest() {
        searchedString = "d06da315-0565-472c-88e6-068619e4cfe0";
        errorMessage = "По значению \"" + searchedString + "\" ничего не найдено.";
        handler.startAlgorithm(log, newLog, searchedString);
        assertEquals(errorMessage, handler.getErrorMessage());
    }

    @Test
    public void errorMessageFileIsEmptyTest() {
        log = new File("H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test1.txt");
        errorMessage = "Обрабатываемый файл пуст";
        handler.startAlgorithm(log, newLog, searchedString);
        assertEquals(errorMessage, handler.getErrorMessage());
    }
}