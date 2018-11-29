package melissageorgec195;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CustomerViewController implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private Button appointButton;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private Button editButton;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> cityColumn;

    @FXML
    private TableColumn<Customer, String> postalCodeColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    void AddButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent loginPageParent = fxmlLoader.load(getClass().getResource("AddCustomer.fxml"));
        Scene loginPageScene = new Scene(loginPageParent);
        Stage customerScene = (Stage) ((Node) event.getSource()).getScene().getWindow();
        customerScene.setScene(loginPageScene);
        customerScene.show();
    }

    @FXML
    void EditButtonHandler(ActionEvent event) throws IOException, SQLException {
        Stage stage = (Stage) editButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        EditCustomerController controller = loader.getController();
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        controller.setCustomer(customer);
    }

    @FXML
    void DeleteButtonHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText("Delete customer.");
        alert.setContentText("Are you sure you want to delete?");
        //use of lambda to ensure user wants to delete customer before deleting
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                try {
                    Customer customer = customerTable.getSelectionModel().getSelectedItem();
                    int customerId = customer.getCustomerId();
                    int addressId = customer.getAddressId();
                    DBConnection.getInstance().DeleteCustomer(customerId, addressId);
                    customerList.remove(customer);
                    customerTable.setItems(customerList);
                } catch (SQLException ex) {
                    System.out.println("Couldn't delete customer: " + ex.getMessage());
                }
            }
        }));

    }

    @FXML
    void AppointmentButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) appointButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        CalendarController controller = loader.getController();
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        String customerName = customer.getName().getValue();
        Main.setCustomerName(customerName);
        System.out.println("cust : " + Main.getCustomerName());
        controller.setAppointments(customerName);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nameColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getName();
        });
        addressColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getAddress();
        });
        cityColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCity();
        });
        postalCodeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getPostalCode();
        });
        phoneColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getPhone();
        });

        // ResultSet rs = accessDB();
        try {
            Connection conn = DBConnection.getInstance().makeConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT customer.customerName, address.address, city.city, "
                    + "address.postalCode, address.phone, customer.customerid, customer.addressId "
                    + "from customer INNER JOIN address ON customer.addressId = address.addressid "
                    + "INNER JOIN city ON address.cityId = city.cityid;");
            try {
                while (rs.next()) {
                    String name = rs.getString("customerName");
                    String address = rs.getString("address");
                    String city = rs.getString("city");
                    String postalCode = rs.getString("postalCode");
                    String phone = rs.getString("phone");
                    int customerId = rs.getInt("customerid");
                    int addressId = rs.getInt("addressId");
                    Customer customer = new Customer(new ReadOnlyStringWrapper(name),
                            new ReadOnlyStringWrapper(address), new ReadOnlyStringWrapper(city),
                            new ReadOnlyStringWrapper(postalCode), new ReadOnlyStringWrapper(phone),
                            customerId, addressId);
                    customerList.add(customer);
                }
                customerTable.setItems(customerList);
                customerTable.getSelectionModel().selectFirst();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
