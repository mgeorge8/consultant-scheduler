/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package melissageorgec195;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordField;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label loginLabel;

    @FXML
    private Label passwordLabel;

    ResourceBundle rb;

    @FXML
    public void handleLoginAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            Logger log = Logger.getLogger("log.txt");
            
            FileHandler fh = new FileHandler("log.txt", true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            log.addHandler(fh);
            
            log.log(Level.SEVERE, "User with username: {0} and password: {1} attempted login at {2}", new Object[]{username, password, LocalDateTime.now()});
            int id = DBConnection.getInstance().queryForUserLogin(username, password);
            ObservableList<Appointment> appointments = DBConnection.getInstance().queryAppointment();
            System.out.println(appointments.get(1).getStartTime());
            FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
            //use of lambda to filter the list instead of looping through every appointment to gain efficiency
            filteredData.setPredicate(row -> {

                ZonedDateTime rowDate = row.getStartTime().getValue();

                return rowDate.isAfter(ZonedDateTime.now()) && rowDate.isBefore(ZonedDateTime.now().plusMinutes(15));

            });
            DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("kk:mm:ss.S");
            ObservableList<Appointment> newAppointments = filteredData;
            for (Appointment appointment : newAppointments) {
                if (appointment.getUserId().getValue() == id) {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setContentText("You have an appointment with " + appointment.getCustName().getValue() + " at " + appointment.getStartTime().getValue().format(tFormatter));
                    a.setHeaderText("Upcoming appointment!");
                    a.showAndWait();
                }
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent loginPageParent = fxmlLoader.load(getClass().getResource("Customer View.fxml"));
            Scene loginPageScene = new Scene(loginPageParent);
            Stage customerScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
            customerScene.setScene(loginPageScene);
            customerScene.show();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText(rb.getString("alert"));
            a.setHeaderText(null);
            a.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;

        usernameLabel.setText(rb.getString("userLabel"));
        passwordLabel.setText(rb.getString("passLabel"));
        loginButton.setText(rb.getString("button"));
        loginLabel.setText(rb.getString("prompt"));
    }

}
