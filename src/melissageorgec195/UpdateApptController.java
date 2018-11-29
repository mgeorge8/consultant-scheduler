package melissageorgec195;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateApptController implements Initializable {

    @FXML
    private ComboBox<String> startMinBox;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> endMinBox;

    @FXML
    private ComboBox<String> endHoursBox;

    @FXML
    private ComboBox<String> startHoursBox;

    @FXML
    private TextField custNameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button saveButton;

    @FXML
    private ComboBox<String> typeField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;
    
    @FXML 
    private Label headerLabel;
    
    private Appointment appointment;

    @FXML
    void SaveButtonHandler(ActionEvent event) throws IOException {
        String name = custNameField.getText();
        String description = descriptionField.getText();
        String type = typeField.getValue();
        String startDateString = startDatePicker.getValue() + " " + startHoursBox.getValue() + ":" + startMinBox.getValue() + ":00.0";
        String endDateString = endDatePicker.getValue() + " " + endHoursBox.getValue() + ":" + endMinBox.getValue() + ":00.0";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.S");

        LocalDateTime ldtStart = LocalDateTime.parse(startDateString, df);
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdtStart = ldtStart.atZone(zid);
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        ldtStart = utcStart.toLocalDateTime();
        Timestamp startsqlts = Timestamp.valueOf(ldtStart);

        LocalDateTime ldtEnd = LocalDateTime.parse(endDateString, df);
        ZonedDateTime zdtEnd = ldtEnd.atZone(zid);
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
        ldtEnd = utcEnd.toLocalDateTime();
        Timestamp endsqlts = Timestamp.valueOf(ldtEnd);

        ObservableList<Appointment> appointments = DBConnection.getInstance().queryAppointment();
        for( Appointment appointment : appointments) {
            if(appointment.getAppointmentId() == this.appointment.getAppointmentId()) {
                continue;
            }
            ZonedDateTime start = appointment.getStartTime().getValue();
            ZonedDateTime end = appointment.getEndTime().getValue();
            int startAfterStart = zdtStart.compareTo(start);
            int startBeforeEnd = zdtStart.compareTo(end);
            int endAfterStart = zdtEnd.compareTo(start);
            int endBeforeEnd = zdtEnd.compareTo(end);
            try {
                if (startAfterStart > 0 && startBeforeEnd < 0) {
                    throw new Exception("Couldn't insert appointment, overlapping times.");
                }
                if (endAfterStart > 0 && endBeforeEnd < 0) {
                    throw new Exception("Couldn't insert appointment, overlapping times.");
                }
                if (startAfterStart > 0 && endBeforeEnd < 0) {
                    throw new Exception("Couldn't insert appointment, overlapping times");
                }

            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText(ex.getMessage());
                a.setHeaderText(null);
                a.showAndWait();
                return;
            }
        }

        int id = 0;
        try {
            id = DBConnection.getInstance().QueryCustomerId(name);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id != 0) {
            try {
                DBConnection.getInstance().UpdateAppointment(id, description, startsqlts, endsqlts, type, appointment.getAppointmentId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Couldn't update appointment, not a valid customer.");
            a.setHeaderText(null);
            a.showAndWait();
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        CalendarController controller = loader.getController();

        controller.setAppointments(name);

    }

    @FXML
    void CancelButtonHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText("Cancelling appointment add.");
        alert.setContentText("Are you sure you want to cancel?");
        //use of lambda to ensure user wants to cancel add screen
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                try {
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    CalendarController controller = loader.getController();
                    
                    controller.setAppointments(Main.getCustomerName());
                } catch (IOException ex) {
                    System.out.println("Couldn't load Calendar: " + ex.getMessage());
                }
            }
        }));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startHoursBox.getItems().addAll("09", "10", "11", "12", "13", "14", "15", "16");
        endHoursBox.getItems().addAll("09", "10", "11", "12", "13", "14", "15", "16");
        startMinBox.getItems().addAll("00", "10", "20", "30", "40", "50");
        endMinBox.getItems().addAll("00", "10", "20", "30", "40", "50");
        
        typeField.getItems().addAll("Meeting", "Consultation", "Follow-up");

    }
    
    public void setAppointment(Appointment appointment) throws SQLException {        
        this.appointment = appointment;
        custNameField.setText(appointment.getCustName().getValue());
        descriptionField.setText(appointment.getDescription().getValue());
        typeField.setValue(appointment.getType().getValue());
        startDatePicker.setValue(appointment.getStartTime().getValue().toLocalDate());
        String startHour = Integer.toString(appointment.getStartTime().getValue().getHour());
        if (startHour.equals("9")) {
            startHour = "09";
        }
        startHoursBox.setValue(startHour);
        String startMinute = Integer.toString(appointment.getStartTime().getValue().getMinute());
        if( startMinute.equals("0")) {
            startMinute = "00";
        }
        
                    
        startMinBox.setValue(startMinute);
        endDatePicker.setValue(appointment.getEndTime().getValue().toLocalDate());
        String endHour = Integer.toString(appointment.getEndTime().getValue().getHour());
        if (endHour.equals("9")) {
            endHour = "09";
        }
        endHoursBox.setValue(endHour);
        String endMinute = Integer.toString(appointment.getStartTime().getValue().getMinute());
        if( endMinute.equals("0")) {
            endMinute = "00";
        }
        
        endMinBox.setValue(endMinute);
        
        
    } 

}

