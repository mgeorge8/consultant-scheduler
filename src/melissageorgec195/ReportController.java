package melissageorgec195;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ReportController {

    @FXML
    private Button cityListButton;

    @FXML
    private TextArea reportTextArea;

    @FXML
    private Button consultSchedButton;

    @FXML
    private Button apptTypeButton;

    @FXML
    void ApptTypeHandler(ActionEvent event) {
        reportTextArea.setText(DBConnection.getInstance().ApptTypeByMonth());
    }

    @FXML
    void ConsultSchedHandler(ActionEvent event) {
        ArrayList<UserSchedule> schedule = DBConnection.getInstance().ScheduleByUser();
        String text;
        if (schedule.isEmpty()) {
            text = "No appointments.";
        } else {
            String name = schedule.get(0).getUserName();
            StringBuilder sb = new StringBuilder("Consultant: " + name);
            sb.append(System.lineSeparator());
            for (UserSchedule entry : schedule) {
                if(!entry.getUserName().equals(name)) {
                    name = entry.getUserName();
                    sb.append("Consultant: ");
                    sb.append(name);
                    sb.append(System.lineSeparator());
                }
                sb.append("\t");
                sb.append("Description: ");
                sb.append(entry.getDescription());
                sb.append("\t");
                sb.append("Start time: ");
                sb.append(entry.getStart());
                sb.append("\t \t");
                sb.append("End time: ");
                sb.append(entry.getEnd());
                sb.append(System.lineSeparator());               
            }
            text = sb.toString();
        }

        reportTextArea.setText(text);

    }

    @FXML
    void CityListHandler(ActionEvent event) {
        StringBuilder sb = new StringBuilder("City List");
        sb.append(System.lineSeparator());
        ObservableList<City> cities = DBConnection.getInstance().listCities();
        for (City city : cities) {
            sb.append(city.getCity());
            sb.append(System.lineSeparator());
        }
        reportTextArea.setText(sb.toString());
    }

}
