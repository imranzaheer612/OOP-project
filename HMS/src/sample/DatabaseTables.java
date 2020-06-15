package sample;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseTables {


    Connection connection = DatabaseConnection.connector();
    TableView reservationsTable = new TableView<>();
    TableView customersTable = new TableView<>();


    TableColumn<String, TableDataForReservations> column1 = new TableColumn<>("Room Number");
    TableColumn<String, TableDataForReservations> column2 = new TableColumn<>("Name");
    TableColumn<String, TableDataForReservations> column3 = new TableColumn<>("Phone Number");
    TableColumn<String, TableDataForReservations> column4 = new TableColumn<>("ID");
    TableColumn<String, TableDataForReservations> column5 = new TableColumn<>("City");

    /*
     * this method help us in making a table view
     * it will make a table of the database
     * we just have to pass the -->>> tableName of either new reservations or deleted reservations
     * */

     default TableView makingTableViewForReservationClass(String tableName){
        /*
         * making*/
        reservationsTable.getItems().clear();
        commonColumn();
        TableColumn<String, TableDataForReservations> column6  = new TableColumn<>("Arrival Date");
        column6.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        TableColumn<String, TableDataForReservations> column7  = new TableColumn<>("Departure Date");
        column7.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        TableColumn<String, TableDataForReservations> column8  = new TableColumn<>("Reservation Number");
        column8.setCellValueFactory(new PropertyValueFactory<>("reservationNumber"));

        reservationsTable.getColumns().setAll(column1, column2, column3, column4, column5, column6, column7,  column8);

        Rooms rooms = new Rooms();
        gettingDataOfReservations(rooms.singleRooms(), tableName);
        gettingDataOfReservations(rooms.doubleRooms(), tableName);
        gettingDataOfReservations(rooms.tripleRooms(), tableName);

        return reservationsTable;
    }

    /*
     * we have made this method for the table view method
     * in this method we will dig the database info bu using the database quries
     * and then pass it to th table to show the data
     *
     * */

    default void gettingDataOfReservations(String[] roomTypeArray, String tableName){

        String[] data = new String[8];
        PreparedStatement stmt;
        for (String s : roomTypeArray) {
            String query = "SELECT * FROM " + tableName + " WHERE roomNumber = ?";
            try {
                stmt = connection.prepareStatement(query);
                stmt.setString(1, s);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    data[0] = rs.getString("roomNumber");
                    data[1] = rs.getString("name");
                    data[2] = rs.getString("phoneNumber");
                    data[3] = rs.getString("id");
                    data[4] = rs.getString("city");
                    data[5] = rs.getString("arrivalDate");
                    data[6] = rs.getString("departureDate");
                    data[7] = rs.getString("reservationNumber");

                    reservationsTable.getItems().add(new TableDataForReservations(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]));
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

    }
    /*
     * now we are going to make a table view that will help to show the
     *  tables of the customer class we will use it for
     * --> new Customers table
     * --> deleted customers table
     * */

    default TableView makingTableViewForCustomerClass(String tableName) {

        customersTable.getItems().clear();
        commonColumn();
        TableColumn<String, TableDataForReservations> column6 = new TableColumn<>("Nationality");
        column6.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        TableColumn<String, TableDataForReservations> column7 = new TableColumn<>("Birth Date");
        column7.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        TableColumn<String, TableDataForReservations> column8 = new TableColumn<>("Departure Date");
        column8.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        TableColumn<String, TableDataForReservations> column9 = new TableColumn<>("Emergency Contact Name");
        column9.setCellValueFactory(new PropertyValueFactory<>("emergencyContactName"));
        TableColumn<String, TableDataForReservations> column10 = new TableColumn<>("Emergency Contact Number");
        column10.setCellValueFactory(new PropertyValueFactory<>("emergencyContactNumber"));
        TableColumn<String, TableDataForReservations> column11 = new TableColumn<>("Arrival Date");
        column11.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        TableColumn<String, TableDataForReservations> column12 = new TableColumn<>("Customer Number");
        column12.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));

        customersTable.getColumns().setAll(column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, column11, column12);

        Rooms rooms = new Rooms();
        gettingDataForCustomer(rooms.singleRooms(), tableName);
        gettingDataForCustomer(rooms.doubleRooms(), tableName);
        gettingDataForCustomer(rooms.tripleRooms(), tableName);
        return customersTable;
    }

    default void gettingDataForCustomer(String[] roomTypeArray, String tableName){

        String[] data = new String[12];
        PreparedStatement stmt;
        for (String rooms : roomTypeArray) {
            String query = "SELECT * FROM " +  tableName + " WHERE roomNumber = ?";
            try {
                stmt = connection.prepareStatement(query);
                stmt.setString(1, rooms);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    data[0] = rs.getString("roomNumber");
                    data[1] = rs.getString("name");
                    data[2] = rs.getString("phNum");
                    data[3] = rs.getString("id");
                    data[4] = rs.getString("city");
                    data[5] = rs.getString("nationality");
                    data[6] = rs.getString("birthDate");
                    data[7] = rs.getString("departureDate");
                    data[8] = rs.getString("emergencyContactNumber");
                    data[9] = rs.getString("emergencyContactName");
                    data[10] = rs.getString("arrivalDate");
                    data[11] = rs.getString("customerNumber");

                    customersTable.getItems().add(new TableDataForCustomerClass(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11]));
                }
                rs.close();
                stmt.close();
            }
            catch (SQLException e){
                System.out.println(e);
            }
        }
    }
    /*
    * we have made two types of tables
    * both of them have have some common column implementations
    * we gonna move all these implementations to a single method
    * */
    default void commonColumn(){
        column1.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        column3.setCellValueFactory(new PropertyValueFactory<>("phNumber"));
        column4.setCellValueFactory(new PropertyValueFactory<>("id"));
        column5.setCellValueFactory(new PropertyValueFactory<>("city"));
    }



}
