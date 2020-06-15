package sample;
//used for making the interface
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
//used  for establishing the connection with the Database
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;


public class  Reservation extends Rooms implements UserOptions, DatabaseTables, Warning {



    private  static String notReservedYet;
    private static int reservationNumber = 1000;
    private String currentRoomNumber;//room in process
    private String selectedRoom = "";//room selected in the entry field

    //reservation number will be started from 1000

    private TextField nameEntry = new TextField();
    private TextField phNumEntry = new TextField();
    private TextField idEntry = new TextField();
    private TextField cityEntry = new TextField();
    private TextField entryText = new TextField();//we will call this when ever we want secure entry

    private DatePicker checkInPicker = new DatePicker();
    private DatePicker checkOutPicker = new DatePicker();

    private Label clientDetails = new Label("Client Details");
    private Label arrival = new Label("Arrival Date: ");
    private Label departure = new Label("Departure Date: ");
    private Label status = new Label();

    //Buttons we gonna use in the main intreface of the reservation class
    private Button enterButton = new Button("Enter");
    private Button back = new Button("Back");

    private Button newEntry = new Button();
    private Button showData = new Button();
    private Button modifyEntry = new Button();
    private Button deletion = new Button();
    private Button showOldData = new Button();

    private ToggleGroup roomTypesToggle = new ToggleGroup();
    private ToggleGroup roomSeatsToggle = new ToggleGroup();
    private RadioButton singleRoom;
    private RadioButton doubleRoom;
    private RadioButton tripleRoom;
    private ComboBox<String> dropBox;

    private Connection connection = DatabaseConnection.connector();
    String tableNameOfReservedRooms = "reservedRooms";
    String tableNameOfOldReservations = "oldReservedRooms";

    private GridPane gridInStack = new GridPane();

    GridPane reservationPane = new GridPane();
    BorderPane mainRoot = new BorderPane();
    VBox sidePanel = new VBox();


    Reservation(){
//        this.reservationPane = pane;
    }

    Reservation(BorderPane mainPane) throws FileNotFoundException {
        mainRoot = mainPane;
        mainRoot.getChildren().clear();
        sidePanel.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 10% 50%, rgba(0,255,255,0.26), rgba(0,63,139,0.45))");
        sidePanel.setPrefWidth(400);
        sidePanel.setPadding(new Insets(100, 20, 50, 50));
        sidePanel.setSpacing(30);
//        mainRoot.setLeft(sidePanel);
        mainRoot.setCenter(reservationPane);

//        reservationPane.setPadding(new Insets(50, 100, 10, 50));
        reservationPane.setHgap(70);
        reservationPane.setVgap(10);
        reservationPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 50% 50%, #FFE4C4, #40E0D0)");

    }
    /*
     * this interface will give option about weather he would like to do a
     * 1-> new Reservation
     * 2 -> modify one
     * 3 -> Delete one */

    public void mainInterface() throws FileNotFoundException {
        //setting up the pane for the entry window
        reservationPane.getChildren().clear();
        sidePanel.getChildren().clear();
        reservationPane.setPadding(new Insets(100, 100, 10, 50));
        mainRoot.setLeft(sidePanel);

        HBox hBox = new HBox();
        hBox.getChildren().clear();
        gridInStack.getChildren().clear();
        hBox.getChildren().add(gridInStack);
        gridInStack.setPadding(new Insets(50, 50, 50, 100));
        gridInStack.setVgap(20);
        gridInStack.setHgap(20);
        StackPane stackPane = new StackPane();
        stackPane.setEffect(new DropShadow(10, Color.PURPLE));

        stackPane.setStyle("-fx-background-color: rgb(255, 255, 255)");
        stackPane.setPrefSize(700,550);
        stackPane.getChildren().add(hBox);
        reservationPane.add(stackPane, 0, 0);


        newEntry.setText("New Reservation");
        showData.setText("Show Reservations");
        modifyEntry.setText("Modify Reservation");
        deletion.setText("Delete Reservation");
        showOldData.setText("Show deleted Reservations");

        back.setPrefSize(70, 12);
        newEntry.setPrefSize(300, 10);
        showData.setPrefSize(300, 10);
        modifyEntry.setPrefSize(300, 10);
        deletion.setPrefSize(300, 10);
        showOldData.setPrefSize(300, 10);

        //adding lgos to the UI
        mainLogos();

        //adding components to the pane
        gridInStack.add(newEntry, 3, 0);
        gridInStack.add(deletion, 3, 1);
        gridInStack.add(modifyEntry, 3, 2);
        gridInStack.add(showData, 3, 4);
        gridInStack.add(showOldData, 3, 5);
        gridInStack.add(back, 3, 10);

        //action listeners for the buttons
        newEntry.setOnAction(actionEvent -> newEntry());
        modifyEntry.setOnAction(actionEvent -> modifyEntry());
        deletion.setOnAction(e -> deleteEntryInterface());
        showData.setOnAction(e -> {
            mainRoot.getChildren().remove(sidePanel);
            showTable(tableNameOfReservedRooms);});
        showOldData.setOnAction(e -> {
            mainRoot.getChildren().remove(sidePanel);
            showTable(tableNameOfOldReservations);
        });
        back.setOnAction(e -> {
            try {
                startScreen(mainRoot);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }

  /* This method is used to insert logos that
            we see on
            Reservation pane
         */

    public void mainLogos() throws FileNotFoundException {

        Image logo3 = new Image(new FileInputStream("logos/reserve.png"));
        ImageView addReservationLogo = new ImageView(logo3);
        addReservationLogo.setFitHeight(40);
        addReservationLogo.setFitWidth(40);

        Image logo4 = new Image(new FileInputStream("logos/show.png"));
        ImageView showTableLogo1 = new ImageView(logo4);
        showTableLogo1.setFitHeight(40);
        showTableLogo1.setFitWidth(40);

        Image logo5 = new Image(new FileInputStream("logos/delete.png"));
        ImageView deleteLogo = new ImageView(logo5);
        deleteLogo.setFitHeight(40);
        deleteLogo.setFitWidth(40);

        Image logo6 = new Image(new FileInputStream("logos/edit.png"));
        ImageView editLogo = new ImageView(logo6);
        editLogo.setFitHeight(40);
        editLogo.setFitWidth(40);

        Image logo7 = new Image(new FileInputStream("logos/table.png"));
        ImageView showTableLogo2 = new ImageView(logo7);
        showTableLogo2.setFitHeight(40);
        showTableLogo2.setFitWidth(40);


        Image logo8 = new Image(new FileInputStream("logos/avatar.png"));
        ImageView welcomeLogo = new ImageView(logo8);
        welcomeLogo.setFitHeight(200);
        welcomeLogo.setFitWidth(200);

        Label welcomeInfo = new Label();
        welcomeInfo.setText("Welcome to the rooms reservation center\n" +
                "You can deal with reservations here");
        welcomeInfo.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));

        gridInStack.add(addReservationLogo, 2,  0);
        gridInStack.add(deleteLogo, 2,  1);
        gridInStack.add(editLogo,  2,  2);
        gridInStack.add(showTableLogo1, 2,  4);
        gridInStack.add(showTableLogo2, 2,  5);
        welcomeLogo.setTranslateX(20);
        sidePanel.getChildren().add(welcomeLogo);
        sidePanel.getChildren().add(welcomeInfo);

    }


    /*
     * This function will be called when ever
     * a user want to add a reservation
     *--> the user will input the customer info in the user entry interface
     * --> then we wil save it the data base table and will show notification
     * for successful saving of data
     *  */
    public void newEntry(){
//        initReservableRooms();//intializing the reservable Rooms array
        selectedRoom = "";
        userEntryInterface();// making an interface for the data entry
        roomsDropBox();//shows the selected room for the process in this case
        /*
         * if there is successful saving then initialize reservable rooms array
         * so that combo box will be refreshed*/
        enterButton.setOnAction(actionEvent -> {
            if(checking()){
                if (alreadyReserved()){
                    if (safeEntryFromCustomersSide()) {
                        if (safeEntry(false)) {
                            reservationNumber();
                            savingEntry();
                        }
                    }
                }
                else if (!alreadyReserved()){
                    reservationNumber();//making a reservation Number for the room
                    savingEntry();
                    newEntry();
                }
            }
        });
        //first checking the data then saving it
        back.setOnAction(actionEvent -> {
            try {
                mainInterface();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    /*this will help in modifying a reservation
     * --> first interface will be made to get input for the room number to be modified
     * --> then we will search the required room in the database table
     * --> then user will modify the data in  user entry interface
     * --> last we gonna save the updated data*/
    public void modifyEntry(){
//        initReservableRooms();
        targetInputInterface("Enter the Reservation Number you want to modify");
        back.setOnAction(e -> {
            try {
                mainInterface();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        enterButton.setOnAction(e -> {
            //showing the warning Screen on wrong entry
            if (entryText.getText().isBlank() || isNotInteger(entryText.getText())){
                warningStage("Please enter the right reservation number!", "Warning");
            }
            else modifyingReservedRoomInterface();
        });

        refreshEntry();
    }
    /*
     * --> user will enter the required reservation number to be deleted
     * --> then window will be appeared after successful deletion after checking thr
     * room in the database
     * */
    public  void deleteEntryInterface(){
        reservationPane.getChildren().clear();
        entryText.setText("");
        reservationPane.add(entryText,  1,  1);
        reservationPane.add(enterButton,  1,  2);
        reservationPane.add(back,  2,  4);
        back.setOnAction(e -> {
            try {
                mainInterface();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        /*
         * the room will only be deleted if it is not present in reservedRooms
         * this mean this room have been reserved
         * otherwise warning will be shown indicating that this room have not been reserved yet
         * */
        enterButton.setOnAction(e -> deletionChecking());
    }

    public void deletionChecking(){
        String[] data = searchingDataFromDatabase();
        String war = "Are you sure you want to delete Reservation Number " + entryText.getText()
                + "\nName : " + data[0]
                + "\nPhNum : " + data[1];

        if (entryText.getText().isBlank() || isNotInteger(entryText.getText())){
            warningStage("Please enter the correct Reservation Number", "Warning");
        }
        else {
            if (dataExist("reservationNumber", tableNameOfReservedRooms)) {
                warning_R_Deletion(war, "Warning", "No", "Yes");
            } else {
                warningStage("NO such reservation found", "Warning");
            }
        }
    }

    @Override
    public void handle_R_Deletion() throws SQLException {
        connection.close();
        DatabaseDeletion processing = new DatabaseDeletion();
        processing.movingRecordToOtherTable(tableNameOfOldReservations, tableNameOfReservedRooms, "reservationNumber", entryText);
        processing.deletingRecord(tableNameOfReservedRooms, "reservationNumber", entryText);
        warningStage("Room is successfully deleted!", "Status");
        connection = DatabaseConnection.connector();
    }

    /*
     * the purpose of this finction is that
     * --> we only gonna delete and move the data from the table if it exists
     * */
    public boolean dataExist(String targetField, String tableToCheck){
        /*target field is the field on the basis of
         * which we gonna check the data*/
        String data = "";
        PreparedStatement stmt;
        String query =  "SELECT "+ targetField +" FROM " + tableToCheck + " WHERE "+ targetField + " = " + entryText.getText();
        try {
            stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                data = rs.getString(targetField);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !data.isBlank();
    }
    /*
     * we gonna use the javafx table view here
     * for showing the reserved rooms data from
     * the database
     * */
    private void showTable(String tableName){
        reservationPane.getChildren().clear();
        reservationPane.setPadding(new Insets(50, 50, 50,  50));

        /*
         * making a scrollable interface for teh data base table*/
        TableView table = makingTableViewForReservationClass(tableName);
        table.setPrefSize(1500, 1000);

        reservationPane.add(table, 0, 0);
        reservationPane.add(back,  0, 10);
        back.setOnAction(e -> {
            try {
                mainInterface();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }
    /*
     * so we are making a booking number generator here
     * --> it will make a unique reservation number
     * */

    private void reservationNumber(){
        PreparedStatement stmt;
        String query = "SELECT * FROM  R_NumGenrator";
        try {
            stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                reservationNumber = Integer.parseInt(rs.getString("rNumber"));
                stmt.close();
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        reservationNumber++;
        update_Rnum_Database();
    }

    public void update_Rnum_Database(){
        PreparedStatement stmt;
        String query = "UPDATE R_NumGenrator SET rNumber = ?";
        try{
            stmt = connection.prepareStatement(query);
            stmt.setString(1, String.valueOf(reservationNumber));
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /*
     * if a room is not reserved yet
     * then pop it up on top in the combo Box
     * while entry of new reservation*/

    private void findingUnReservedRoom(char roomType, String[] roomsArr){
        notReservedYet = "1A";
        PreparedStatement stmt;
        int countRoom = 1;
        while (countRoom <= roomsArr.length){
            String query = "SELECT roomNumber FROM reservedRooms WHERE roomNumber = ?";
            try {
                stmt = connection.prepareStatement(query);
                stmt.setString(1, String.valueOf(countRoom) + roomType);
                ResultSet rs = stmt.executeQuery();
                //if the room number not exists in the reserved rooms table then break
                if (!rs.next()){
                    notReservedYet = String.valueOf(countRoom) + roomType;
                    System.out.println(notReservedYet + "is not reserved");
                    break;
                }
                countRoom++;
                rs.close();
                stmt.close();
            }
            catch (SQLException e){
                System.out.println(e);
            }
        }
    }

    /*
     * this code will make an interface that will help
     * user for inputting the customer data
     * --> we will use this method when ever we will be trying
     * to saving or updating customer data */

    public void userEntryInterface(){
        reservationPane.getChildren().clear();

        //labels for showing things necessary for the enrolment
        reservationPane.setPadding(new Insets(50, 100, 10, 50));


        clientDetails.setFont(Font.font("Montserrat", FontWeight.NORMAL, 23));
        Line line = new Line();
        //Setting the properties to a line
        line.setStartX(100.0);
        line.setStartY(150.0);
        line.setEndX(300.0);
        line.setEndY(150.0);
//        reservationPane.setGridLinesVisible(true);

        Label name = new Label("Name");
        Label phNum = new Label("Contact Number");
        Label idNum = new Label("PASSPORT/ID Number");
        Label city = new Label("City");
        enterButton.setText("Submit");
        status.setText("PLease enter all the information correctly!");
        status.setTextFill(Color.WHITE);


        //textFields for filling all the data
        //adding components to the pane
        reservationPane.add(clientDetails, 0, 0);
        reservationPane.add(line, 0, 1);
        reservationPane.add(name, 0, 2);
        reservationPane.add(nameEntry, 1, 2);
        reservationPane.add(phNum, 0, 3);
        reservationPane.add(phNumEntry, 1, 3);
        reservationPane.add(idNum, 0, 4);
        reservationPane.add(idEntry, 1, 4);
        reservationPane.add(city, 0, 5);
        reservationPane.add(cityEntry, 1, 5);
        reservationPane.add(arrival, 0, 8);
        reservationPane.add(checkInPicker, 1, 8);
        reservationPane.add(departure, 0, 9);
        reservationPane.add(checkOutPicker, 1, 9);

        reservationPane.add(status, 0,  10 );
        reservationPane.add(enterButton, 0,  12 );
        reservationPane.add(back, 2,  14);

        roomTypeSelection();
    }


    /* Selection of
       Room type such as Single , Double , Triple
       Seat Rooms we gonna make a radio button toggle that will help us in selecting the
       specific room type
     */

    public void roomTypeSelection(){

        sidePanel.getChildren().clear();
        HBox roomSeats = new HBox();
        roomSeats.setSpacing(20);
        singleRoom =  new RadioButton("Single Seat");
        doubleRoom =  new RadioButton("Double Seat");
        tripleRoom =  new RadioButton("Triple Seat");

        roomSeatsToggle = new ToggleGroup();

        singleRoom.setToggleGroup(roomSeatsToggle);
        doubleRoom.setToggleGroup(roomSeatsToggle);
        tripleRoom.setToggleGroup(roomSeatsToggle);
        //setting default selection
        singleRoom.setSelected(true);

        roomSeats.getChildren().add(singleRoom);
        roomSeats.getChildren().add(doubleRoom);
        roomSeats.getChildren().add(tripleRoom);
        System.out.println("Hey i am here..!");

        roomToggleListener();

        sidePanel.getChildren().add(roomSeats);

    }

    public void roomToggleListener(){

        roomSeatsToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                dropBox.setItems(FXCollections.observableArrayList(getSelectedRoomArray()));
                findingUnReservedRoom(getSelectedRoomChar(), getSelectedRoomArray());
                dropBox.setValue(notReservedYet);

            }
        });

    }

    public String[] getSelectedRoomArray(){
        if (getSelectedRoomRadio().equals(singleRoom)){
            return singleRooms();
        }
        else if (getSelectedRoomRadio().equals(doubleRoom)){
            return doubleRooms();
        }
        else return tripleRooms();
    }

    public char getSelectedRoomChar(){
        if (getSelectedRoomRadio().equals(singleRoom)){
            return 'A';
        }
        else if (getSelectedRoomRadio().equals(doubleRoom)){
            return 'B';
        }
        else return 'C';
    }


    public RadioButton getSelectedRoomRadio(){
         return ((RadioButton)roomSeatsToggle.getSelectedToggle());
    }
    /**
     * side  pane for the new Reservation Interface
     * showing som extra information about the room no
     * going to be reserved
     * we gonna use this method for
     * two diff processing that is why we
     * are using a boolean value here
     * */

    public void roomsDropBox(){
        //in this case this side pane area contain the drop box for the room
        Label label = new Label("Recommended Un-Reserved Room");
        label.setFont(Font.font("Montserrat", FontWeight.NORMAL, 13));

        dropBox = new ComboBox<>(FXCollections.observableArrayList(getSelectedRoomArray()));

        findingUnReservedRoom(getSelectedRoomChar(), getSelectedRoomArray());
        dropBox.setValue(notReservedYet);

        selectedRoom = dropBox.getValue();
        dropBox.setOnAction(actionEvent -> selectedRoom = dropBox.getValue());

        dropBox.setPrefWidth(250);
        sidePanel.getChildren().add(label);
        sidePanel.getChildren().add(dropBox);

    }

    /**
     * making an inter face for getting input from the
     * user for which reserved room he want to
     * modify
     * */
    public void targetInputInterface(String textToShow){

        entryText.setText("");
        Label text = new Label(textToShow);
        text.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));
//        entryText.setPromptText("Enter the reservation number of reserved room");
//        entryText.setFocusTraversable(false);
        entryText.setPrefSize(250, 8);
        entryText.setPrefWidth(200);

        reservationPane.setPadding(new Insets(100, 200, 200, 300));
        reservationPane.getChildren().clear();
        reservationPane.add(text, 0, 0);
        reservationPane.add(entryText, 0, 3);
        reservationPane.add(enterButton, 0, 5);
        reservationPane.add(back,  0, 10);
    }

    private void modifyingReservedRoomInterface(){
        String[] data;//temp data array for checking if record exists
        data = searchingDataFromDatabase();
        //data[7] contains reservation number
        if ( data[7] == null){
            warningStage("No such room is reserved",  "Try again");
        }
        else{
            userEntryInterface();
            Button newModify = new Button("Update other one");
            newModify.setPrefSize(150,  10);
            reservationPane.add(newModify, 2,  12 );
            showingData();
            enterButton.setOnAction(e -> savingModification());
            //this action listener should be bellow the "enterButton" action listener
            //this is because getting input method will update the "enterButton" action listener
            newModify.setOnAction(e -> modifyEntry());
        }
    }
    private void savingModification(){
        String[] data = searchingDataFromDatabase();
        if (checking()){
            boolean datesNotChanged =
                    checkInPicker.getValue().isEqual(LocalDate.parse(data[4]))
                            &&
                            checkOutPicker.getValue().isEqual(LocalDate.parse(data[5]));
            if (!datesNotChanged){
                if (safeEntryFromCustomersSide()) {
                    if (safeEntry(true)) {
                        savingModifiedData();
                        warningStage("Info is modified successfully!", "Status");
                        modifyingReservedRoomInterface();
                    }
                }
            }
            if (datesNotChanged){
                savingModifiedData();
                warningStage("Info is modified successfully!",  "Status");
                modifyingReservedRoomInterface();
            }
        }
    }

    //making a method that will show the data of the required room
    private void showingData(){

        Label headingLabelForPreviousData = new Label("Previous Data");
        headingLabelForPreviousData.setTextFill(Color.DEEPSKYBLUE);
        headingLabelForPreviousData.setFont(Font.font("Montserrat", FontWeight.NORMAL, 25));
        String[] data = searchingDataFromDatabase();

        Label previousDataLabel = new Label();
        previousDataLabel.setText("Name:\t\t\t\t" + data[0] + "\n" +
                "Phone Number:\t\t" + data[1] + "\n" +
                "ID:\t\t\t\t\t" + data[2] + "\n" +
                "City:\t\t\t\t\t" + data[3] + "\n" +
                "Arrival Date:\t\t\t" + data[4] + "\n" +
                "Departure Date:\t\t" + data[5] + "\n" +
                "Room Number:\t\t" + data[6] + "\n" +
                "Reservation Number:\t\t " + data[7]
        );
        previousDataLabel.setFont(Font.font("Montserrat", FontWeight.BOLD, 13));
        //this will show the previous data in the testFields
        LocalDate checkIn = LocalDate.parse(data[4]);
        LocalDate checkOut = LocalDate.parse(data[5]);
        nameEntry.setText(data[0]);
        phNumEntry.setText(data[1]);
        idEntry.setText(data[2]);
        cityEntry.setText(data[3]);
        checkInPicker.setValue(checkIn);
        checkOutPicker.setValue(checkOut);
        currentRoomNumber = data[6];

        /*
         * showing the previous data*/
        roomsDropBox();
        dropBox.setValue(currentRoomNumber);
        selectedRoom = currentRoomNumber;
        modifyRoomToggle(currentRoomNumber);

        sidePanel.getChildren().add(headingLabelForPreviousData);
        sidePanel.getChildren().add(previousDataLabel);
    }

    public void modifyRoomToggle(String roomNumber){

        if (roomNumber.charAt(1) == 'A')getSingleRoom().setSelected(true);
        else if (roomNumber.charAt(1) == 'B')getDoubleRoom().setSelected(true);
        else getTripleRoom().setSelected(true);
    }


    /*
    updating the reserved room table in the database
    * by the following method*/
    private void savingModifiedData(){
        PreparedStatement statement;

        String query = "UPDATE " + tableNameOfReservedRooms + " SET " +
                "name = ?, " +
                "phoneNumber =?, " +
                "id = ?, " +
                "city = ?, " +
                "arrivalDate = ?, " +
                "departureDate = ?, " +
                "roomNumber = ?" +
                " WHERE reservationNumber = " + entryText.getText();
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, nameEntry.getText());
            statement.setString(2, phNumEntry.getText());
            statement.setString(3, idEntry.getText());
            statement.setString(4, cityEntry.getText());
            statement.setString(5, checkInPicker.getValue().toString());
            statement.setString(6, checkOutPicker.getValue().toString());
            statement.setString(7, selectedRoom);
            statement.executeUpdate();
            statement.close();
            refreshEntry();
            status.setText("The data has been updated successfully!");
            status.setTextFill(Color.DEEPSKYBLUE);
            status.setFont(new Font("Arial", 13));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }


    }

    /*
    checker method for checking the room we gonna reserve should not be already reserved
    * */
    public boolean alreadyReserved(){

        String tempRoom = "";
        PreparedStatement stmt;
        String query = "SELECT roomNumber FROM reservedRooms WHERE roomNumber = ?";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, selectedRoom);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                tempRoom = rs.getString("roomNumber");
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // temp room wil only gonna be blank if we have not
        //reserved it yet, then it will return false other wise true
        return !tempRoom.isBlank();
    }

    /* Verification if the
       other rooms is
       Reserved or occupied at given date
    */
    private boolean safeEntry(boolean modificationMode){
        //if modificationMode is on then it gonna check the dates for the modifying room
        PreparedStatement stmt;
        LocalDate checkIn = LocalDate.of(2020, 1, 8);
        LocalDate checkOut = LocalDate.of(2020, 1, 8);
        boolean notClearEntry = false;
        String query = "SELECT arrivalDate, departureDate, reservationNumber FROM reservedRooms WHERE roomNumber = ?";
        if (modificationMode){
            query = "SELECT arrivalDate, departureDate, reservationNumber FROM reservedRooms WHERE (roomNumber = ?" + ") AND (reservationNumber != " + entryText.getText() +")";
        }
        try {
            String warningStaement = "";
            stmt = connection.  prepareStatement(query);
            stmt.setString(1, selectedRoom);
            ResultSet rs = stmt.executeQuery();
            /*if the check in date of the required room will be
             * bw the check in and out date of
             * other reservations of that room --> then it gonna make an error
             * otherwise--> entry will be saved */
            while (rs.next()) {
                checkIn = LocalDate.parse(rs.getString("arrivalDate")) ;
                checkOut = LocalDate.parse(rs.getString("departureDate"));

                /*
                 * we are verifying that selected time period
                 * -->  should not be in between any other time period
                 * --> and also not contain any other time period in itself
                 *  */
                notClearEntry = (
                        (
                                ((checkInPicker.getValue().isAfter(checkIn) || checkInPicker.getValue().isEqual(checkIn))
                                        && (checkInPicker.getValue().isBefore(checkOut)))
                                        ||
                                        ((checkOutPicker.getValue().isAfter(checkIn))
                                                && (checkOutPicker.getValue().isBefore(checkOut) || checkOutPicker.getValue().isEqual(checkOut)))
                        )
                                ||
                                (
                                        ((checkInPicker.getValue().isBefore(checkIn) || checkInPicker.getValue().isEqual(checkIn))
                                                && (checkOutPicker.getValue().isAfter(checkOut)))
                                )
                );
                String reservationNo = rs.getString("reservationNumber");
                if (notClearEntry) {
                    warningStaement = "This room is already reserved from " + checkIn + " to " + checkOut + "\nBy Reservation No " + reservationNo +"\n" +
                            "Please change the room number or the Date";
                    warningStage(warningStaement, "Warning");
                    break;
                }
                rs.close();
                stmt.close();
            }


        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if(!notClearEntry) {
            return true;
        }
        else return false;
    }


    /*
     * this gonna check that if the reservation is not messing uo with the occupied rooms*/
    private boolean safeEntryFromCustomersSide(){
        //if modificationMode is on then it gonna check the dates for the modifying room
        PreparedStatement stmt;

        DateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
        LocalDate checkOut;
        LocalDate checkIn;
        boolean notClearEntry = false;
        String query = "SELECT arrivalDate, departureDate, customerNumber FROM newCustomers WHERE roomNumber = ?";
        try {
            String warningStatement = "";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, selectedRoom);
            ResultSet rs = stmt.executeQuery();
            /*if the check in date of the required room will be
             * bw the check in and out date of
             * occupied room --> then it gonna make an error
             * otherwise --> entry will be saved */
            while (rs.next()) {

                //saving the arrival and departure dates of the customer in from stings to Local Dates
                Date parseIt = format.parse(rs.getString("arrivalDate"));
                checkIn = parseIt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                checkOut = LocalDate.parse(rs.getString("departureDate"));
                /*
                 * we are verifying that selected time period
                 * -->  should not be in between any other occuppied time period of customer
                 * --> and also not contain any other time period in itself
                 *  */
                notClearEntry = (
                        (
                                ((checkInPicker.getValue().isAfter(checkIn) || checkInPicker.getValue().isEqual(checkIn))
                                        && (checkInPicker.getValue().isBefore(checkOut)))
                                        ||
                                        ((checkOutPicker.getValue().isAfter(checkIn))
                                                && (checkOutPicker.getValue().isBefore(checkOut) || checkOutPicker.getValue().isEqual(checkOut)))
                        )
                                ||
                                (
                                        ((checkInPicker.getValue().isBefore(checkIn) || checkInPicker.getValue().isEqual(checkIn))
                                                && (checkOutPicker.getValue().isAfter(checkOut)))
                                )
                );
                String customerNo = rs.getString("customerNumber");
                if (notClearEntry) {
                    warningStatement = "This room is occupied from " + checkIn + " to " + checkOut + "\nBy Customer No " + customerNo +"\n" +
                            "Please change the Room number or the Date";
                    warningStage(warningStatement, "Warning");
                    break;
                }
                rs.close();
                stmt.close();
            }
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return !notClearEntry;
    }

    /**
     * searching for the reserved room by the room number
     * from the data base*/
    public String[] searchingDataFromDatabase(){
        /*Saving the data in the form of the String array and then
         * showing it to the user*/

        String[] data = new String[8];
        //this will help in checking a room exist or not on given reservation
        PreparedStatement stmt;
        String query = "SELECT * FROM reservedRooms WHERE reservationNumber = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, entryText.getText());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                data[0] = rs.getString("name");
                data[1] = rs.getString("phoneNumber");
                data[2] = rs.getString("id");
                data[3] = rs.getString("city");
                data[4] = rs.getString("arrivalDate");
                data[5] = rs.getString("departureDate");
                data[6] = rs.getString("roomNumber");
                data[7] = rs.getString("reservationNumber");
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
    Saving the  user entry information to the Data Base*/

    private void savingEntry(){
        PreparedStatement statement;

        String query = "INSERT INTO reservedRooms VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1,  nameEntry.getText());
            statement.setString(2,  phNumEntry.getText());
            statement.setString(3,  idEntry.getText());
            statement.setString(4,  cityEntry.getText());
            statement.setString(5,  checkInPicker.getValue().toString());
            statement.setString(6,  checkOutPicker.getValue().toString());
            statement.setString(7, selectedRoom );
            statement.setString(8, String.valueOf(reservationNumber));
            statement.executeUpdate();
            statement.close();
            refreshEntry();
            String status = "The data have been submitted successfully!\n" +
                    "The reservation number is " + reservationNumber;
            warningStage(status, "Status");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }


    /*
     * helps in refreshing the user entry interface after every
     * process*/
    public void refreshEntry(){
        nameEntry.setText("");
        phNumEntry.setText("");
        idEntry.setText("");
        cityEntry.setText("");
        checkInPicker.setValue(null);
        checkOutPicker.setValue(null);
        status.setTextFill(Color.BLACK);
        sidePanel.getChildren().clear();
    }
    /*checking that if the data is enters correctly
     * helps in the user entry interface method*/
    public boolean checking(){
        boolean temp = nameEntry.getText().isBlank()
                ||  phNumEntry.getText().isBlank() || isNotInteger(phNumEntry.getText())
                || idEntry.getText().isBlank() || (isNotInteger(idEntry.getText()))
                || cityEntry.getText().isBlank()
                || checkInPicker.getValue().toString().isBlank()
                || checkOutPicker.getValue().toString().isBlank();
        /*the data will only be saved ont if none of the
         * area is blank*/
        if (!temp){return true;}
        else {
            Toolkit.getDefaultToolkit().beep();
            status.setText("Please enter the data in correct form!");
            status.setTextFill(Color.web("#FF0000"));
            return false;
        }
    }

    /*method to check the integer
     * helps in the checking method*/
    public  boolean isNotInteger(String s) {
        try {
            Long.parseLong(s);
//            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return true;
        }
        // only got here if we didn't return false
        return false;
    }


    public Connection getConnection() {
        return connection;
    }

    /**
     * making a waring buttons for
     * diff actions performed by the
     * user accidently*/


    public Button getNewEntry() {
        return newEntry;
    }

    public Button getShowData() {
        return showData;
    }

    public Button getShowOldData() {
        return showOldData;
    }

    public Button getDeletion() {
        return deletion;
    }

    public Button getModifyEntry() {
        return modifyEntry;
    }

    public Button getEnterButton() {
        return enterButton;
    }

    public Button getBack() {
        return back;
    }

    public Label getStatus() {
        return status;
    }

    public TextField getNameEntry() {
        return nameEntry;
    }

    public TextField getPhNumEntry() {
        return phNumEntry;
    }

    public TextField getIdEntry() {
        return idEntry;
    }

    public TextField getCityEntry() {
        return cityEntry;
    }

    public DatePicker getCheckInPicker() {
        return checkInPicker;
    }

    public DatePicker getCheckOutPicker() {
        return checkOutPicker;
    }

    public Label getArrival() {
        return arrival;
    }

    public Label getDeparture() {
        return departure;
    }

    public TextField getEntryText() {
        return entryText;
    }

    public Label getClientDetails() {
        return clientDetails;
    }

    public ToggleGroup getRoomSeatsToggle() {
        return roomSeatsToggle;
    }

    public ComboBox<String> getDropBox() {
        return dropBox;
    }

    public RadioButton getSingleRoom() {
        return singleRoom;
    }

    public RadioButton getDoubleRoom() {
        return doubleRoom;
    }

    public RadioButton getTripleRoom() {
        return tripleRoom;
    }

    public VBox getSidePanel() {
        return sidePanel;
    }

    public BorderPane getMainRoot() {
        return mainRoot;
    }

    public GridPane getReservationPane() {
        return reservationPane;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
