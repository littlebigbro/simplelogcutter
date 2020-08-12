package ru.littlebigbro;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
    private final String PAYLOG_PATTERN = "Шаблон для pay.log с поиском по GUID";
    private final String REGEX_PATTERN = "Регулярное выражение";
    private String userString;
    private String payLogStdPattern;
    private String payLogSearchPattern;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField filePath;

    @FXML
    private TextField saveDirectoryPath;

    @FXML
    private TextField GUID;

    @FXML
    private ComboBox<String> patternBox;

    @FXML
    private Button handlerButton;

    @FXML
    private Button chooseFilePathButton;

    @FXML
    private Button chooseSavePathButton;

    @FXML
    void changePattenAction(ActionEvent event) {
        if (patternBox.getValue().equals(PAYLOG_PATTERN)) {
            payLogStdPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + ".*$";
            payLogSearchPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + userString + ".*$";
        }
        if (patternBox.getValue().equals(REGEX_PATTERN)) {
            //payLogStdPattern вообще не надо заполнять
            payLogStdPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + ".*$";
            payLogSearchPattern = GUID.getText();
        }
    }

    @FXML
    void chooseFilePathAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage PrimaryStage = (Stage) source.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        if (!filePath.getText().isEmpty()) {
            String fileDirectoryPath = filePath.getText().substring(0,filePath.getText().lastIndexOf(File.separator));
            fileChooser.setInitialDirectory(new File(fileDirectoryPath));
        }
        fileChooser.getExtensionFilters().addAll();
        fileChooser.setTitle("Выбор файла");
        File fileObject = fileChooser.showOpenDialog(PrimaryStage);
        if (fileObject != null) {
            filePath.setText(fileObject.getPath());
        }
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
    }

    @FXML
    void handlerActivation(ActionEvent event) {
        userString = GUID.getText();
        if(!filePath.getText().isEmpty() && !userString.isEmpty()) {
            if (patternBox.getValue().equals(PAYLOG_PATTERN)) {
                payLogStdPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + ".*$";
                payLogSearchPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + userString + ".*$";
                File log = new File(filePath.getText());
                File saveLog = new File(saveDirectoryPath.getText());
                Pattern pattern = new Pattern();
                pattern.setHandler(new PayLogHandler(log, saveLog, userString, payLogStdPattern, payLogSearchPattern));
                pattern.executeHandler();
            }
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
    }
}
