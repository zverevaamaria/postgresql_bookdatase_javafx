package postg_book.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import postg_book.MainApp;

import java.io.IOException;
import java.sql.SQLException;

public class RootLayoutController {

    @FXML
    private Label tablename;

    @FXML
    private Label dataname;

    public void setDataname(String name) {
        dataname.setText(name);
    }

    private MainApp mainApp;
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }



      @FXML
    private void handleExit() {
        System.exit(0);
    }



}
