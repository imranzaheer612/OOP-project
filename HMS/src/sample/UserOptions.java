package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public interface  UserOptions {

        void mainInterface() throws FileNotFoundException;
        void newEntry();
        void deleteEntryInterface();
        void modifyEntry();

}