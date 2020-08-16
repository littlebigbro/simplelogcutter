package ru.littlebigbro;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
    private final String PAYLOG_PATTERN = "Шаблон для pay.log с поиском по GUID";
    private final String REGEX_PATTERN = "Регулярное выражение";
    private final String ERROR = "Ошибка";
    private final String DONE = "Готово!";


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField filePath;

    @FXML
    private Tooltip chooseFileTooltip;

    @FXML
    private Button chooseFilePathButton;

    @FXML
    private Tooltip chooseFileButtonTooltip;

    @FXML
    private TextField saveDirectoryPath;

    @FXML
    private Tooltip chooseFolderTooltip;

    @FXML
    private Button chooseSavePathButton;

    @FXML
    private Tooltip saveFolderButtonTooltip;

    @FXML
    private ComboBox<String> patternBox;

    @FXML
    private TextField searchString;

    @FXML
    private Button handlerButton;

    @FXML
    private Tooltip ripTheDevilTooltip;

    @FXML
    void changePattenAction(ActionEvent event) {
        switch (patternBox.getValue()) {
            case PAYLOG_PATTERN : {
                searchString.setPromptText("Укажите GUID");
                searchString.setTooltip(new Tooltip("Укажите GUID"));
                break;
            }

            case REGEX_PATTERN : {
                searchString.setPromptText("Укажите регулярное выражение");
                searchString.setTooltip(new Tooltip("Укажите регулярное выражение"));
                break;
            }
        }
    }

    @FXML
    void chooseFilePathAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage PrimaryStage = (Stage) source.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        if (!filePath.getText().isEmpty()) {
            String fileDirectoryPath = filePath.getText().substring(0, filePath.getText().lastIndexOf(File.separator));
            fileChooser.setInitialDirectory(new File(fileDirectoryPath));
        }
        fileChooser.getExtensionFilters().addAll();
        fileChooser.setTitle("Выбор файла");
        File fileObject = fileChooser.showOpenDialog(PrimaryStage);
        if (fileObject != null) {
            filePath.setText(fileObject.getPath());
        }
        chooseFileTooltip.setText(filePath.getText());
    }

    @FXML
    void chooseSaveDirectoryAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage PrimaryStage = (Stage) source.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (!saveDirectoryPath.getText().isEmpty()) {
            directoryChooser.setInitialDirectory(new File(saveDirectoryPath.getText()));
        }
        directoryChooser.setTitle("Выбор папки сохранения");
        File directoryObject = directoryChooser.showDialog(PrimaryStage);
        if (directoryObject != null) {
            saveDirectoryPath.setText(directoryObject.getPath());
        }
        chooseFolderTooltip.setText(saveDirectoryPath.getText());
    }

    @FXML
    void ripTheDevilOutOfIt(ActionEvent event) {
        String userString = searchString.getText().trim();
        String alertMessage;
        if (saveDirectoryPath.getText().isEmpty()) {
            saveDirectoryPath.setText(filePath.getText().substring(0, filePath.getText().lastIndexOf(File.separator)));
        }
        if(!filePath.getText().isEmpty()) {
            Handler handler;
            File log = new File(filePath.getText());
            File saveLog = new File(saveDirectoryPath.getText());
            ProcessingTemplate processingTemplate = new ProcessingTemplate();
            switch (patternBox.getValue()) {
                case PAYLOG_PATTERN : {
                    handler = new PayLogHandler(log, saveLog, userString);
                    processingTemplate.setHandler(handler);
                    processingTemplate.executeHandler();
                    break;
                }

                case REGEX_PATTERN : {
                    handler = new RegExHandler(log, saveLog, userString);
                    processingTemplate.setHandler(handler);
                    processingTemplate.executeHandler();
                    break;
                }

                default : {
                    handler = new PayLogHandler(log, saveLog, userString);
                    processingTemplate.setHandler(handler);
                    processingTemplate.executeHandler();
                }
            }
            if(!handler.getErrorMessage().equals("FALSE") || handler.getDone()) {
                String alertType = ERROR;
                if (handler.getDone()){
                    alertType = DONE;
                    alertMessage = "Обработка завершена. Файл сохранён по пути: " + saveDirectoryPath.getText();
                } else {
                    alertMessage = handler.getErrorMessage();
                }
                alertGenerator(alertType, alertMessage);

            }
        } else {
            alertMessage = "Не указан путь к обрабатываемому файлу";
            alertGenerator(ERROR, alertMessage);
        }
    }

    @FXML
    void userStringFillAction(ActionEvent event) {
    }

    @FXML
    void initialize() {
        List<String> patternsList = new ArrayList<>();
        patternsList.add(PAYLOG_PATTERN);
        patternsList.add(REGEX_PATTERN);
        patternBox.getItems().addAll(patternsList);
        patternBox.setValue(PAYLOG_PATTERN);
        searchString.setPromptText("Укажите GUID");
        searchString.setTooltip(new Tooltip("Укажите GUID"));

        chooseFileTooltip.setText("Путь к файлу...");
        filePath.setTooltip(chooseFileTooltip);
        chooseFolderTooltip.setText("Путь к папке...");
        saveDirectoryPath.setTooltip(chooseFolderTooltip);
        chooseFilePathButton.setTooltip(new Tooltip("Выбрать файл"));
        chooseSavePathButton.setTooltip(new Tooltip("Выбрать папку"));
        handlerButton.setTooltip(new Tooltip("Rip the devil out of it!"));
    }

    void alertGenerator (String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
