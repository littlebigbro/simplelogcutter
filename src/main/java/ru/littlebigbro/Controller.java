package ru.littlebigbro;

import java.io.File;
import java.net.URL;
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
    private ComboBox<?> patternBox;

    @FXML
    private Button handlerButton;

    @FXML
    private Button chooseFilePathButton;

    @FXML
    private Button chooseSavePathButton;

    @FXML
    void changePattenAction(ActionEvent event) {
        payLogStdPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + ".*$";
        payLogSearchPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + userString + ".*$";
    }

    @FXML
    void chooseFilePathAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage PrimaryStage = (Stage) source.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
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
        directoryChooser.setTitle("Выбор папки сохранения");
        File directoryObject = directoryChooser.showDialog(PrimaryStage);
        if (directoryObject != null) {
            saveDirectoryPath.setText(directoryObject.getPath());
        }
    }

    @FXML
    void handlerActivation(ActionEvent event) {
        Handler fileHandler;
        userString = GUID.getText();
        payLogStdPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + ".*$";
        payLogSearchPattern = "^.*" + "Лог перехода" + ".*" + "для документа " + userString + ".*$";

        if(!filePath.getText().isEmpty() && !userString.isEmpty()) {
            File log = new File(filePath.getText());
            File saveLog = new File(saveDirectoryPath.getText());
            fileHandler = new Handler(log, saveLog, userString, payLogStdPattern, payLogSearchPattern);
            fileHandler.startAlgorithm();
            fileHandler = null;
        }
    }

    @FXML
    void userStringFillAction(ActionEvent event) {
    }

    @FXML
    void initialize() {
    }
}
