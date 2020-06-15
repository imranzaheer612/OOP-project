package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Rooms extends StartMenu{

    private int tot_SingleRooms;
    private int tot_DoubleRooms;
    private int tot_TripleRooms;

    private String singleRoomRepresent = "A";
    private String doubleRoomRepresent = "B";
    private String tripleRoomRepresent = "C";

//    private int tot_SingleRooms;
//    private int tot_DoubleRooms;
//    private int tot_TripleRooms;

    Spinner<Integer> singleRoomSpinner = new Spinner<Integer>();
    Spinner<Integer> doubleRoomSpinner = new Spinner<Integer>();
    Spinner<Integer> tripleRoomSpinner = new Spinner<Integer>();

    private final Button enterButton = new Button("Submit");
    private final Button backButton = new Button("Back");

    private Connection connection = DatabaseConnection.connector();


    private GridPane gridInStack = new GridPane();
    private GridPane roomsPane = new GridPane();
    private BorderPane rootPane =  new BorderPane();
    private VBox sidePanel  =  new VBox();

    Rooms(){
        settingInitialValues();
    }

    Rooms(BorderPane root){
        rootPane = root;
        rootPane.getChildren().clear();
        rootPane.setCenter(roomsPane);
        sidePanel.setPrefWidth(400);
        sidePanel.setPadding(new Insets(100, 20, 50, 50));
        sidePanel.setSpacing(30);
        rootPane.setLeft(sidePanel);
        sidePanel.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 10% 50%, rgba(0,255,255,0.26), rgba(0,63,139,0.45))");
        roomsPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 10% 50%, #FFE4C4, #008B8B)");
        roomsPane.setPadding(new Insets(150, 100, 50, 200));
        roomsPane.setVgap(20);
        roomsPane.setHgap(40);


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
        roomsPane.add(stackPane, 0, 0);

    }

    /*
    * initializing a single seat rooms array
    * if the user want to give a single seat room
    * then this array will be retuned
    * */
    public String[] singleRooms(){
        String[] singleRooms = new String[tot_SingleRooms];
        for(int i = 0; i < tot_SingleRooms; i++ ){
            singleRooms[i] = (i+1) + singleRoomRepresent;
        }
        return singleRooms;
    }
    /*
     * initializing a double seat rooms array
     * if the user want to give a double seat room
     * then this array will be retuned
     * */
    public String[] doubleRooms(){
        String[] doubleRooms = new String[tot_DoubleRooms];
        for(int i = 0; i < tot_DoubleRooms; i++ ){
            doubleRooms[i] = (i+1) + doubleRoomRepresent;
        }
        return doubleRooms;
    }

    /*
     * initializing a triple seat rooms array
     * if the user want to give a triple seat room
     * then this array will be retuned
     * */
    public String[] tripleRooms(){
        String[] tripleRooms = new String[tot_TripleRooms];
        for(int i = 0; i < tot_TripleRooms; i++ ){
            tripleRooms[i] = (i+1) + tripleRoomRepresent;
        }
        return tripleRooms;
    }


    /*
     * we gonna make the UI for setting the rooms
     * for example if we want to add some extra rooms in our
     * we gonna add it from here
     * */

    public void userInterface() throws FileNotFoundException {
        Image logo9 = new Image(new FileInputStream("logos/mechanic.png"));
        ImageView mechanic = new ImageView(logo9);
        mechanic.setFitHeight(200);
        mechanic.setFitWidth(200);
        mechanic.setTranslateX(20);

        Label welcomeInfo = new Label();
        welcomeInfo.setText("Welcome to the Rooms center\n" +
                "You can set total rooms numbers here");
        welcomeInfo.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));

        sidePanel.getChildren().add(mechanic);
        sidePanel.getChildren().add(welcomeInfo);


        Label single_L = new Label("Single Seat Rooms");
        Label double_L = new Label("Double Seat Rooms");
        Label triple_L = new Label("Triple Seat Rooms");

        single_L.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));
        double_L.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));
        triple_L.setFont(Font.font("Montserrat", FontWeight.BOLD, 15));

        //making spinners and setting there value factory
        singleRoomSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, tot_SingleRooms));
        doubleRoomSpinner.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, tot_DoubleRooms));
        tripleRoomSpinner.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, tot_TripleRooms));

        //adding labels to the pane
        gridInStack.add(single_L, 0, 0);
        gridInStack.add(double_L, 0, 1);
        gridInStack.add(triple_L, 0, 2);

        //adding spinners to the pane
        gridInStack.add(singleRoomSpinner, 1, 0);
        gridInStack.add(doubleRoomSpinner, 1, 1);
        gridInStack.add(tripleRoomSpinner, 1, 2);
        gridInStack.add(enterButton, 0, 4);
        gridInStack.add(backButton, 2, 5);
        }

    /*
    Setting Number of rooms available in the hotel
     */
    public  void settingRooms() throws FileNotFoundException {
        settingInitialValues();
        userInterface();
        enterButton.setOnAction(e -> {
            updatingDataBase();
            new Reservation().warningStage("Rooms data have been successfully updated", "Status");
        });
        backButton.setOnAction(e -> {
            try {
                new StartMenu().startScreen(rootPane);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }

    //updating values in data base
    public void updatingDataBase(){
        PreparedStatement stmt;
        String query = "UPDATE totRooms SET " +
                "singleRooms = ?, " +
                "doubleRooms = ?, " +
                "tripleRooms = ?";
//                "DESC FETCH FIRST ROWS ONLY";

        try{
            stmt = connection.prepareStatement(query);
            stmt.setString(1, singleRoomSpinner.getValue().toString());
            stmt.setString(2, doubleRoomSpinner.getValue().toString());
            stmt.setString(3, tripleRoomSpinner.getValue().toString());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    * setting the initial values of the text field
    * these initials values are actually the total rooms available in the database
    * */
    public void settingInitialValues(){
        PreparedStatement stmt;
        String query = "SELECT * FROM totRooms";
        try{
            stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                tot_SingleRooms = Integer.parseInt(rs.getString("singleRooms"));
                tot_DoubleRooms = Integer.parseInt(rs.getString("doubleRooms"));
                tot_TripleRooms = Integer.parseInt(rs.getString("tripleRooms"));
                stmt.close();
                rs.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}


