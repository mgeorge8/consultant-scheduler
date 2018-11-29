/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package melissageorgec195;

import javafx.beans.value.ObservableValue;

/**
 *
 * @author Josh
 */
public class City {
    private int cityid;
    private String city;
   // private ObservableValue<Integer> countryId;

    public City(int cityid, String city) {
        this.cityid = cityid;
        this.city = city;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    
    
    

//    public ObservableValue<Integer> getCountryId() {
//        return countryId;
//    }
//
//    public void setCountryId(ObservableValue<Integer> countryId) {
//        this.countryId = countryId;
//    }

    @Override
    public String toString() {
        return "" + city;
    }
    
    
}
