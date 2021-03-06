/*
hard coded test with adding, viewing, and removing items from database

obsolete code

use InventoryTesting.java
 */

import java.sql.*;


public class testing {

    static Connection myConnect = null;
    static PreparedStatement myPreparedStat = null;

    public static void main(String[] args){

        try {
            log("------ Tut on making JDBC connection to MySQL DB ------");
            makeJDBCConnection();

            log("\n------ Adding data to DB ------");

            addDataToDB( 123456789,"Item 1", 1);
            addDataToDB( 987654321,"Item 2", 1);
            addDataToDB( 123454321,"Item 3", 1);

            log("\n------ Get Data from DB ------");
            log("\n    UPC     Name   Amount");
            getDataFromDB();

            log("\n------ Remove Data from DB ------");
            removeDataFromDB(123456789);
            removeDataFromDB(987654321);
            removeDataFromDB(123454321);

            myPreparedStat.close();
            myConnect.close();


        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static void makeJDBCConnection() {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            log("Congrats - MySQL JDBC Driver Registered");

        } catch (ClassNotFoundException e) {
            log("Did not find JDBC driver.");
            e.printStackTrace();
            return;
        }

        try{
            //DriverManager: basic service for managing a set of JDBC drivers.
            myConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory?useSSL=false", "code", "code");
            if (myConnect != null) {
                log("Connection Successful.");
            } else{
                log("Failed to connect.");
            }

        } catch (SQLException e) {
            log("MySQL Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    private static void addDataToDB( int upc, String itemName, int amount) {

        try {
            String insertQueryStatement = "INSERT INTO invlist VALUES (?,?,?)";

            myPreparedStat = myConnect.prepareStatement(insertQueryStatement);
            myPreparedStat.setInt(1, upc);
            myPreparedStat.setString(2, itemName);
            myPreparedStat.setInt(3, amount);

            //execute insert SQL statement
            myPreparedStat.executeUpdate();
            log(itemName + " added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getDataFromDB() {
        try{
            String getQueryStatement = "SELECT * FROM invlist";

            myPreparedStat = myConnect.prepareStatement(getQueryStatement);

            //Execute query, get java ResultSet
            ResultSet rs = myPreparedStat.executeQuery();

            //Iterate through java ResultSet
            while (rs.next()) {
                int upc =rs.getInt("UPC");
                String itemName = rs.getString("Name");
                int amount = rs.getInt("amount");

                //Print results
                System.out.format("%s, %s, %s\n", upc, itemName, amount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void removeDataFromDB(int upc) {
        try{
                String getQueryStatement = "DELETE FROM invlist WHERE UPC=" + upc;

                myPreparedStat = myConnect.prepareStatement(getQueryStatement);

                myPreparedStat.executeUpdate();
            log(upc + " removed successfully.");

            } catch (SQLException e){
                e.printStackTrace();
            }
    }

    //log utility
    private static void log(String string) {
        System.out.println(string);
    }

    /* while "things to add"
    ask for upc (int)
    ask for name (string)
    ask for amount (int)

    addDataToDB();

    ask if more items (y/n)
     */

    /* while "things to add"
    ask for upc (int)
    ask for name (string)
    ask for amount (int)

    ask if more items (y/n)

    add all items to DB at once
     */
}
