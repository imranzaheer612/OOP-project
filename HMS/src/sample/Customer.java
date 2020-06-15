package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;


import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Customer extends Reservation {


    private TextField emergencyPhNumberEntry = new TextField();
    private TextField emergencyContactNameEntry = new TextField();
    private TextField nationalityEntry = new TextField();
    private DatePicker birthDate = getCheckInPicker();// we gonna use the check in picker as the birthDate

    private String tableNameOfNewCustomers = "newCustomers";
    private String tableNameOfOldCustomers = "oldCustomers";
    private static int customerNumber;// we gonna save and access the booking fom here
    private static int totalRooms = 100;
    private int[] emptyRooms = new int[totalRooms];

    private String roomToGive;
    private String reservationNumberOfRoomTODelete;

    private String[] singleRoomsArr = singleRooms();
    private String[] doubleRoomsArr = doubleRooms();
    private String[] tripleRoomsArr = tripleRooms();

    private TableView tableCustomerClass;

    private ComboBox<String> dropBox;


    Customer() {
    }

    /* Setting visual
        effects for
        Customer pane
     */

    Customer(BorderPane root) {

        mainRoot = root;
        mainRoot.getChildren().clear();
        sidePanel.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 10% 50%, rgba(0,255,255,0.26), rgba(0,63,139,0.45))");
        sidePanel.setPrefWidth(400);
        sidePanel.setPadding(new Insets(50, 20, 50, 50));
        sidePanel.setSpacing(30);
        mainRoot.setCenter(reservationPane);

        reservationPane.setPadding(new Insets(50, 100, 10, 50));
        reservationPane.setHgap(70);
        reservationPane.setVgap(10);
        reservationPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 50% 50%, #FFE4C4, #40E0D0)");
    }


    /*
     * now making an interface for
     * dealing with the customers
     * it will include the options like modify, delete or display a data
     * */
    @Override
    public void mainInterface() throws FileNotFoundException {
        super.mainInterface();

        mainRoot.setLeft(sidePanel);
        getNewEntry().setText("Check In Customer");
        getShowData().setText("All Customers");
        getShowOldData().setText("Old Customers");
        getDeletion().setText("Check Out Customers");
        getModifyEntry().setText("Edit a Customer");

        getNewEntry().setOnAction(e -> newCustomer(false));
        getShowData().setOnAction(e -> {
            mainRoot.getChildren().remove(sidePanel);
            showTable(tableNameOfNewCustomers);
        });
        getShowOldData().setOnAction(e -> showTable(tableNameOfOldCustomers));
        getDeletion().setOnAction(e -> checkOutInterface());
        getModifyEntry().setOnAction(e -> gettingInputToModify());

        Image logo9 = new Image(new FileInputStream("logos/avatar2.png"));
        ImageView customerMenu = new ImageView(logo9);
        customerMenu.setFitHeight(200);
        customerMenu.setFitWidth(200);
        customerMenu.setTranslateX(20);

        Label welcomeInfo = new Label();
        welcomeInfo.setText("Welcome to the Customers center\n" +
                "You can give rooms to the customers here");
        welcomeInfo.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));

        sidePanel.getChildren().clear();
        sidePanel.getChildren().add(customerMenu);
        sidePanel.getChildren().add(welcomeInfo);

    }
    /* Logos
       for
       Customer Pane
    */
    public void checkOutInterface(){
        mainRoot.setLeft(sidePanel);
        sidePanel.getChildren().clear();
        Label info = new Label("You can Delete a customer\nOR\ncan check its Invoice");
        info.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));
        sidePanel.getChildren().add(info);
        Button delete = new Button("Delete Customer");
        Button invoice = new Button("Invoice of Customer");

        delete.setPrefSize(300, 10);
        invoice.setPrefSize(300, 10);

        reservationPane.getChildren().clear();
        reservationPane.setPadding(new Insets(50, 100, 10, 50));
        reservationPane.add(delete, 2, 1);
        reservationPane.add(invoice, 2, 2);
        reservationPane.add(getBack(), 3, 3);

        delete.setOnAction(e -> deleteEntryInterface());
        invoice.setOnAction(e -> new Invoice(mainRoot).entryInterface(false));
        getBack().setOnAction(e -> {
            try {
                mainInterface();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

    }

    public void newCustomer(boolean attendingReservation) {
        refreshEntry();
        userEntryInterface();// thn shoeing the remaining interface
        System.out.println("entry text value  4 " +  getEntryText().getText() );

        dropBoxForRoomNumber(false);// then making a drop box having all the empty
        System.out.println("entry text value 5 " +  getEntryText().getText() );

        System.out.println("from new customer");
        System.out.println(Arrays.toString(singleRoomsArr));
        getEnterButton().setOnAction(e -> {
            if (checking()) {
                if (roomIsNotReserved(attendingReservation)){
                    customerNumber();
                    savingCustomer();
                    newCustomer(attendingReservation);
                    if (attendingReservation){
                        try {
                            getConnection().close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        DatabaseDeletion processing = new DatabaseDeletion();
                        processing.movingRecordToOtherTable(tableNameOfOldReservations, tableNameOfReservedRooms, "reservationNumber", getEntryText());
                        processing.deletingRecord(tableNameOfReservedRooms, "reservationNumber", getEntryText());
                        setConnection(DatabaseConnection.connector());
                    }
                }
            }

        });
        getBack().setOnAction(e -> {
            try {
                mainInterface();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }


    /*
     * this is the entry interface
     * --> it will be help full i getting
     * data of the Customers
     * */

    @Override
    public void userEntryInterface() {
        super.userEntryInterface();
        reservationPane.getChildren().removeAll(getEnterButton(), getBack(), getStatus());


        /*
         * arrival label was in the reservation class we inherited it
         * , changed it text and then use this label
         * as" date of birth"
         * --> now updating the interface
         * */
        Line line = new Line();
        //Setting the properties to a line
        line.setStartX(100.0);
        line.setStartY(150.0);
        line.setEndX(300.0);
        line.setEndY(150.0);

        //making new data entries
        Label extraInfo = new Label("Emergency Contact Information");
        extraInfo.setFont(Font.font("Montserrat", FontWeight.NORMAL, 23));
        extraInfo.setFont(Font.font("Montserrat", FontWeight.NORMAL, 23));
        Label emergencyPhNum = new Label("Emergency Phone Number");
        Label emergencyContactName = new Label("Contact Name");


        Label nationalityLabel = new Label("Nationality");
        getArrival().setText("Date of birth");
        reservationPane.add(nationalityLabel, 0, 10);
        reservationPane.add(nationalityEntry, 1, 10);

        reservationPane.add(line, 0, 11);
        reservationPane.add(extraInfo, 0, 12);
        reservationPane.add(emergencyContactName, 0, 14);
        reservationPane.add(emergencyContactNameEntry, 1, 14);
        reservationPane.add(emergencyPhNum, 0, 15);
        reservationPane.add(emergencyPhNumberEntry, 1, 15);
        reservationPane.add(getStatus(), 0, 17);
        reservationPane.add(getEnterButton(), 1, 19);
        reservationPane.add(getBack(), 2, 22);

    }

    /*
     * now we gonna made a booking number genrator as we made in the
     * reservation class for making a reservation number
     * --> we will save the initialized number in a
     * the data variable (customerNumber)
     * --> this gonna give the number after checking that
     * there should no be a same =booking number already
     * defined in the database
     * */

    private void customerNumber() {
        PreparedStatement stmt;
        String query = "SELECT * FROM  C_NumGenrator";
        try {
            stmt = getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                customerNumber = Integer.parseInt(rs.getString("cNumber"));
                stmt.close();
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerNumber++;

        update_Cnum_Database();
    }

    public void update_Cnum_Database(){
        PreparedStatement stmt;
        String query = "UPDATE C_NumGenrator SET cNumber = ?";
        try{
            stmt = getConnection().prepareStatement(query);
            stmt.setString(1, String.valueOf(customerNumber));
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /*
     * --> now we gonna save the data into
     * the database
     * */
    public void savingCustomer() {
        PreparedStatement statement;

        String query = "INSERT INTO " + tableNameOfNewCustomers + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            statement = getConnection().prepareStatement(query);
            statement.setString(1, getNameEntry().getText());
            statement.setString(2, getPhNumEntry().getText());
            statement.setString(3, getIdEntry().getText());
            statement.setString(4, getCityEntry().getText());
            statement.setString(5, nationalityEntry.getText());
            statement.setString(6, birthDate.getValue().toString());
            statement.setString(7, getCheckOutPicker().getValue().toString());
            statement.setString(8, emergencyContactNameEntry.getText());
            statement.setString(9, emergencyPhNumberEntry.getText());
            statement.setString(10, roomToGive);
            statement.setString(11, String.valueOf(customerNumber));
            statement.setString(12, (new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date())));
            statement.executeUpdate();
            statement.close();
            refreshEntry();
            String status = "The data have been submitted successfully!\n" +
                    "The customer number is " + customerNumber +
                    "\n Arrival date is " + new Date();
            warningStage(status, "Status");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    /*
     * after a successful saving of the data
     * the TextFields will be  refreshed
     * i.e--> it will become empty so that  a new data of the customer
     * could be saved
     * */
    @Override
    public void refreshEntry() {
        super.refreshEntry();
        emergencyPhNumberEntry.setText("");
        emergencyContactNameEntry.setText("");
        nationalityEntry.setText("");
        birthDate.setValue(null);

        sidePanel.getChildren().clear();
//        roomTypeSelection();
//        dropBoxForRoomNumber(false);
    }

    /*
     * we have inherited it from the super class  and
     * gonna use it in our customer class
     * */

    /*
    * this warning stage will include tqqo butons
    * we made this for specific use for deleting the reservation
    * */
    public void warningStageToDeleteReservation(String info, String title) {
        warning_R_Deletion(info, title, "OK", "Continue");
    }

    @Override
    public void handle_R_Deletion() throws SQLException {
        DatabaseDeletion processing = new DatabaseDeletion();
        processing.movingRecordToOtherTable(tableNameOfOldReservations, tableNameOfReservedRooms, "reservationNumber", reservationNumberOfRoomTODelete);
        processing.deletingRecord(tableNameOfReservedRooms,"reservationNumber", reservationNumberOfRoomTODelete);
    }




    /*
     * --> we have inherited it from the super class and
     * made some modifications in it so that
     * we gonna use it in the customer class in order to
     * check that if thr customer data entered by the user
     * is correct ot not
     * */


    @Override
    public boolean checking() {
        if (emergencyPhNumberEntry.getText().isBlank() || isNotInteger(emergencyPhNumberEntry.getText())) {
            Toolkit.getDefaultToolkit().beep();
            getStatus().setText("Please enter the data in correct form!");
            getStatus().setTextFill(Color.web("#FF0000"));
            return false;
        }
        return super.checking();
    }


    public void dropBoxForRoomNumber(boolean useForModification) {
        //by adding the boolean parameter we can also use it for the modification process
        Label label = new Label("Drop box will only show empty Rooms");
        initializingEmptyRooms(useForModification);//first renewing the empty rooms data

        dropBox = new ComboBox<>(FXCollections.observableArrayList(getSelectedRoomArray()));
        dropBox.getSelectionModel().selectFirst();

        if (useForModification)dropBox.setValue(roomToGive);
        /*
         * the selected room by the user
         * will be stored in a integer to be processed further
         * */
        System.out.println("i am in the show man...!");
        roomToGive = dropBox.getValue();
        dropBox.setOnAction(actionEvent -> roomToGive = dropBox.getValue());


        dropBox.setPrefWidth(250);
        sidePanel.getChildren().add(label);
        sidePanel.getChildren().add(dropBox);
    }

    @Override
    public void roomToggleListener() {
        getSingleRoom().setSelected(true);
        getRoomSeatsToggle().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
            dropBox.setItems(FXCollections.observableArrayList(getSelectedRoomArray()));
            dropBox.getSelectionModel().selectFirst();
            }
        });
    }

    @Override
    public String[] getSelectedRoomArray() {
        if (getSelectedRoomRadio().equals(getSingleRoom())){
            return singleRoomsArr;
        }
        else if (getSelectedRoomRadio().equals(getDoubleRoom())){
            return doubleRoomsArr;
        }
        else return tripleRoomsArr;
    }

    public void initializingEmptyRooms(boolean useForModificationMode) {
        singleRoomsArr = gettingDataToCheckEmptyRooms(singleRoomsArr, useForModificationMode);
        doubleRoomsArr = gettingDataToCheckEmptyRooms(doubleRoomsArr, useForModificationMode);
        tripleRoomsArr = gettingDataToCheckEmptyRooms(tripleRoomsArr, useForModificationMode);

        System.out.println(Arrays.toString(singleRoomsArr));
    }

    public String[] gettingDataToCheckEmptyRooms(String[] roomsArray, boolean useForModificationMode){
        PreparedStatement stmt;
        for (int i = 0; i < roomsArray.length; i++) {
            try {
                if (useForModificationMode){
                    String query = "SELECT roomNumber FROM " + tableNameOfNewCustomers + " WHERE (roomNumber = ?) AND (roomNumber != ?)";
                    stmt = getConnection().prepareStatement(query);
                    stmt.setString(1, roomsArray[i]);
                    stmt.setString(2, gettingCustomerDataFromDatabase()[0]);
                    System.out.println("helo g here ree " + gettingCustomerDataFromDatabase()[0]);

                }
                else{
                    String query = "SELECT roomNumber FROM " + tableNameOfNewCustomers + " WHERE roomNumber = ?";
                    stmt = getConnection().prepareStatement(query);
                    stmt.setString(1, roomsArray[i]);
                }
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("room  number: " + roomsArray[i]);
                    roomsArray[i] = "0";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        roomsArray = remove(roomsArray, "0");//removing all the elements having value 0
        System.out.println(Arrays.toString(roomsArray));

        return  roomsArray;
    }


    /*
     *this function will remove the all the elements having
     * the targeted value
     */
    public static String[] remove(String[] stringsArr, String target) {
        int count = 0;
        // loop over array to count number of target values.
        // this required to calculate length of new array
        for (String string : stringsArr) {
            if (string.equals(target)) count++;
        }
        // if original array doesn't contain number to removed
        // return same array
        if (count == 0) {
            return stringsArr;
        }
        String[] result = new String[stringsArr.length - count];
        int index = 0;
        for (String value : stringsArr) {
            if (!value.equals(target)) {
                result[index] = value;
                index++;
            }
        }
        stringsArr = null; // make original array eligible for GC
        return result;
    }

    /*
     * we gonna use the javafx table view here
     * for showing the reserved rooms data from
     * the database
     * */
    private void showTable(String tableName) {
        reservationPane.getChildren().clear();
        reservationPane.setPadding(new Insets(50, 50, 50, 50));

        /*
         * making a scrollable interface for teh data base table*/
        TableView table = makingTableViewForCustomerClass(tableName);
        table.setPrefSize(1500, 1000);

        reservationPane.add(table, 0, 0);
        reservationPane.add(getBack(), 0, 10);
        getBack().setOnAction(e -> {
            try {
                mainInterface();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }



    public void deleteEntryInterface() {
        reservationPane.getChildren().clear();
        getEntryText().setText("");
        Label text = new Label();
        text.setText("Enter the Customer NUmber to Delete");
        text.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));


        reservationPane.setPadding(new Insets(100, 200, 200, 300));
        reservationPane.add(text, 1, 0);
        reservationPane.add(getEntryText(), 1, 1);
        reservationPane.add(getEnterButton(), 1, 2);
        reservationPane.add(getBack(), 2, 4);
        getBack().setOnAction(e -> {
            checkOutInterface();
        });
        /*
         * the room will only be deleted if it is not present in reservedRooms
         * this mean this room have been reserved
         * otherwise warning will be shown indicating that this room have not been reserved yet
         * */
        getEnterButton().setOnAction(e -> deletionChecking());
    }

    @Override
    public void deletionChecking() {
        String[] data = gettingCustomerDataFromDatabase();
        String war = "Are you sure you want to delete Customer Number " + getEntryText().getText()
                + "\nName : " + data[1]
                + "\nPhNum : " + data[2]
                + "\nRoom : " + data[0];

        if (getEntryText().getText().isBlank() || isNotInteger(getEntryText().getText())){
            warningStage("Please enter the correct Reservation Number", "Warning");
        }
        else {
            if (dataExist("customerNumber", tableNameOfNewCustomers)) {
                warning_C_Deletion(war, "Warning", "No", "Yes");
            } else {
                warningStage("No such customer found", "Warning");
            }
        }
    }

    @Override
    public void handle_C_Deletion() throws SQLException {
        updateDepartureDate();
        getConnection().close();
        DatabaseDeletion processing = new DatabaseDeletion();
        processing.movingRecordToOtherTable(tableNameOfOldCustomers, tableNameOfNewCustomers, "customerNumber", getEntryText());
        processing.deletingRecord(tableNameOfNewCustomers, "customerNumber", getEntryText());
        warningStage("Room is successfully deleted!", "Status");
        setConnection(DatabaseConnection.connector());
    }

    /*
     * we gonna update the departure date whenever we are going to delete a customer
     * in oder to get the exact time
     * */
    public void updateDepartureDate() {
        PreparedStatement statement;

        String query = "UPDATE " + tableNameOfNewCustomers + " SET " +
                "departureDate = ? " +
                " WHERE customerNumber = " + getEntryText().getText();
        try {
            statement = getConnection().prepareStatement(query);
            statement.setString(1, new Date().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    /*
     * it gonna show the user a screen where the user can
     * enter the specific customer number to modify
     * */

    public void gettingInputToModify() {
        super.targetInputInterface("Enter the Customer Number you want to Edit");
        //this will contain a textField to enter number and a enter button to carry on process
        //getEntryText().setPromptText("Enter the Customer to Edit");
        getBack().setOnAction(e -> {
            try {
                mainInterface();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        //initializing the enter button of "target Input interface"
        getEnterButton().setOnAction(e -> {
            if (getEntryText().getText().isBlank() || isNotInteger(getEntryText().getText())) {
                warningStage("Please enter the correct Customer Number", "warning");
            }
            else if(dataExist("customerNumber", tableNameOfNewCustomers)) {
                modifyInterface();
            }
            else warningStage("No such room exist", "warning");
        });

    }

    /*the room we gonna give to the customer should not be reserved in today's date*/
    public boolean roomIsNotReserved(boolean attendingReservation){
        System.out.println("checking 1");
        String warningString;// we will show this string if there condition satisfied
        PreparedStatement stmt;
        String query = "SELECT arrivalDate, departureDate, reservationNumber FROM reservedRooms WHERE roomNumber = ?";
        if (attendingReservation){
            query = "SELECT arrivalDate, departureDate, reservationNumber FROM reservedRooms WHERE (roomNumber = ?" + ") AND (reservationNumber != " + getEntryText().getText() +")";
        }
        boolean notClearEntry = false;
        LocalDate arrival;
        LocalDate departure;
        LocalDate presentDate = LocalDate.now();
        try{
            stmt = getConnection().prepareStatement(query);
            stmt.setString(1, roomToGive);
            ResultSet rs = stmt.executeQuery();


            while(rs.next()){

                //getting data from the database and storing them in the local Date variable after parsing it
                arrival = LocalDate.parse(rs.getString("arrivalDate"));
                departure = LocalDate.parse(rs.getString("departureDate"));
                reservationNumberOfRoomTODelete = rs.getString("reservationNumber");
                //the arrival date shoud not be equal to anny arrival period
                // and also not equal to any departure period
                notClearEntry = (presentDate.isAfter(arrival) || presentDate.isEqual(arrival)) && presentDate.isBefore(departure)
                        //depature date of the customer should not be in bw the reservation period
                        || (getCheckOutPicker().getValue().isAfter(arrival) && (getCheckOutPicker().getValue().isBefore(departure) || getCheckOutPicker().getValue().isEqual(departure)))
                        || (presentDate.isBefore(arrival) && getCheckOutPicker().getValue().isAfter(departure));
                //breaking the loop if we find the reservation that is on today
                if (notClearEntry){

                    rs.close();
                    stmt.close();

                    System.out.println("checking 4");
                    warningString = "The Room No " +  roomToGive + " is already reserved\n" +
                            "Reservation Period: " + arrival + " to " + departure + "\n" +
                            "Press \"Continue\" to Delete this Reservation\n" +
                            "Press \"OK\" if you want to change the Room Number or Dates" ;
                    getConnection().close();
                    warningStageToDeleteReservation(warningString, "Warning");
                    setConnection(DatabaseConnection.connector());

                    break;
                }
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return !notClearEntry;
    }


    /*
    * interface for modifying ant customer data
    * it will be same as the entry interface so
    * that's why we also gonna use the entryInterface method here
    * */
    public void modifyInterface() {
        userEntryInterface();
        Button newModify = new Button("Update other one");
        newModify.setPrefSize(150,  10);
        reservationPane.add(newModify, 2,  21 );
        showingPreviousData();
        reservationPane.setSnapToPixel(false);
        getEnterButton().setOnAction(e -> {
            if (checking()) {
                if(roomIsNotReserved(false)){
                    modificationInDataBase();
                    warningStage("Info is modified successfully!",  "Status");
                    modifyInterface();
                }
            }
        });
        newModify.setOnAction(e -> gettingInputToModify());
//        reservationPane.setGridLinesVisible(true);
    }


    /*
    * when the modification Interface open this method will help us in is setting the default
    * value of the text fields to the previous customer data
    * we gonna also show the previous data of the user in a side label helping the user to
    * modify properly
    * */
    public void showingPreviousData(){

        Label headingLabelForPreviousData = new Label("Previous Data");
        headingLabelForPreviousData.setTextFill(Color.LIGHTGRAY);
        headingLabelForPreviousData.setFont(Font.font("Montserrat", FontWeight.NORMAL, 25));
        String[] customerData = gettingCustomerDataFromDatabase();

        Label previousDataLabel = new Label();
        previousDataLabel.setText(
                "RoomNumber:\t\t\t\t" + customerData[0] + "\n" +
                "Name:\t\t\t\t\t\t" + customerData[1] + "\n" +
                "Phone Number:\t\t\t\t" + customerData[2] + "\n" +
                "Passport/ID:\t\t\t\t\t" + customerData[3] + "\n" +
                "City:\t\t\t\t\t\t\t" + customerData[4] + "\n" +
                "Nationality:\t\t\t\t\t" + customerData[5] + "\n" +
                "Birth Date:\t\t\t\t\t" + customerData[6] + "\n" +
                "Departure Date:\t\t\t\t" + customerData[7] + "\n" +
                "Emergency Contact Number:\t" + customerData[8] + "\n" +
                "Emergency Contact Name:\t\t" + customerData[9] + "\n" +
                "Arrival Date:\t\t\t\t\t" + customerData[10] + "\n" +
                "Customer Number:\t\t\t" + customerData[11] + "\n"
        );
        previousDataLabel.setFont(Font.font("Montserrat", FontWeight.BOLD, 13));
//        previousDataLabel.setMinHeight(225);
//        previousDataLabel.setMinWidth(400);
        previousDataLabel.setTextOverrun(OverrunStyle.CLIP);
        //this will show the previous data in the testFields
        LocalDate dateOfBith = LocalDate.parse(customerData[6]);
        LocalDate checkOut = LocalDate.parse(customerData[7]);
        roomToGive = customerData[0];
        getNameEntry().setText(customerData[1]);
        getPhNumEntry().setText(customerData[2]);
        getIdEntry().setText(customerData[3]);
        getCityEntry().setText(customerData[4]);
        nationalityEntry.setText(customerData[5]);
        getCheckOutPicker().setValue(checkOut);
        birthDate.setValue(dateOfBith);
        emergencyPhNumberEntry.setText(customerData[8]);
        emergencyContactNameEntry.setText(customerData[9]);

//        initializingEmptyRooms(true);
        dropBoxForRoomNumber(true);
        modifyRoomToggle(roomToGive);
        dropBox.setValue(roomToGive);

        /*
        * adding the labels to the side panel
        * */
        sidePanel.getChildren().add(headingLabelForPreviousData);
        sidePanel.getChildren().add(previousDataLabel);

    }

    @Override
    public void modifyRoomToggle(String roomNumber) {
        if (roomNumber.charAt(1) == 'A')getSingleRoom().setSelected(true);
        else if (roomNumber.charAt(1) == 'B')getDoubleRoom().setSelected(true);
        else getTripleRoom().setSelected(true);
    }

    public void modificationInDataBase() {
        PreparedStatement statement;

        String query = "UPDATE " + tableNameOfNewCustomers + " SET " +
                "name = ?, " +
                "phNum =?, " +
                "id = ?, " +
                "city = ?, " +
                "nationality = ?, " +
                "birthDate = ?, " +
                "departureDate = ?, " +
                "emergencyContactName = ?, " +
                "emergencyContactNumber = ?, " +
                "roomNumber = ?" +
                " WHERE customerNumber = " + getEntryText().getText();
        try {
            statement = getConnection().prepareStatement(query);
            statement.setString(1, getNameEntry().getText());
            statement.setString(2, getPhNumEntry().getText());
            statement.setString(3, getIdEntry().getText());
            statement.setString(4, getCityEntry().getText());
            statement.setString(5, nationalityEntry.getText());
            statement.setString(6, birthDate.getValue().toString());
            statement.setString(7, getCheckOutPicker().getValue().toString());
            statement.setString(8, emergencyContactNameEntry.getText());
            statement.setString(9, emergencyPhNumberEntry.getText());
            statement.setString(9, emergencyPhNumberEntry.getText());
            statement.setString(10, String.valueOf(roomToGive));

            statement.executeUpdate();
            statement.close();
            getStatus().setText("The data has been updated successfully!");
            getStatus().setTextFill(Color.DEEPSKYBLUE);
            getStatus().setFont(new Font("Arial", 13));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }
    /*
    * this method is used in order the get specific data from the Database
    * --> we gonna use it when ever er wnat to modify the previous data
    * of the customer*/

    public String[] gettingCustomerDataFromDatabase() {
        /*Saving the data in the form of the String array and then
         * showing it to the user*/

        String[] data = new String[12];
        //this will help in checking a room exist or not on given reservation
        PreparedStatement stmt;
        System.out.println("hhhhhhhhh + " + getEntryText().getText());
        String query = "SELECT * FROM " + tableNameOfNewCustomers + " WHERE customerNumber = " + getEntryText().getText();
        try {
            stmt = getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                /*
                * if the required data is present in the data base then savving it in the String array
                * from the string array we can process it further where ever we want it
                * */
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
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e){
                System.out.println(e);
            }
            return data;
    }


    /*
     *now we gonna mak a mehtod that will help us to attend a reservation , it means that if
     * we are going to attend a customer that have reserved his room already then we
     * have to take his previous details from the reservation database and then take some extra info from it
     * and after this saving all these combined details in the customer database
     * */

    public void gettingInputToAttendAReservation() throws FileNotFoundException {

        targetInputInterface("Enter the Reservation Number of the Customer you want to add");
        //making an logo
        Image logo7 = new Image(new FileInputStream("logos/infodesk.png"));
        ImageView infodesk = new ImageView(logo7);
        infodesk.setFitHeight(200);
        infodesk.setFitWidth(200);
        //making info label
        Label welcomeInfo = new Label();
        welcomeInfo.setText("Welcome to the rooms reservation center\n" +
                "You can deal with reservations here");
        welcomeInfo.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));
        //adding to side panel
        sidePanel.getChildren().add(infodesk);
        sidePanel.getChildren().add(welcomeInfo);
        mainRoot.setLeft(sidePanel);
        //action listeners
        getEnterButton().setOnAction(e -> {
            if(getEntryText().getText().isBlank() || isNotInteger(getEntryText().getText())) {
                warningStage("Please enter the correct Reservation Number", "Warning");
            }
            else if(reservationExists()) {
                reservationAttendingInterface();
            }

        });
        getBack().setOnAction( e -> {
            try {
                startScreen(mainRoot);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }

    public void reservationAttendingInterface(){

        String[] reservationData = searchingDataFromDatabase();

        newCustomer(true);

        modifyRoomToggle(reservationData[6]);
        mainRoot.setLeft(sidePanel);
        getNameEntry().setText(reservationData[0]);
        getPhNumEntry().setText(reservationData[1]);
        getIdEntry().setText(reservationData[2]);
        getCityEntry().setText(reservationData[3]);
        getCheckOutPicker().setValue(LocalDate.parse(reservationData[5]));
        roomToGive = reservationData[6];
        dropBox.setValue(roomToGive);

    }
    /*
    * checking that the room we are going to give to
    * the customer is not all ready occupied or not
    * But first checking that the reservation number entered by the user is correct or not
    * */

    boolean reservationExists(){
        String[] reservationData  = searchingDataFromDatabase();
        boolean correctR_Number = true;
        //checking if the reservation number entered is correct
        try {
            LocalDate.parse(reservationData[4]);
        } catch (NullPointerException exception){
            correctR_Number = false;
        }
        //if correct only then parse the local date from the database and check the reservation date
        if (correctR_Number){
            boolean reservationIsOnToday = (LocalDate.parse(reservationData[4])).isEqual(LocalDate.now());
            if (getEntryText().getText().equals(reservationData[7]) && !getEntryText().getText().isBlank()
                    && reservationIsOnToday) return true;
            else {
                warningStage("There reservation is for "+ reservationData[4] + "\nNot for Today", "Warning" );
                return false;
            }
        }
        //else if the reservation number is not correct show up a warning
        else {
            warningStage("No Such Reserved Room", "Warning");
            return false;
        }
    }

    public String getRoomToGive() {
        return roomToGive;
    }

    public void setRoomToGive(String roomToGive) {
        this.roomToGive = roomToGive;
    }

    public String getTableNameOfNewCustomers() {
        return tableNameOfNewCustomers;
    }

    public String getTableNameOfOldCustomers() {
        return tableNameOfOldCustomers;
    }
}
