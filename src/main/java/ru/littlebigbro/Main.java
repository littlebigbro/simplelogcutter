package ru.littlebigbro;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
/*
+-  1) тесты?
    2) Добавить "О программе" в саму программу.
+-  3) Бага. Жрёт память при каждом нажатии на кнопку "Обработать".
    Решение - запуск через Start.bat с ограничением памяти javaw -Xmx256m -jar SimpleLogCutter.jar
    другого решения не нашел
-   4) Почистить репозиторий от лишних файлов
+-  5) проверить локаль
    локаль у контекстного меню не подтягивается через .properties.
    6) PROFIT
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