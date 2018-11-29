package melissageorgec195;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCustomerController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private ComboBox<City> cityDropdown;

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField addressField;
    
    private Customer customer;

    @FXML
    void SaveButtonHandler(ActionEvent event) throws SQLException, IOException {
        String name = nameField.getText();
        String address = addressField.getText();
        City city = cityDropdown.getSelectionModel().getSelectedItem();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumberField.getText();
        int customerId = customer.getCustomerId();
        int addressId = customer.getAddressId();
        DBConnection.getInstance().UpdateCustomer(name, customerId, address, city.getCityid(), postalCode, phone, addressId);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent loginPageParent = fxmlLoader.load(getClass().getResource("Customer View.fxml"));
        Scene loginPageScene = new Scene(loginPageParent);
        Stage customerScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
        customerScene.setScene(loginPageScene);
        customerScene.show();

    }

    @FXML
    void CancelButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent loginPageParent = fxmlLoader.load(getClass().getResource("Customer View.fxml"));
        Scene loginPageScene = new Scene(loginPageParent);
        Stage customerScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
        customerScene.setScene(loginPageScene);
        customerScene.show();
    }

    @FXML
    void CityDropdownHandler(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cityDropdown.getItems().addAll(DBConnection.getInstance().listCities());
    }
    
    public void setCustomer(Customer customer) throws SQLException {
        this.customer = customer;
        nameField.setText(customer.getName().getValue());
        addressField.setText(customer.getAddress().getValue());
        cityDropdown.getSelectionModel().select(DBConnection.getInstance().FindCity(customer.getCity().getValue()));
        postalCodeField.setText(customer.getPostalCode().getValue());
        phoneNumberField.setText(customer.getPhone().getValue());
        
    } 
    
}