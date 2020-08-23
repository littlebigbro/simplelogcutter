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

+-  1) тесты?
    2) Добавить "О программе" в саму программу.
+-  3) Бага. Жрёт память при каждом нажатии на кнопку "Обработать".
    Решение - запуск через Start.bat с ограничением памяти javaw -Xmx256m -jar SimpleLogCutter.jar
    другого решения не нашел
-   4) Почистить репозиторий от лишних файлов
+-  5) красивости, проверить локаль
    локаль у контекстного меню не подтягивается через .properties.
+-  6) в связи с п.1 необходимо сделать так чтобы jar запускался через конфиг с нужными параметрами
        сделано через Start.bat
    7) PROFIT
*/
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        ResourceBundle bundle = ResourceBundle.getBundle("context_menu", new Locale("ru", "RU"));
        Parent panel = FXMLLoader.load(getClass().getClassLoader().getResource("ru.littlebigbro.fxml"));
        Scene scene = new Scene(panel, 475, 340);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Обработчик логов");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}