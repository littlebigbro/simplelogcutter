package ru.littlebigbro;
import java.io.*;

public class Main {
    public static void main(String[] args) {
//        String filePath = "H:/Java/SimpleLogCutter/src/main/resources/pay.log.2020-07-28-0";
//        String userString = "59c13466-306c-492b-33c1-f12116313311";
        String filePath = "H:/Java/SimpleLogCutter/src/main/resources/test_files/test.txt";
        String userString = "d06da315-0565-472c-88e6-068619e4cfe8";
        String payLogStdPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + ".*$";
        String payLogSearchPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + userString + ".*$";
        File log = new File(filePath);
        
        Handler fileHandler = new Handler(log, userString, payLogStdPattern, payLogSearchPattern); // Можно сделать через лист параметров.

    }
}