package ru.littlebigbro;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class RegExHandlerTest {
    private File log;
    private File newLog;
    private String searchedString;
    private Handler handler;
    private String errorMessage;

    @Before
    public void setUp() {
        log = new File("H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_files\\test.txt");
        searchedString = ".*MSC_.*";
        newLog = new File("H:\\Java\\SimpleLogCutter\\src\\test\\java\\resources\\test_output");
        handler = new RegExHandler();
    }

    @After
    public void tearDown() {
        log = null;
        searchedString = null;
        newLog = null;
        handler = null;
        errorMessage = null;
    }

    @Test
    public void getDoneIsTrueTest() {
        handler.startAlgorithm(log, newLog, searchedString);
        Assert.assertTrue(handler.getDone());
    }

    @Test
    public void getDoneIsFalseTest() {
        searchedString = "97";
        handler.startAlgorithm(log, newLog, searchedString);
        Assert.assertFalse(handler.getDone());
    }

    @Test
    public void errorMessageSearchStringNotFoundTest() {
        searchedString = "97";
        errorMessage = "По регулярному выражению \"" + searchedString + "\" ничего не найдено.";
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