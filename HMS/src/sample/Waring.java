package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.awt.*;
import java.sql.SQLException;

interface Warning {





    default void warningStage(String info, String title){
        Toolkit.getDefaultToolkit().beep();//adding alert sound

        Stage stage =  new Stage();

         GridPane pane = new GridPane();

        pane.setPadding(new Insets(10, 20, 10, 20));
        pane.setVgap(20);

        Label status = new Label();
        status.setText(info);
        status.setTranslateX(25);

         Button warningButton = new Button();
        warningButton.setText("Ok");
        warningButton.setPrefSize(100, 8);
        warningButton.setTranslateX(70);

        pane.add(status, 0,  0);
        pane.add(warningButton, 0,  1);

        Scene scene = new Scene(pane, 400, 150);
        stage.setScene(scene);
        stage.setTitle(title);
        //this will make user to not interact with other windows
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();

        warningButton.setOnAction(e -> {
            scene.setRoot(new GridPane());
            stage.close();
        });
    }

    default void warning_R_Deletion(String info, String title, String button_1_text, String button_2_text){

        Toolkit.getDefaultToolkit().beep();//adding alert sound
        Stage stage =  new Stage();

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 20, 10, 20));
        pane.setVgap(20);

        Label status = new Label();
        status.setText(info);
        status.setTranslateX(25);

        Button button2 = new Button();

        Button warningButton = new Button();
        warningButton.setText(button_1_text);
        warningButton.setPrefSize(100, 8);
        warningButton.setTranslateX(70);
        button2.setText(button_2_text);
        button2.setPrefSize(100, 8);

        pane.add(status, 0,  0);
        pane.add(warningButton, 0,  1);
        pane.add(button2, 1, 1);

        Scene scene = new Scene(pane, 400, 150);
        stage.setScene(scene);
        stage.setTitle(title);
        //this will make user to not interact with other windows
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();

        warningButton.setOnAction(e -> {
            stage.close();
        });

        button2.setOnAction(e -> {
            stage.close();
            try {
                handle_R_Deletion();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    default void handle_R_Deletion() throws SQLException {
        //empty
    }


    default void warning_C_Deletion(String info, String title, String button_1_text, String button_2_text){
        Toolkit.getDefaultToolkit().beep();//adding alert sound
        Stage stage =  new Stage();

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 20, 10, 20));
        pane.setVgap(20);

        Label status = new Label();
        status.setText(info);
        status.setTranslateX(25);

        Button button2 = new Button();
        Button warningButton = new Button();
        warningButton.setText(button_1_text);
        warningButton.setPrefSize(100, 8);
        warningButton.setTranslateX(70);
        button2.setText(button_2_text);
        button2.setPrefSize(100, 8);

        Scene scene = new Scene(pane, 400, 150);
        stage.setScene(scene);
        stage.setTitle(title);
        //this will make user to not interact with other windows
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();


        warningButton.setOnAction(e -> {
            stage.close();
        });

        button2.setOnAction(e -> {
            stage.close();
            try {
                handle_C_Deletion();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        pane.add(button2, 1, 1);
        pane.add(status, 0,  0);
        pane.add(warningButton, 0,  1);
    }

    default void handle_C_Deletion() throws SQLException {
        //empty
    }


}
