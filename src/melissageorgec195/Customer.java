/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package melissageorgec195;

import java.time.LocalDateTime;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Josh
 */
public class Customer {
    private ObservableValue<String> name;
   // private ObservableValue<Integer> id;
    private ObservableValue<String> address;
    private ObservableValue<String> city;
    private ObservableValue<String> postalCode;
    private ObservableValue<String> phone;
    private int customerId;
    private int addressId;
   // private ObservableValue<Integer> active; 
//    private ObservableValue<LocalDateTime> createDate;
//    private ObservableValue<String> createdBy;
//    private ObservableValue<LocalDateTime> lastUpdate;
//    private ObservableValue<String> lastUpdateBy;

    public Customer(ObservableValue<String> name, ObservableValue<String> address, 
            ObservableValue<String> city, ObservableValue<String> postalCode, 
            ObservableValue<String> phone, int customerId, int addressId) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.customerId = customerId;
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public ObservableValue<String> getName() {
        return name;
    }

    public void setName(ObservableValue<String> name) {
        this.name = name;
    }

    public ObservableValue<String> getAddress() {
        return address;
    }

    public void setAddress(ObservableValue<String> address) {
        this.address = address;
    }

    public ObservableValue<String> getCity() {
        return city;
    }

    public void setCity(ObservableValue<String> city) {
        this.city = city;
    }

    public ObservableValue<String> getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(ObservableValue<String> postalCode) {
        this.postalCode = postalCode;
    }

    public ObservableValue<String> getPhone() {
        return phone;
    }

    public void setPhone(ObservableValue<String> phone) {
        this.phone = phone;
    }
    
    

}