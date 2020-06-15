package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class Invoice extends Customer {

    private long daysSpent;
    private double singeleRate = 1000;
    private double doubleRate = 1000;
    private double tripleRate = 1000;
    private HBox hBox = new HBox();

    /*
    * this class gonna access from two diff classes thats why we have
    * to make this boolean for changig behavior of the back button for two diff
    *  panes
    * */
    private boolean callFromStart;


    private TextField single_T = new TextField();
    private TextField double_T = new TextField();
    private TextField triple_T = new TextField();


//    GridPane reservationPane = new GridPane();
    Invoice(BorderPane root){
        mainRoot = root;
        root.setCenter(reservationPane);
        reservationPane.setPadding(new Insets(200, 100, 10, 500));
        reservationPane.setHgap(70);
        reservationPane.setVgap(10);
        reservationPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 50% 50%, #FFE4C4, #40E0D0)");

    }

    /*
    * invoice method will help us in making an
    * interface that will give us a invoice report
    * */
    public void invoice() throws ParseException, FileNotFoundException {
        String[] data = gettingCustomerDataFromDatabase();

        reservationPane.getChildren().clear();
        hBox.getChildren().clear();
        GridPane gridInStack = new GridPane();
        hBox.getChildren().add(gridInStack);
        gridInStack.setPadding(new Insets(50, 50, 50, 100));
        gridInStack.setVgap(20);
        gridInStack.setHgap(20);
        StackPane stackPane = new StackPane();
        stackPane.setEffect(new DropShadow(10, Color.PURPLE));

        stackPane.setStyle("-fx-background-color: rgb(255, 255, 255)");
        stackPane.setPrefSize(1500,1500);
        stackPane.getChildren().add(hBox);
        reservationPane.add(stackPane, 0, 0);

        Line line = new Line();
        //Setting the properties to a line
        line.setStartX(100.0);
        line.setStartY(150.0);
        line.setEndX(300.0);
        line.setEndY(150.0);

        addingLogo();

        Label roomsName = new Label("Name: ");
        roomsName.getStylesheets().add("sample/style.css");
        Label phNum = new Label("Phone Number: ");
        phNum.getStylesheets().add("sample/style.css");
        Label roomsNum = new Label("Room Number: ");
        roomsNum.getStylesheets().add("sample/style.css");
        Label roomType = new Label("Rooms Type: ");
        roomType.getStylesheets().add("sample/style.css");
        Label roomsFee = new Label("Rent For One Day: ");
        roomsFee.getStylesheets().add("sample/style.css");
        Label days = new Label("Days Spent");
        days.getStylesheets().add("sample/style.css");
        Label totRent = new Label("Total Rent: ");
        totRent.getStylesheets().add("sample/style.css");



        Label roomsName_txt = new Label();
        roomsName_txt.getStylesheets().add("sample/style.css");
        Label phNum_txt = new Label();
        phNum_txt.getStylesheets().add("sample/style.css");
        Label roomsNum_txt = new Label();
        roomsNum_txt.getStylesheets().add("sample/style.css");
        Label roomsType_txt = new Label( );
        roomsType_txt.getStylesheets().add("sample/style.css");
        Label roomsFee_txt = new Label();
        roomsFee_txt.getStylesheets().add("sample/style.css");
        Label days_txt = new Label();
        days_txt.getStylesheets().add("sample/style.css");
        Label totRent_txt = new Label();
        totRent_txt.getStylesheets().add("sample/style.css");

        calculateDays();
        roomsName_txt.setText(data[1]);
        phNum_txt.setText(data[2]);
        roomsNum_txt.setText(data[0]);
        roomsType_txt.setText(getRoomsType());
        roomsFee_txt.setText(String.valueOf(getRoomsRent()));
        days_txt.setText(String.valueOf(daysSpent));
        totRent_txt.setText(String.valueOf(daysSpent * getRoomsRent()));

        gridInStack.add(roomsName, 0, 0);
        gridInStack.add(phNum, 0, 1);
        gridInStack.add(roomsNum, 0, 2);
        gridInStack.add(roomType, 0, 3);
        gridInStack.add(days, 0, 4);
        gridInStack.add(roomsFee, 0, 5);
        gridInStack.add(line, 0, 7);
        gridInStack.add(totRent, 0, 9);

        gridInStack.add(roomsName_txt, 1, 0);
        gridInStack.add(phNum_txt, 1, 1);
        gridInStack.add(roomsNum_txt, 1, 2);
        gridInStack.add(roomsType_txt, 1, 3);
        gridInStack.add(days_txt, 1, 4);
        gridInStack.add(roomsFee_txt, 1, 5);
        gridInStack.add(totRent_txt, 1, 9);

        Button deleteCustomer = new Button("Delete this Customer");
        deleteCustomer.setPrefWidth(150);
        reservationPane.add(getBack(), 0, 2);
        reservationPane.add(deleteCustomer, 0, 3);
        getBack().setOnAction(e -> entryInterface(callFromStart));
        deleteCustomer.setOnAction(e -> deletionChecking());
//        pane.setGridLinesVisible(true);
//        gridInStack.setGridLinesVisible(true);
    }

    public String getRoomsType(){
        String[] data = gettingCustomerDataFromDatabase();
        if (containsChar(data[0], 'A'))return "Single";
        else if (containsChar(data[0], 'B'))return "Double";
        else return "Triple";
    }

    public double getRoomsRent(){
        String[] data = gettingCustomerDataFromDatabase();
        if (containsChar(data[0], 'A'))return singeleRate;
        else if (containsChar(data[0], 'B'))return doubleRate;
        else return tripleRate;
    }

    /*
    * it will check a specific
    * char in a string
    * */
    public boolean containsChar(String s, char search) {
        if (s.length() == 0)
            return false;
        else
            return s.charAt(0) == search || containsChar(s.substring(1), search);
    }


    /*
    * calculating the days spent by the customer
    * by calculating the arrival and deparure local
    * dates saved in teh database
    * */
    public void calculateDays() throws ParseException {
        String[] data = gettingCustomerDataFromDatabase();
        LocalDate arrival;
        LocalDate departure;

        DateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
        Date parseIt = format.parse(data[10]);
        arrival = parseIt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        departure = LocalDate.parse(data[7]);

        daysSpent = ChronoUnit.DAYS.between(arrival, departure);
    }

    /*
    * making in interface to get the
    * customer number of which we want to
    * genrate a report
    * */
    public void entryInterface(boolean callFromStart){
        reservationPane.getChildren().clear();
        getEntryText().setText("");
        Label text = new Label();
        text.setText("Enter the Customer Number for Invoice");
        text.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));

        reservationPane.setPadding(new Insets(100, 200, 200, 100));

        reservationPane.add(text, 1, 0);
        reservationPane.add(getEntryText(), 1, 1);
        reservationPane.add(getEnterButton(), 1, 2);
        reservationPane.add(getBack(), 2, 4);
        /*
        setting up the back button as the function is useed at two pooints in
        * our program
        */
        this.callFromStart = callFromStart;
        if (callFromStart){
            getBack().setOnAction(e -> {
                invoiceOptions();
            });
        }
        else {
            getBack().setOnAction(e -> {
                new Customer(mainRoot).checkOutInterface();
            });
        }
        getEnterButton().setOnAction(e -> {
            try {
                checkingInvoice();
            } catch (ParseException | FileNotFoundException parseException) {
                parseException.printStackTrace();
            }
        });

    }
    /*
    adding the invoice logo the report
    * */
    public void addingLogo() throws FileNotFoundException {

        Image logo = new Image(new FileInputStream("logos/invoice.png"));
        ImageView invoice = new ImageView(logo);
        invoice.setFitHeight(200);
        invoice.setFitWidth(200);

        invoice.setTranslateY(100);
        invoice.setTranslateX(30);
        hBox.setSpacing(20);
        hBox.getChildren().add(invoice);

    }

    /*
    * checking that the customer number entered by the user is correct or wrong
    * or if correct then it should be exist the database table
    * */
    public void checkingInvoice() throws FileNotFoundException, ParseException {
        if (getEntryText().getText().isBlank() || isNotInteger(getEntryText().getText())){
            warningStage("Please enter the correct Customer Number", "Warning");
        }
        else {
            if (dataExist("customerNumber", getTableNameOfNewCustomers())) {
                invoice();
            } else {
                warningStage("No such customer found", "Warning");
            }
        }
    }

    /*
    * making some
    * useful options relates to tehe invoice
    * */

    public void invoiceOptions(){

        reservationPane.setPadding(new Insets(200, 100, 10, 500));
        Label info = new Label("YOU CAN DEAL WITH THE INVOICE HERE");
        info.getStylesheets().add("sample/style.css");

        Button invoiceCheck = new Button("Check Customer Invoice");
        invoiceCheck.setTranslateX(20);
        Button invoiceSet = new Button("Invoice Setting");
        invoiceSet.setTranslateX(20);
        invoiceCheck.setPrefSize(250, 15);
        invoiceSet.setPrefSize(250, 15);

        reservationPane.getChildren().clear();
        reservationPane.add(info, 0, 0);
        reservationPane.add(invoiceCheck, 0, 2);
        reservationPane.add(invoiceSet, 0, 3);
        reservationPane.add(getBack(), 2, 6);


        invoiceSet.setOnAction(e -> {
            try {
                invoiceSetInterface();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        invoiceCheck.setOnAction(e -> {
            entryInterface(true);
        });
        getBack().setOnAction(e -> {
            try {
                startScreen(mainRoot);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }

    /*
    * setting up the rates of the invoice
    * */
    public void invoiceSetInterface() throws FileNotFoundException {

        reservationPane.getChildren().clear();
        reservationPane.setPadding(new Insets(100, 100, 10, 300));
        hBox.getChildren().clear();
        GridPane gridInStack = new GridPane();
        hBox.getChildren().add(gridInStack);
        gridInStack.setPadding(new Insets(50, 50, 50, 100));
        gridInStack.setVgap(20);
        gridInStack.setHgap(20);
        StackPane stackPane = new StackPane();
        stackPane.setEffect(new DropShadow(10, Color.PURPLE));

        stackPane.setStyle("-fx-background-color: rgb(255, 255, 255)");
        stackPane.setPrefSize(1000,600);
        stackPane.getChildren().add(hBox);
        reservationPane.add(stackPane, 0, 0);

        Line line = new Line();
        //Setting the properties to a line
        line.setStartX(100.0);
        line.setStartY(150.0);
        line.setEndX(300.0);
        line.setEndY(150.0);

        addSettingLogo();

        Label type = new Label("RoomsType");
        type.getStylesheets().add("sample/style.css");
        type.setTextFill(Color.BLUE);
        Label rate = new Label("Rate Per Day");
        rate.getStylesheets().add("sample/style.css");
        rate.setTextFill(Color.BLUE);

        Label single_L = new Label("Single Seat Rooms");
        Label double_L = new Label("Double Seat Rooms");
        Label triple_L = new Label("Triple Seat Rooms");

        single_L.getStylesheets().add("sample/style.css");
        double_L.getStylesheets().add("sample/style.css");
        triple_L.getStylesheets().add("sample/style.css");


        settingInitialRates();

        single_T.setText(String.valueOf(singeleRate));
        double_T.setText(String.valueOf(doubleRate));
        triple_T.setText(String.valueOf(tripleRate));

        gridInStack.add(type, 0, 0);
        gridInStack.add(rate, 1, 0);

        gridInStack.add(line, 0, 1);

        gridInStack.add(single_L, 0, 3);
        gridInStack.add(double_L, 0, 4);
        gridInStack.add(triple_L, 0, 5);

        gridInStack.add(single_T, 1, 3);
        gridInStack.add(double_T, 1, 4);
        gridInStack.add(triple_T, 1, 5);

        gridInStack.add(getEnterButton(), 1, 7);
        gridInStack.add(getBack(), 1, 8);

        getEnterButton().setOnAction(e ->checkingRates());
        getBack().setOnAction(e -> invoiceOptions());
    }

    public void updatingRates(){
        PreparedStatement stmt;
        String query = "UPDATE roomRate SET " +
                "single = ?, " +
                "double = ?, " +
                "triple = ?";
//                "DESC FETCH FIRST ROWS ONLY";

        try{
            stmt = getConnection().prepareStatement(query);
            stmt.setString(1, single_T.getText());
            stmt.setString(2, double_T.getText());
            stmt.setString(3, triple_T.getText());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //adding logos to the pane

    private void addSettingLogo() throws FileNotFoundException {
        Image logo = new Image(new FileInputStream("logos/seting.png"));
        ImageView setting = new ImageView(logo);
        setting.setFitHeight(200);
        setting.setFitWidth(200);

        setting.setTranslateY(100);
        setting.setTranslateX(30);
        hBox.setSpacing(20);
        hBox.getChildren().add(setting);

    }

    public void settingInitialRates(){
        PreparedStatement stmt;
        String query = "SELECT * FROM roomRate";
        try{
            stmt = getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                singeleRate = Integer.parseInt(rs.getString("single"));
                doubleRate = Integer.parseInt(rs.getString("double"));
                tripleRate = Integer.parseInt(rs.getString("triple"));
                stmt.close();
                rs.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void checkingRates(){
        if (single_T.getText().isBlank() || double_T.getText().isBlank() || triple_T.getText().isBlank()
            || isNotDouble(single_T.getText()) || isNotDouble(double_T.getText()) || isNotDouble(triple_T.getText())){
            warningStage("Please Input Correct Data", "Warning");
        }
        else{
            updatingRates();
            warningStage("Data Successfully Upated", "Status");
        }
    }

    /*method to check the rate
     * helps in the checking method*/
    public  boolean isNotDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch(NumberFormatException | NullPointerException e) {
            return true;
        }
        // only got here if we didn't return false
        return false;
    }

}
