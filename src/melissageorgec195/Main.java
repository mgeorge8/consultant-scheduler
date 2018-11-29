/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package melissageorgec195;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Josh
 */
public class Main extends Application {
    public static String customerName;

    public static String getCustomerName() {
        return customerName;
    }

    public static void setCustomerName(String customerName) {
        Main.customerName = customerName;
    }
    
    @Override
    public void start(Stage stage) {
        
        //Uncomment this line to get Spanish! 
       // Locale.setDefault(new Locale("es"));
        ResourceBundle rb = ResourceBundle.getBundle("language.files/rb");
        
        Parent main = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            loader.setResources(rb);
            main = loader.load();
            
            Scene scene = new Scene(main);
            
            stage.setScene(scene);
            
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, Exception {
        
        DBConnection.getInstance().makeConnection();
        launch(args);
        DBConnection.getInstance().closeConnection();
        
    }
    
}
