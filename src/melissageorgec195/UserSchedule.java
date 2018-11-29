/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package melissageorgec195;

import java.time.LocalDateTime;

/**
 *
 * @author Josh
 */
public class UserSchedule {
    private String userName;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;

    public UserSchedule(String userName, String description, LocalDateTime start, LocalDateTime end) {
        this.userName = userName;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    
    
}
