package ru.littlebigbro;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    private boolean handlingIsSuccess = false;

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
            handlingIsSuccess = false;
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
    void ripTheDevilOutOfIt(ActionEvent event) throws IOException {
        String userString = searchString.getText().trim();
        switch (patternBox.getValue()) {
            case PAYLOG_PATTERN: {
                handler = new PayLogHandler();
                break;
            }
            case REGEX_PATTERN: {
                handler = new RegExHandler();
                break;
            }
        }
        Map<String,String> params = new HashMap<>();
        params.put("FILE_PATH", filePath.getText());
        params.put("NEW_FILE_PATH", saveDirectoryPath.getText());
        params.put("SEARCH_STRING", userString);

        ProcessingTemplate processingTemplate = new ProcessingTemplate();
        processingTemplate.setHandler(handler);
        processingTemplate.executeHandler(params);

        if (handler.getNewFilePath() != null) {
            saveDirectoryPath.setText(handler.getNewFilePath());
        }
        handlingIsSuccess = handler.getDone();
        if(!handler.getErrorMessage().equals("FALSE") || handlingIsSuccess) {
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
    }

    @FXML
    void initialize() {
        List<String> patternsList = new ArrayList<>();
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
}
