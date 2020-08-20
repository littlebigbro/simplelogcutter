package ru.littlebigbro;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application {

    public static void main(String[] args) {
//        String filePath = "H:/Java/SimpleLogCutter/src/main/resources/pay.log.2020-07-28-0";
//        String userString = "59c13466-306c-492b-33c1-f12116313311";
//        String filePath = "H:/Java/SimpleLogCutter/src/main/resources/test_files/test.txt";
//        String userString = "d06da315-0565-472c-88e6-068619e4cfe8";
/*
+-  1) Бага. Жрёт память при каждом нажатии на кнопку "Обработать".
    Решение - запуск через Start.bat с ограничением памяти javaw -Xmx256m -jar SimpleLogCutter.jar
    другого решения не нашел
+   2) зашить паттерн в класс PayLogHandler
+   3) Необходимо поправить RegExHandler в чтении/записи из файла(как в PayLogHandler)
+   4) Проверить методы поиска начала/конца перехода в PayLogHandler, и сделать как в RegExHandler если возможно
+   5) записывать в новый файл если с таким именем уже существует.
+   6)  зашить trim в строку поиска,
+       строки с выбором пути закрыть на редактирование в подсказке выводить весь путь
+   7) добавить проверку на формат гуида для PayLogHandler, и для RegExHandler
+   8) выправить классы PayLogHandler и RegExHandler
    9) тесты?
-   10) Почистить репозиторий от лишних файлов
    11) красивости, проверить локаль
+-  12) в связи с п.1 необходимо сделать так чтобы jar запускался через конфиг с нужными параметрами
        сделано через Start.bat
    13) PROFIT
*/
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent panel = FXMLLoader.load(getClass().getClassLoader().getResource("ru.littlebigbro.fxml"));
        Scene scene = new Scene(panel, 475, 340);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Обработчик логов");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}