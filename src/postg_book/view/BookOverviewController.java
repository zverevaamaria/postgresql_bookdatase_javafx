package postg_book.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.postgresql.util.PSQLException;
import postg_book.MainApp;
import postg_book.model.aBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class BookOverviewController {

    private boolean okClicked = false;

    @FXML
    private TableView<aBook> personTable;

    @FXML
    private TableColumn<aBook, Integer> idColumn;

    @FXML
    private TableColumn<aBook, String> TittleColumn;

    @FXML
    private TableColumn<aBook, String> AuthorColumn;

    @FXML
    private TableColumn<aBook, Integer> YearColumn;

    @FXML
    private TextField newid;

    @FXML
    private TextField newtittle;

    @FXML
    private TextField newautor;

    @FXML
    private TextField newyear;

    @FXML
    private TextField editid;

    @FXML
    private TextField edittit;

    @FXML
    private TextField editaut;

    @FXML
    private TextField edityear;

    @FXML
    private TextField searchfield;

    @FXML
    private TextField tablename;

    @FXML
    private TextField deletebook;

    @FXML
    private Label currenttable;

    @FXML
    private TextField namedb;

    @FXML
    private Label currentdb;

    int table_counter = 0;
    private ObservableList<aBook> bookdata = FXCollections.observableArrayList();
    MainApp mainApp;

    private void initialize()
    {
        idColumn.setCellValueFactory(new PropertyValueFactory<aBook, Integer>("id"));
        TittleColumn.setCellValueFactory(new PropertyValueFactory<aBook, String>("tittle"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<aBook, String>("author"));
        YearColumn.setCellValueFactory(new PropertyValueFactory<aBook, Integer>("year"));

        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBookDetails(newValue));



    }

    private void todisplay(aBook abook){
        bookdata.add(abook);
        personTable.setItems(bookdata);

    }

    public void showBookDetails(aBook somebook)
    {
        if (somebook != null) {
            editid.setText(Integer.toString(somebook.getId()));
            edittit.setText(somebook.getTittle());
            editaut.setText(somebook.getAuthor());
            edityear.setText(Integer.toString(somebook.getYear()));
        } else {
            editid.setText("1");
            edittit.setText("");
            editaut.setText("");
            edityear.setText("");
        }

    }

    @FXML
    void clearTable(ActionEvent event) throws ClassNotFoundException, SQLException {
        Connection conn2 = null;
        String url2 = "jdbc:postgresql://127.0.0.1:5432/" + currentdb.getText(),
                userName2 = "postgres", userPassd2 = "1";
        Class.forName("org.postgresql.Driver");
        conn2 = DriverManager.getConnection(url2, userName2, userPassd2);
        Statement stat2 = conn2.createStatement();

        //создание таюлицы
        stat2.executeQuery("SELECT * FROM clear_table('" + currenttable.getText() + "');\n");

    }

    @FXML
    void createTable(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(table_counter == 0) {
            table_counter += 1;
            String nameoftable = tablename.getText();
            currenttable.setText(nameoftable);
            Connection conn2 = null;
            String url2 = "jdbc:postgresql://127.0.0.1:5432/" +  currentdb.getText(),
                    userName2 = "postgres", userPassd2 = "1";
            Class.forName("org.postgresql.Driver");
            conn2 = DriverManager.getConnection(url2, userName2, userPassd2);
            Statement stat2 = conn2.createStatement();

            //создание таюлицы
            stat2.executeQuery("SELECT * FROM create_table_type1('" + nameoftable + "');\n");

            //ввод данных
            stat2.executeUpdate("CREATE OR REPLACE FUNCTION insert_table (idn int, tit text,aut text, yyear int)\n" +
                    "RETURNS VOID AS $$\n" +
                    "BEGIN\n" +
                    "EXECUTE format('INSERT INTO " + nameoftable + "(id, tittle, autor, yearr) VALUES('|| idn || ', ''' ||  tit || ''','''  || aut || ''','|| yyear ||\n" +
                    "')');\n" +
                    "END;\n" +
                    "$$LANGUAGE plpgsql;");

            //обновление кортежа
            stat2.executeUpdate("CREATE OR REPLACE FUNCTION update_table(oldid int, idn int, tit text,aut text, yyear int)\n" +
                    "RETURNS VOID AS $$\n" +
                    "BEGIN\n" +
                    "EXECUTE format('UPDATE " + nameoftable + " SET  id ='|| idn || ', tittle =''' || tit || ''',\n" +
                    " autor =''' || aut || ''', yearr = ' || yyear || ' WHERE id =' || oldid );\n" +
                    "END;\n" +
                    "$$LANGUAGE plpgsql;");

            //Очистка таблицы
            stat2.executeUpdate("CREATE OR REPLACE FUNCTION clear_table(tablename varchar)\n" +
                    "RETURNS VOID AS $$\n" +
                    "BEGIN\n" +
                    "EXECUTE format('TRUNCATE ' || tablename);\n" +
                    "END;\n" +
                    "$$LANGUAGE plpgsql;");

            //Удаление по одному текстовому полю
            stat2.executeUpdate("CREATE OR REPLACE FUNCTION delete_record(aut text)\n" +
                    "RETURNS VOID AS $$\n" +
                    "BEGIN\n" +
                    "EXECUTE format('DELETE FROM " + nameoftable + " WHERE autor= ''' || aut || ''';'  );\n" +
                    "END;\n" +
                    "$$LANGUAGE plpgsql;");

            //Поиск по одному текстовому  полю
            stat2.executeUpdate("CREATE OR REPLACE FUNCTION FIND_record( tit text )\n" +
                    "RETURNS TABLE(id int, tittle text, autor text, yearr int) AS $$\n" +
                    "BEGIN\n" +
                    "return query SELECT *  FROM " + nameoftable + " WHERE table1.tittle = tit; \n" +
                    "IF NOT FOUND THEN\n" +
                    "    RAISE EXCEPTION 'книга не найдена';\n" +
                    "END IF;\n" +
                    "END;\n" +
                    "$$LANGUAGE plpgsql;");
            stat2.executeQuery("SELECT * FROM insert_table(3, 'root', 'smile', 1234)");
            conn2.close();
        }
        else
            {
                System.out.println("Таблица уже создана!");
            }
    }

    @FXML
    void createdb(ActionEvent event) throws SQLException, ClassNotFoundException {

        String nameofdb = namedb.getText();
        currentdb.setText(nameofdb);
        Connection conn1 = null;
        String url = "jdbc:postgresql://127.0.0.1:5432/books_db",
                userName = "postgres", userPassd = "1";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }
        conn1 = DriverManager.getConnection(url, userName, userPassd);
        System.out.println("Соединение установлено");
        Statement stat1 = conn1.createStatement();
        try {
            stat1.executeUpdate("create database " + nameofdb);
        }
        catch (PSQLException n)
        {
            System.out.println("база данных " + nameofdb + " уже существует");
        }
        conn1.close();
        //переподключаемся к новой дб и создаем функции
        Connection conn2 = null;
        String url2 = "jdbc:postgresql://127.0.0.1:5432/" + nameofdb ,
                userName2 = "postgres", userPassd2 = "1";
        Class.forName("org.postgresql.Driver");
        conn2 = DriverManager.getConnection(url2, userName2, userPassd2);
        Statement stat2 = conn2.createStatement();

        //создание таюлицы
        stat2.executeUpdate("CREATE OR REPLACE FUNCTION create_table_type1(t_name varchar(30))\n" +
                "  RETURNS VOID AS\n" +
                "$func$\n" +
                "BEGIN\n" +
                "\n" +
                "EXECUTE format('\n" +
                "   CREATE TABLE IF NOT EXISTS %I (\n" +
                "    id int PRIMARY KEY,\n" +
                "    tittle text,\n" +
                "    autor text,\n" +
                "    yearr int\n" +
                "   )', '' || t_name);\n" +
                "\n" +
                "END\n" +
                "$func$ LANGUAGE plpgsql;");
        okClicked = true;
        conn2.close();
    }

    @FXML
    void editBook(ActionEvent event) {


    }

    @FXML
    void handleDeleteBook(ActionEvent event) throws ClassNotFoundException, SQLException {
        String autordelete = deletebook.getText();
        Connection conn2 = null;
        String url2 = "jdbc:postgresql://127.0.0.1:5432/" +  currentdb.getText(),
                userName2 = "postgres", userPassd2 = "1";
        Class.forName("org.postgresql.Driver");
        conn2 = DriverManager.getConnection(url2, userName2, userPassd2);
        Statement stat2 = conn2.createStatement();

        //создание таюлицы
        stat2.executeQuery("SELECT * FROM delete_record('" + autordelete + "');\n");



    }

    @FXML
    void newBook(ActionEvent event) throws ClassNotFoundException, SQLException {
        int id = Integer.parseInt(newid.getText());
        String tittle = newtittle.getText();
        String autor = newautor.getText();
        int year = Integer.parseInt(newyear.getText());
        aBook abook = new aBook(id, tittle, autor, year);

        todisplay(abook);

        Connection conn2 = null;
        String url2 = "jdbc:postgresql://127.0.0.1:5432/" +  currentdb.getText(),
                userName2 = "postgres", userPassd2 = "1";
        Class.forName("org.postgresql.Driver");
        conn2 = DriverManager.getConnection(url2, userName2, userPassd2);
        Statement stat2 = conn2.createStatement();

        //ввод данных в фию
        stat2.executeQuery("SELECT * FROM insert_table("+id+ ", '"+ tittle+"', '"+autor+"', "+ year +");\n");

    }

    @FXML
    void searching(ActionEvent event) {
        String searc_tittle = searchfield.getText();



    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}

