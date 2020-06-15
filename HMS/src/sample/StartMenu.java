package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;



public class StartMenu extends Application{


    private ImageView mainLogo;
    private ImageView customerLogo;
    private ImageView reservationLogo;
    private ImageView doorLogo;
    private ImageView attendLogo;
    private ImageView invoiceLogo;
    private ImageView welcome;


    //the border pane will be our main layout and the other layouts will also be added in it
    private HBox hBox = new HBox();
    protected BorderPane root = new BorderPane();
    Scene mainScreen = new Scene(root, 1300, 600);


    public StartMenu(){}
    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(mainScreen);
        /*passing the gridPane to the to the start
         * screen method to make a start interface for us*/
        startScreen(root);
        stage.setTitle("Management System");
        stage.show();

    }


    //this method will help in launching args from the Main class
    public void launching(String[] args){ launch(args);}

    public void startScreen(BorderPane startRoot) throws FileNotFoundException {

        GridPane startPane = new GridPane();
        startPane.getChildren().clear();
        startPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 10% 50%, #FFE4C4, #008B8B)");
        //padding the grid pane and styling it
        startPane.setPadding(new Insets(70, 10, 10, 300));
        startPane.setHgap(20);
        startPane.setVgap(15);

        hBox.getChildren().clear();
        GridPane gridInStack = new GridPane();
        hBox.getChildren().add(gridInStack);
        gridInStack.setPadding(new Insets(50, 50, 50, 100));
        gridInStack.setVgap(20);
        gridInStack.setHgap(20);
        StackPane stackPane = new StackPane();
        stackPane.setEffect(new DropShadow(10, Color.PURPLE));

        stackPane.setStyle("-fx-background-color: rgb(255, 255, 255)");
        stackPane.setPrefSize(900,700);
        stackPane.getChildren().add(hBox);
        startPane.add(stackPane, 0, 0);




        //making buttons for the main menu
        Button customer = new Button();
        Button reservation = new Button();
        Button roomsSetting = new Button();
        Button attendReservation = new Button();
        Button invoiceData = new Button();

        customer.setTranslateX(50);
        reservation.setTranslateX(50);
        roomsSetting.setTranslateX(50);
        attendReservation.setTranslateX(50);
        invoiceData.setTranslateX(50);

        //setting there text
        reservation.setText("Reservation");
        customer.setText("Customer");
        roomsSetting.setText("All Rooms");
        attendReservation.setText("Attend Reservation");
        invoiceData.setText("Invoice");
//
//        reservation.getStylesheets().add("sample/style.css");
//        customer.getStylesheets().add("sample/style.css");
//        roomsSetting.getStylesheets().add("sample/style.css");
//        attendReservation.getStylesheets().add("sample/style.css");
//        invoiceData.getStylesheets().add("sample/style.css");

        //setting there dimensions
        reservation.setPrefSize(300, 30 );
        customer.setPrefSize(300, 30);
        roomsSetting.setPrefSize(300, 30);
        attendReservation.setPrefSize(300, 30);
        invoiceData.setPrefSize(300, 30);

        //adding hotel logo
        logos();

        hBox.getChildren().add(welcome);
        welcome.setTranslateX(30);
        welcome.setTranslateY(270);

        gridInStack.add(mainLogo,  5, 0);
        //adding icons
        gridInStack.add(reservationLogo, 3, 1);
        gridInStack.add(customerLogo, 3, 2);
        gridInStack.add(doorLogo, 3, 3);
        gridInStack.add(attendLogo, 3, 4);
        gridInStack.add(invoiceLogo, 3, 5);
        //adding buttons
        gridInStack.add(reservation, 5, 1);
        gridInStack.add(customer, 5, 2);
        gridInStack.add(roomsSetting, 5, 3);
        gridInStack.add(attendReservation, 5, 4);
        gridInStack.add(invoiceData, 5, 5);

        //refreshing the pane and then setting it to the main pane
        startRoot.getChildren().clear();
        startRoot.setCenter(startPane);

        /*
        * making event handlers for the buttons these gonna wake our other classes
        * and we gonna pass our main layout pane to these classes to modify it
        * accordingly
        * */

        reservation.setOnAction(actionEvent -> {
            try {
                new Reservation(startRoot).mainInterface();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        customer.setOnAction(e -> {
            try {
                new Customer(startRoot).mainInterface();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        roomsSetting.setOnAction(e -> {
            try {
                new Rooms(startRoot).settingRooms();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        attendReservation.setOnAction(e -> {
            try {
                new Customer(startRoot).gettingInputToAttendAReservation();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        invoiceData.setOnAction(e -> new Invoice(startRoot).invoiceOptions());
    }

     /* Inserting logos for the
        Start Menu interface
     */

    public void logos() throws FileNotFoundException {
        Image logo = new Image(new FileInputStream("logos/hotellogo5.png"));
        mainLogo = new ImageView(logo);
        mainLogo.setFitHeight(200);
        mainLogo.setFitWidth(300);
        mainLogo.setTranslateX(50);

        Image logo1 = new Image(new FileInputStream("logos/reserve (1).png"));
        reservationLogo = new ImageView(logo1);
        reservationLogo.setFitHeight(40);
        reservationLogo.setFitWidth(40);

        Image logo2 = new Image(new FileInputStream("logos/reserve (3).png"));
        customerLogo = new ImageView(logo2);
        customerLogo.setFitHeight(40);
        customerLogo.setFitWidth(40);

        Image logo3 = new Image(new FileInputStream("logos/door.png"));
        doorLogo = new ImageView(logo3);
        doorLogo.setFitHeight(40);
        doorLogo.setFitWidth(40);

        Image logo4 = new Image(new FileInputStream("logos/attend.png"));
        attendLogo = new ImageView(logo4);
        attendLogo.setFitHeight(40);
        attendLogo.setFitWidth(40);

        Image logo5 = new Image(new FileInputStream("logos/invoiceMini.png"));
        invoiceLogo = new ImageView(logo5);
        invoiceLogo.setFitHeight(40);
        invoiceLogo.setFitWidth(40);

        Image logo6 = new Image(new FileInputStream("logos/hello.png"));
        welcome = new ImageView(logo6);
        welcome.setFitHeight(250);
        welcome.setFitWidth(250);

    }

}