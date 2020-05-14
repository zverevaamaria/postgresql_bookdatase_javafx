package postg_book.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import postg_book.MainApp;
import postg_book.model.aBook;


public class ShowResultController {
    @FXML
    private TableView<aBook> personTable1;
    @FXML
    private TableColumn<aBook, Integer> IDcolumn;

    @FXML
    private TableColumn<aBook, String> TitleColumn;

    @FXML
    private TableColumn<aBook, String> AuthorColumn;

    @FXML
    private TableColumn<aBook, Integer> DateColumn;



    private Stage dialogStage2;
    boolean okClicked = false;
    private ObservableList<aBook> toshow = FXCollections.observableArrayList();
    MainApp mainapp ;

    public void setDialogStage(Stage dialogStage2) {
        this.dialogStage2 = dialogStage2;
    }


    @FXML
    private void initialize() {
        IDcolumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("tittle"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("year"));


    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;
    }
}


