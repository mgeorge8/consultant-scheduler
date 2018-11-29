package melissageorgec195;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCustomerController implements Initializable {

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

    @FXML
    void SaveButtonHandler(ActionEvent event) throws IOException {
        String name = nameField.getText();
        String address = addressField.getText();
        City city = cityDropdown.getSelectionModel().getSelectedItem();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumberField.getText();
        if (isInputValid()) {
            try {
                ArrayList<Integer> ids = DBConnection.getInstance().insertCustomer(name, address, city.getCityid(), postalCode, phone);
                int addressId = ids.get(0);
                int customerId = ids.get(1);
                if (customerId != 0 && addressId != 0) {
                    Customer customer = new Customer(new ReadOnlyStringWrapper(name),
                            new ReadOnlyStringWrapper(address), new ReadOnlyStringWrapper(city.getCity()),
                            new ReadOnlyStringWrapper(postalCode), new ReadOnlyStringWrapper(phone),
                            customerId, addressId);
                }
            } catch (SQLException e) {
                System.out.println("couldn't insert customer.");
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent loginPageParent = fxmlLoader.load(getClass().getResource("Customer View.fxml"));
            Scene loginPageScene = new Scene(loginPageParent);
            Stage customerScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
            customerScene.setScene(loginPageScene);
            customerScene.show();
        }

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

    public boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name! \n";
        }
        addressField.getText();
        City city = cityDropdown.getSelectionModel().getSelectedItem();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumberField.getText();
        if (addressField.getText() == null || addressField.getText().length() == 0) {
            errorMessage += "No valid address! \n";
        }

        if (cityDropdown.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Must select a city! \n";
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() != 5) {
            errorMessage += "Postal code must be 5 numbers. \n";
        }
        if (phoneNumberField.getText() == null || phoneNumberField.getText().length() != 12) {
            errorMessage += "Phone number must be entered in format of 000-000-0000 \n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid fields.");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
