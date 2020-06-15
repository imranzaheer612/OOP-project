package sample;

import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;

class DatabaseDeletion {

    Connection connection = DatabaseConnection.connector();
    /*
     * this will move the room which
     * user want to delete to the deleted Rooms table
     * then it will be deleted
     * the main logic of moving the data to an other table is that we also have to make a table
     * that will have the data og the deleted users
     * */
    public void movingRecordToOtherTable(String targetTable, String selectedTable, String targetField, TextField entryText){
        PreparedStatement stmt;
        String query = "INSERT INTO " + targetTable +
                " SELECT * " +
                "FROM "+ selectedTable + " WHERE " + targetField + " = "+ entryText.getText();
        try {
            stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Over loading the method
    public void movingRecordToOtherTable(String targetTable, String selectedTable, String targetField, String targetFieldValue){
        PreparedStatement stmt;
        String query = "INSERT INTO " + targetTable +
                " SELECT * " +
                "FROM "+ selectedTable + " WHERE " + targetField + " = "+ targetFieldValue;
        System.out.println("target field value : " + targetFieldValue);
        try {
            stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
     * after the room is successfully moved from one database table
     * --> to an other database table we can simple remove the record from
     * the previous table
     * */

    public void deletingRecord(String tableName, String targetField, TextField entryText){
        PreparedStatement stmt;
        String query = "DELETE FROM " + tableName + " WHERE " + targetField + " = " + entryText.getText();
        try {
            stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //over Loading
    public void deletingRecord(String tableName, String targetField, String targetFieldValue){
        PreparedStatement stmt;
        String query = "DELETE FROM " + tableName + " WHERE " + targetField + " = " + targetFieldValue;
        try {
            stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
