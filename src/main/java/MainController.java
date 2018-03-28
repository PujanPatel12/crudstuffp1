import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.*;
import java.util.List;


public class MainController {
    @FXML
    private TextField nametextfield;
    @FXML
    private TextField emailtextfield;
    @FXML
    private ChoiceBox statuschoicebox;
    @FXML
    private TableColumn<Person, Integer> personidcol;
    @FXML
    private TableColumn<Person, String> personnamecol;
    @FXML
    private TableColumn<Person, String> personemailcol;
    @FXML
    private TableColumn<String,Person> personstatuscol;
    @FXML
    private TableView<Person> persontable;

    public void initialize()throws SQLException{
        BasicDataSource bs = new BasicDataSource();
        bs.setUsername("postgres");
        bs.setPassword("postgres");
        bs.setUrl("jdbc:postgresql://localhost:5432/aitp");
        personidcol.setCellValueFactory(new PropertyValueFactory<Person,Integer>("personid"));
        personnamecol.setCellValueFactory(new PropertyValueFactory<Person,String>("personname"));
        personemailcol.setCellValueFactory(new PropertyValueFactory<Person,String>("personemail"));
        personstatuscol.setCellValueFactory(new PropertyValueFactory<String, Person>("statusdesc"));
        QueryRunner queryRunner = new QueryRunner(bs);
        ResultSetHandler<List<Person>> P = new BeanListHandler<Person>(Person.class);
        String sql = "select person.personid,person.personname,person.personemail,person.statusid, person_status.statusdesc from person INNER JOIN person_status  ON person.statusid = person_status.statusid ORDER BY person.personid";
        List<Person> people = queryRunner.query(sql,P);
        persontable.setItems(FXCollections.observableArrayList(people));
        //initialize choice box
        Connection connection = bs.getConnection();

        Statement statement = connection.createStatement();
        String sql1 = "select * from person_status;";
        ResultSet resultSet = statement.executeQuery(sql1);
        String statusname;
        while(resultSet.next()){
            statusname = resultSet.getString("statusdesc");
            statuschoicebox.getItems().addAll(statusname);
        }
        resultSet.close();
        connection.close();
    }

    public void deletebuttonpressed(MouseEvent mouseEvent) throws SQLException{
        //gets ID of the person
        //
      int personid = persontable.getSelectionModel().getSelectedIndex()+1;
       String sql = "update person set statusid ='2' WHERE personid="+personid;
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("postgres");
        basicDataSource.setUrl("jdbc:postgresql://localhost:5432/aitp");
        Connection connection = basicDataSource.getConnection();
        try(PreparedStatement p =connection.prepareStatement(sql)){
            p.execute();
            p.close();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully deleted Person");
        alert.setHeaderText("Changed Status of Person");
        alert.setContentText("Successfully deleted Person");
        alert.showAndWait();

        initialize();

        connection.close();
    }
    public void mousebuttonclicked(MouseEvent mouseEvent) throws SQLException {
        BasicDataSource bs = new BasicDataSource();
        bs.setUsername("postgres");
        bs.setPassword("postgres");
        bs.setUrl("jdbc:postgresql://localhost:5432/aitp");
        //these 3 lines aren't required just makes code more cleaner
        String namevalue = nametextfield.getText();
        String emailvalue = emailtextfield.getText();
        int statusid = statuschoicebox.getSelectionModel().getSelectedIndex()+1;
        Connection connection = bs.getConnection();
        String sql ="INSERT INTO person(personname, personemail, statusid) VALUES (?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
           preparedStatement.setString(1,namevalue);
           preparedStatement.setString(2,emailvalue);
           preparedStatement.setInt(3,statusid);
           preparedStatement.execute();
           preparedStatement.close();
        }
        //alert boxes
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully added Person");
        alert.setHeaderText("Added Person to Database");
        alert.setContentText("Successfully added Person");
        alert.showAndWait();
        //this make it so whenever you add a value to db the list automatically refreshes
        initialize();
    }
}
