package ru.littlebigbro;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
//        String filePath = "H:/Java/SimpleLogCutter/src/main/resources/pay.log.2020-07-28-0";
//        String userString = "59c13466-306c-492b-33c1-f12116313311";
//        String filePath = "H:/Java/SimpleLogCutter/src/main/resources/test_files/test.txt";
//        String userString = "d06da315-0565-472c-88e6-068619e4cfe8";
/*
    1) Бага. Жрёт память при каждом нажатии на кнопку "Обработать".
    2) Необходимо поправить RegExHandler в чтении/записи из файла(как в PayLogHandler)
    3) Проверить методы поиска начала/конца перехода в PayLogHandler, и сделать как в RegExHandler если возможно
    4) тесты?
    5) Почистить репозиторий от лишний файлов
* */
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent panel = FXMLLoader.load(getClass().getResource("ru.littlebigbro.fxml"));
        Scene scene = new Scene(panel, 475, 340);
        primaryStage.setTitle("Log Handler");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}