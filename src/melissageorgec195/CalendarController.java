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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CalendarController {

    @FXML
    private TableView<Appointment> apptTable;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> startTimeColumn;

    @FXML
    private Button addApptButton;
    
    @FXML
    private Button updateApptButton;
    
    @FXML
    private Button deleteApptButton;
    
    @FXML
    private TableColumn<Appointment, String> custNameColumn;

    @FXML
    private DatePicker weekDatePicker;

    @FXML
    private TableColumn<Appointment, String> typeColumn;
    
    @FXML
    private DatePicker monthDatePicker;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> endTimeColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();


    @FXML
    void WeekSelectionHandler(ActionEvent event) {
        LocalDate week = weekDatePicker.getValue();
        LocalDate weekPlus7 = week.plusDays(7);
        FilteredList<Appointment> filteredData = new FilteredList<Appointment>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = row.getStartTime().getValue().toLocalDate();

            return rowDate.isAfter(week) && rowDate.isBefore(weekPlus7);
        });
        apptTable.setItems(filteredData);
    }

    @FXML
    void MonthSelectionHandler(ActionEvent event) {
        LocalDate month = monthDatePicker.getValue();
        LocalDate firstOfMonth = month.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = month.with(TemporalAdjusters.lastDayOfMonth());
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDate rowDate = row.getStartTime().getValue().toLocalDate();

            return rowDate.isAfter(firstOfMonth) && rowDate.isBefore(lastDayOfMonth);
        });
        apptTable.setItems(filteredData);
    }

    @FXML
    void AddApptHandler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent calendarPageParent = fxmlLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene calendarPageScene = new Scene(calendarPageParent);
        Stage calendarScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarScene.setScene(calendarPageScene);
        calendarScene.show();     
    }
    
    @FXML
    void UpdateApptHandler(ActionEvent event) throws IOException, SQLException {
        Stage stage = (Stage) updateApptButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAppt.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        UpdateApptController controller = loader.getController();
        Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
        controller.setAppointment(appointment);    
    }
    
    @FXML
    void DeleteApptHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText("Delete appointment.");
        alert.setContentText("Are you sure you want to delete?");
        //use of lambda to ensure user wants to delete appointment before deleting, lambda is useful to check for the OK button to be clicked
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                try {
                    Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
                    int appointmentId = appointment.getAppointmentId();
                    DBConnection.getInstance().DeleteAppointment(appointmentId);
                    appointmentList.remove(appointment);
                    apptTable.setItems(appointmentList);
                } catch (SQLException ex) {
                    System.out.println("Couldn't delete appointment: " + ex.getMessage());
                }
            }
        }));
    }

    public void setAppointments(String customerName) {

        custNameColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustName();
        });
        descriptionColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getDescription();
        });
        typeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getType();
        });
        startTimeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getStartTime();
        });
        endTimeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getEndTime();
        });
        ObservableList<Appointment> appointments;
        appointments = DBConnection.getInstance().queryAppointment();
        appointments.stream().filter((appointment) -> (appointment.getCustName().getValue().equals(customerName))).forEachOrdered((appointment) -> {
            appointmentList.add(appointment);
        });
        apptTable.setItems(appointmentList);
        apptTable.getSelectionModel().selectFirst();

    }
    
    @FXML 
    void ReportButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent calendarPageParent = fxmlLoader.load(getClass().getResource("Report.fxml"));
        Scene calendarPageScene = new Scene(calendarPageParent);
        Stage calendarScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarScene.setScene(calendarPageScene);
        calendarScene.show();
    }

}
