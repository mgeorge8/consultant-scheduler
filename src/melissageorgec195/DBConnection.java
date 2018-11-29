/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package melissageorgec195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Josh
 */
public class DBConnection {

    private static final String databaseName = "U05fyK";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/U05fyK";
    private static final String username = "U05fyK";
    private static final String password = "53688491742";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    static Connection conn;

    public static final String QUERY_USER = "SELECT * FROM user WHERE username = ? AND password = ?";
    public static final String INSERT_ADDRESS = "INSERT INTO address\n"
            + "	(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES\n"
            + "    (?,'',?,?,?,CURDATE(),'admin','admin');";
    public static final String INSERT_CUSTOMER = "INSERT INTO customer\n"
            + "	(customerName,addressId,active,createDate,createdBy,lastUpdateBy) VALUES\n"
            + "    (?,?,1,CURDATE(),'admin','admin');";
    public static final String QUERY_CITY = "SELECT * FROM city WHERE city = ?";
    public static final String UPDATE_CUSTOMER = "UPDATE customer SET customerName = ? WHERE customerid = ?";
    public static final String UPDATE_ADDRESS = "UPDATE address SET address = ?, cityId = ?, postalCode = ?, phone = ? WHERE addressid = ?";
    public static final String DELETE_ADDRESS = "DELETE FROM address WHERE addressid = ?";
    public static final String DELETE_CUSTOMER = "DELETE FROM customer WHERE customerid = ?";
    public static final String QUERY_CUSTOMER_ID = "SELECT customerid FROM customer WHERE customerName = ?";
    public static final String INSERT_APPOINTMENT = "INSERT INTO appointment (customerId,title,description,location,"
            + "contact,url,start,end,createDate,createdBy,lastUpdateBy, type, userId) VALUES "
            + "(?,'Meeting',?,'New York, New York','wflick','',?,?,CURDATE(),'admin','admin',?,'1');";
    public static final String UPDATE_APPOINTMENT = "UPDATE appointment SET customerId = ?, description = ?, start = ?, end = ?, type = ? WHERE appointmentid = ?";
    public static final String QUERY_APPOINTMENTS = "SELECT customer.customerName, appointment.description, "
            + "appointment.type, appointment.start, appointment.end, appointment.userId, appointment.appointmentid from customer "
            + "INNER JOIN appointment ON customer.customerid = appointment.customerId;";
    public static final String APPT_TYPES_BY_MONTH = "SELECT MONTH(start) AS month, SUM(type=\"Meeting\") as Meeting, SUM(type=\"Consultation\") as Consultation, SUM(type='Follow-up') as FollowUp FROM appointment GROUP BY MONTH(start);";
    public static final String SCHEDULE_BY_USER = "SELECT user.userName, description, start, end FROM appointment INNER JOIN user ON appointment.userId = user.userId;";
    public static final String DELETE_APPOINTMENT = "DELETE FROM appointment WHERE appointmentid = ?";
    
    private static PreparedStatement queryUserLogin;
    private static PreparedStatement insertCustAddress;
    private static PreparedStatement insertCust;
    private static PreparedStatement queryForCity;
    private static PreparedStatement updateCustomer;
    private static PreparedStatement updateAddress;
    private static PreparedStatement deleteAddress;
    private static PreparedStatement deleteCustomer;
    private static PreparedStatement queryCustId;
    private static PreparedStatement insertAppt;
    private static PreparedStatement updateAppt;
    private static PreparedStatement deleteAppt;

    private static DBConnection instance = new DBConnection();

    private DBConnection() {
    }

    public static DBConnection getInstance() {
        return instance;
    }

    public static Connection makeConnection() throws ClassNotFoundException, SQLException, Exception {

        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(DB_URL, username, password);
        System.out.println("Connecting to database...");
        queryUserLogin = conn.prepareStatement(QUERY_USER);
        insertCustAddress = conn.prepareStatement(INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS);
        insertCust = conn.prepareStatement(INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
        queryForCity = conn.prepareStatement(QUERY_CITY);
        updateCustomer = conn.prepareStatement(UPDATE_CUSTOMER);
        updateAddress = conn.prepareStatement(UPDATE_ADDRESS);
        deleteAddress = conn.prepareStatement(DELETE_ADDRESS);
        deleteCustomer = conn.prepareStatement(DELETE_CUSTOMER);
        queryCustId = conn.prepareStatement(QUERY_CUSTOMER_ID);
        insertAppt = conn.prepareStatement(INSERT_APPOINTMENT);
        updateAppt = conn.prepareStatement(UPDATE_APPOINTMENT);
        deleteAppt = conn.prepareStatement(DELETE_APPOINTMENT);

        return conn;
    }

    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        if (queryUserLogin != null) {
            queryUserLogin.close();
        }
        if (insertCustAddress != null) {
            insertCustAddress.close();
        }
        if (insertCust != null) {
            insertCust.close();
        }
        if (queryForCity != null) {
            queryForCity.close();
        }
        if (updateCustomer != null) {
            updateCustomer.close();
        }
        if (updateAddress != null) {
            updateAddress.close();
        }
        if (deleteAddress != null) {
            deleteAddress.close();
        }
        if (deleteCustomer != null) {
            deleteCustomer.close();
        }
        if (queryCustId != null) {
            queryCustId.close();
        }
        if (deleteAppt != null) {
            deleteAppt.close();
        }
        if (insertAppt != null) {
            insertAppt.close();
        }
        if (updateAppt != null) {
            updateAppt.close();
        }
        conn.close();
        System.out.println("Connection closed!");

    }

    public int queryForUserLogin(String username, String password) throws SQLException {
        queryUserLogin.setString(1, username);
        queryUserLogin.setString(2, password);
        ResultSet results = queryUserLogin.executeQuery();
        if (results.next()) {
            int id = results.getInt("userId");
            return id;
        } else {
            throw new SQLException("Incorrect username and password");

        }

    }

    public int insertCustomerAddress(String address, int city, String postalCode, String phone) throws SQLException {
        insertCustAddress.setString(1, address);
        insertCustAddress.setInt(2, city);
        insertCustAddress.setString(3, postalCode);
        insertCustAddress.setString(4, phone);

        int affectedRows = insertCustAddress.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't insert address.");
        }
        ResultSet generatedKeys = insertCustAddress.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Couldn't get id for address");
        }
    }

    public ArrayList<Integer> insertCustomer(String name, String address, int city, String postalCode, String phone) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        int addressId = insertCustomerAddress(address, city, postalCode, phone);
        ids.add(addressId);
        insertCust.setString(1, name);
        insertCust.setInt(2, addressId);

        int affectedRows = insertCust.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't insert customer.");
        }
        ResultSet generatedKeys = insertCustAddress.getGeneratedKeys();
        if (generatedKeys.next()) {
            ids.add(generatedKeys.getInt(1));
            return ids;
        } else {
            throw new SQLException("Couldn't get id for customer");
        }
    }

    public ObservableList<City> listCities() {
        ObservableList<City> cityList = FXCollections.observableArrayList();

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT cityid, city FROM city;")) {
            try {
                while (rs.next()) {
                    int id = rs.getInt("cityid");
                    String name = rs.getString("city");
                    City city = new City(id, name);
                    cityList.add(city);
                }
                return cityList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public City FindCity(String name) throws SQLException {
        ObservableList<City> listOfCities = listCities();
        for (City city : listOfCities) {
            if (city.getCity().equals(name)) {
                return city;
            }
        }
        System.out.println("Couldn't find city");
        return null;
    }

    public void UpdateCustomer(String name, int id, String address, int cityId, String postalCode, String phone, int addressId) throws SQLException {
        updateCustomer.setString(1, name);
        updateCustomer.setInt(2, id);

        int affectedRows = updateCustomer.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't update customer.");
        }
        UpdateAddress(address, cityId, postalCode, phone, addressId);
    }

    public void UpdateAddress(String address, int cityId, String postalCode, String phone, int addressId) throws SQLException {
        updateAddress.setString(1, address);
        updateAddress.setInt(2, cityId);
        updateAddress.setString(3, postalCode);
        updateAddress.setString(4, phone);
        updateAddress.setInt(5, addressId);

        int affectedRows = updateAddress.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't update address.");
        }
    }

    public void DeleteAddress(int addressId) throws SQLException {
        deleteAddress.setInt(1, addressId);

        int affectedRows = deleteAddress.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't delete address.");
        }
    }

    public void DeleteCustomer(int customerId, int addressId) throws SQLException {
        deleteCustomer.setInt(1, customerId);

        int affectedRows = deleteCustomer.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't delete customer.");
        }
        DeleteAddress(addressId);

    }

    public int QueryCustomerId(String name) throws SQLException {
        queryCustId.setString(1, name);
        try (
                ResultSet results = queryCustId.executeQuery()) {
            while (results.next()) {
                int id = results.getInt("customerid");
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public void InsertAppointment(int customerid, String description, String type, Timestamp start, Timestamp end) throws SQLException {
        insertAppt.setInt(1, customerid);
        insertAppt.setString(2, description);
        insertAppt.setTimestamp(3, start);
        insertAppt.setTimestamp(4, end);
        insertAppt.setString(5, type);

        int affectedRows = insertAppt.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't add appointment.");
        }

    }

    public ObservableList<Appointment> queryAppointment() {
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY_APPOINTMENTS)) {
            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
            while (rs.next()) {
                String name = rs.getString("customerName");
                String description = rs.getString("description");
                String type = rs.getString("type");
                Timestamp tsStart = rs.getTimestamp("start");

                ZoneId newzid = ZoneId.systemDefault();
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp tsEnd = rs.getTimestamp("end");
                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                int userId = rs.getInt("userId");
                int appointmentId = rs.getInt("appointmentid");

                Appointment appointment = new Appointment(new ReadOnlyStringWrapper(name),
                        new ReadOnlyStringWrapper(description), new ReadOnlyStringWrapper(type),
                        new ReadOnlyObjectWrapper(newLocalStart), new ReadOnlyObjectWrapper(newLocalEnd), new ReadOnlyObjectWrapper(userId), appointmentId);
                appointmentList.add(appointment);
            }
            return appointmentList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void UpdateAppointment(int customerId, String description, Timestamp start, Timestamp end, String type, int appointmentid) throws SQLException {
        updateAppt.setInt(1, customerId);
        updateAppt.setString(2, description);
        updateAppt.setTimestamp(3, start);
        updateAppt.setTimestamp(4, end);
        updateAppt.setString(5, type);
        updateAppt.setInt(6, appointmentid);

        int affectedRows = updateAppt.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't update appointment.");
        }
    }
    
    public void DeleteAppointment(int appointmentid) throws SQLException {
        deleteAppt.setInt(1, appointmentid);

        int affectedRows = deleteAppt.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't delete appointment.");
        }
    }

    
    
    public String ApptTypeByMonth() {
        StringBuilder sb = new StringBuilder("Month Number");
        sb.append("\t");
        sb.append("Meeting");
        sb.append("Follow-up");
        sb.append("Consultation");
        
        sb.append(System.lineSeparator());
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(APPT_TYPES_BY_MONTH)) {
            
            while (rs.next()) {
                int monthNumber = rs.getInt("month");
                int meeting = rs.getInt("Meeting");
                int consultation = rs.getInt("Consultation");
                int followUp = rs.getInt("FollowUp");
                sb.append(monthNumber);
                sb.append("\t");
                sb.append("\t");
                sb.append("\t");
                sb.append("\t");
                sb.append(meeting);
                sb.append("\t \t");
                sb.append(followUp);
                sb.append("\t \t");
                sb.append(consultation);
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return sb.toString();
        }
    }
        
    public ArrayList<UserSchedule> ScheduleByUser() {
        ArrayList<UserSchedule> schedule = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SCHEDULE_BY_USER)) {
            while (rs.next()) {
                String userName = rs.getString("userName");
                String description = rs.getString("description");
                Timestamp tsStart = rs.getTimestamp("start");
                ZoneId newzid = ZoneId.systemDefault();
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);
                LocalDateTime localStart = newLocalStart.toLocalDateTime();

                Timestamp tsEnd = rs.getTimestamp("end");
                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);
                LocalDateTime localEnd = newLocalEnd.toLocalDateTime();
                UserSchedule entry = new UserSchedule(userName, description, localStart, localEnd);
                schedule.add(entry);
              
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return schedule;
        }
        
    }

}
