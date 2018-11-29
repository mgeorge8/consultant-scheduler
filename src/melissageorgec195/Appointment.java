/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package melissageorgec195;

import java.time.ZonedDateTime;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Josh
 */
public class Appointment {
    private ObservableValue<String> custName;
    private ObservableValue<String> description;
    private ObservableValue<String> type;
    private ObservableValue<ZonedDateTime> startTime;
    private ObservableValue<ZonedDateTime> endTime;
    private ObservableValue<Integer> userId;
    private int appointmentId;

    public Appointment(ObservableValue<String> custName, ObservableValue<String> description, ObservableValue<String> type, ObservableValue<ZonedDateTime> startTime, ObservableValue<ZonedDateTime> endTime, ObservableValue<Integer> userId, int appointmentId) {
        this.custName = custName;
        this.description = description;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.appointmentId = appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public ObservableValue<Integer> getUserId() {
        return userId;
    }

    public void setUserId(ObservableValue<Integer> userId) {
        this.userId = userId;
    }

    public ObservableValue<String> getCustName() {
        return custName;
    }

    public void setCustName(ObservableValue<String> custName) {
        this.custName = custName;
    }

    public ObservableValue<String> getDescription() {
        return description;
    }

    public void setDescription(ObservableValue<String> description) {
        this.description = description;
    }

    public ObservableValue<String> getType() {
        return type;
    }

    public void setType(ObservableValue<String> type) {
        this.type = type;
    }

    public ObservableValue<ZonedDateTime> getStartTime() {
        return startTime;
    }

    public void setStartTime(ObservableValue<ZonedDateTime> startTime) {
        this.startTime = startTime;
    }

    public ObservableValue<ZonedDateTime> getEndTime() {
        return endTime;
    }

    public void setEndTime(ObservableValue<ZonedDateTime> endTime) {
        this.endTime = endTime;
    }
    
    
}
