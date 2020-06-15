package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //database connectivity occurs here

    public static Connection connector(){
        try {
            Connection conct = DriverManager.getConnection("jdbc:sqlite:managementSystem.db");

            return conct;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
