package ru.littlebigbro;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
    private final String PAYLOG_PATTERN = "Обработка по GUID";
    private final String REGEX_PATTERN = "Обработка по регулярному выражению";
    private final String ERROR = "Ошибка";
    private final String DONE = "Готово!";
    private String alertMessage;
    private Handler handler = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField filePath;

    @FXML
    private Tooltip tooltip_FilePath;

    @FXML
    private Button chooseFilePathButton;

    @FXML
    private Tooltip tooltip_ChooseFileButton;

    @FXML
    private TextField saveDirectoryPath;

    @FXML
    private Tooltip tooltip_SaveDirectoryPath;

    @FXML
    private Button chooseSaveDirectoryButton;

    @FXML
    private Tooltip tooltip_ChooseSaveDirectoryButton;

    @FXML
    private ComboBox<String> patternBox;

    @FXML
    private TextField searchString;

    @FXML
    private Tooltip tooltip_SearchString;

    @FXML
    private Button handlerButton;

    @FXML
    private Tooltip tooltip_RipTheDevilOutOfIt;


    @FXML
    void changePattenAction(ActionEvent event) {
        switch (patternBox.getValue()) {
            case PAYLOG_PATTERN : {
                searchString.setPromptText("Укажите GUID");
                tooltip_SearchString.setText("Укажите GUID");
                break;
            }

            case REGEX_PATTERN : {
                searchString.setPromptText("Укажите регулярное выражение");
                tooltip_SearchString.setText("Укажите регулярное выражение");
                break;
            }
        }
        searchString.setTooltip(tooltip_SearchString);
    }

    @FXML
    void chooseFilePathAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        if (!filePath.getText().isEmpty()) {
            String fileDirectoryPath = filePath.getText().substring(0, filePath.getText().lastIndexOf(File.separator));
            fileChooser.setInitialDirectory(new File(fileDirectoryPath));
        }
        fileChooser.getExtensionFilters().addAll();
        fileChooser.setTitle("Выбор файла");
        File fileObject = fileChooser.showOpenDialog(primaryStage);
        if (fileObject != null) {
            filePath.setText(fileObject.getPath());
        }
        tooltip_FilePath.setText(filePath.getText());
    }

    @FXML
    void chooseSaveDirectoryAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (!saveDirectoryPath.getText().isEmpty()) {
            directoryChooser.setInitialDirectory(new File(saveDirectoryPath.getText()));
        }
        directoryChooser.setTitle("Выбор папки для сохранения");
        File directoryObject = directoryChooser.showDialog(primaryStage);
        if (directoryObject != null) {
            saveDirectoryPath.setText(directoryObject.getPath());
        }
        tooltip_SaveDirectoryPath.setText(saveDirectoryPath.getText());
    }

    @FXML
    void ripTheDevilOutOfIt(ActionEvent event) {
        String userString = searchString.getText().trim();
        if (!userString.isEmpty()){
            if(!filePath.getText().isEmpty() && userStringMatcher(userString)) {
                if (saveDirectoryPath.getText().isEmpty()) {
                    saveDirectoryPath.setText(filePath.getText().substring(0, filePath.getText().lastIndexOf(File.separator)));
                }
                File log = new File(filePath.getText());
                File saveLog = new File(saveDirectoryPath.getText());
                ProcessingTemplate processingTemplate = new ProcessingTemplate();
                switch (patternBox.getValue()) {
                    case PAYLOG_PATTERN: {
                        handler = new PayLogHandler(log, saveLog, userString);
                        processingTemplate.setHandler(handler);
                        processingTemplate.executeHandler();
                        break;
                    }

                    case REGEX_PATTERN: {
                        handler = new RegExHandler(log, saveLog, userString);
                        processingTemplate.setHandler(handler);
                        processingTemplate.executeHandler();
                        break;
                    }
                }
                if(!handler.getErrorMessage().equals("FALSE") || handler.getDone()) {
                    String alertType = ERROR;
                    if (handler.getDone()){
                        alertType = DONE;
                        alertMessage = "Обработка завершена!!! \nФайл сохранён по пути: " + handler.getNewFilePath();
                    } else {
                        alertMessage = handler.getErrorMessage();
                    }
                    alertGenerator(alertType, alertMessage);
                }
                handler = null;
            } else {
                if (filePath.getText().isEmpty()){
                    alertMessage = "Не указан путь к обрабатываемому файлу";
                    alertGenerator(ERROR, alertMessage);
                }
            }
        } else {
            if (!filePath.getText().isEmpty()) {
                alertMessage = "Поисковой запрос пуст";
                alertGenerator(ERROR, alertMessage);
            }
        }
    }

    @FXML
    void initialize() {
        List<String> patternsList = new ArrayList<String>();
        patternsList.add(PAYLOG_PATTERN);
        patternsList.add(REGEX_PATTERN);
        patternBox.getItems().addAll(patternsList);
        patternBox.setValue(PAYLOG_PATTERN);
        searchString.setPromptText("Укажите GUID");
        setTooltips();

    }

    private void setTooltips() {
        tooltip_FilePath.setText("Путь к файлу..");
        filePath.setTooltip(tooltip_FilePath);

        tooltip_ChooseFileButton.setText("Выбрать файл");
        chooseFilePathButton.setTooltip(tooltip_ChooseFileButton);

        tooltip_SaveDirectoryPath.setText("Путь к папке..");
        saveDirectoryPath.setTooltip(tooltip_SaveDirectoryPath);

        tooltip_ChooseSaveDirectoryButton.setText("Выбрать папку");
        chooseSaveDirectoryButton.setTooltip(tooltip_ChooseSaveDirectoryButton);

        tooltip_SearchString.setText("Укажие GUID");
        searchString.setTooltip(tooltip_SearchString);

        tooltip_RipTheDevilOutOfIt.setText("Rip the devil out of it!");
        handlerButton.setTooltip(tooltip_RipTheDevilOutOfIt);
    }

    private void alertGenerator (String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(null);
        alert.showAndWait();
        alert.close();
    }

    private boolean userStringMatcher(String userString) {
        boolean userStringMatch = false;
        switch (patternBox.getValue()) {
            case PAYLOG_PATTERN : {
                if (userString.matches("[0-9a-zA-Z]{8}[-][0-9a-zA-Z]{4}[-][0-9a-zA-Z]{4}[-][0-9a-zA-Z]{4}[-][0-9a-zA-Z]{12}")) {
                    userStringMatch = true;
                } else {
                    userStringMatch = false;
                    alertMessage = "Указанный GUID имеет неверный формат";
                    alertGenerator(ERROR, alertMessage);
                }
                break;
            }
            case REGEX_PATTERN : {
                try {
                    Pattern.compile(userString);
                    userStringMatch = true;
                }
                catch (PatternSyntaxException e) {
                    userStringMatch = false;
                    alertMessage = "Указанное регулярное выражение имеет неверный формат";
                    alertGenerator(ERROR, alertMessage);
                }
                break;
            }
        }
        return userStringMatch;
    }
}
