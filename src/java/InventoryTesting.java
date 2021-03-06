/*
Current main java back-end.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class InventoryTesting {

    static Connection myConnect = null;
    static PreparedStatement myPreparedStat = null;

    static ArrayList<String> inputArray = new ArrayList<String>();

    public static void main(String[] args){

        try {
            log("------ Making JDBC connection to MySQL DB ------");
            makeJDBCConnection();

            while (true){
                System.out.println();
                System.out.println("What would you like to do? Add/Change Amount/Delete UPC/View/Exit");
                String task = userInput();
                inputArray.clear();

                if(task.equals("add")){
                    log("\n------ Adding data to DB ------");
                    addDataToInvlistDB(inputArray=addInput());
                }

                else if(task.equals("change amount")||task.equals("change")||task.equals("amount")){

                    while (true) {
                        System.out.println("Do you want to add or remove items?");
                        task = userInput();

                        if(task.equals("remove")){
                            log("\n------ Remove amount from DB ------");
                            removeAmountFromInvlistDB(inputArray= amountInput());
                            break;
                        }
                        else if (task.equals("add")){
                            log("\n------ Adding amounts to DB ------");
                            addAmountInInvlistDB(inputArray = amountInput());
                            break;
                        }
                        else if(task.equals("menu")){
                            break;
                        }
                        else{
                            System.out.println("Please enter a valid option or enter menu to return to the main menu.");
                        }
                    }
                }

                else if(task.equals("delete")||task.equals("delete upc")){
                    log("\n------ Delete Data(by UPC) from DB ------");
                    removeUPCFromInvlistDB(inputArray=removeUPCInput());
                }

                else if(task.equals("view")){
                    log("\n------ Get Data from DB ------");
                    log("UPC  Name  Amount");
                    getDataFromInvlistDB();
                }

                else if(task.equals("exit")){
                    break;
                }

                else{
                    System.out.println("Please enter a valid action.");
                }
            }

            if (myPreparedStat != null) { //allows program to end properly still if nothing was ever created
                myPreparedStat.close();
            }
            if (myConnect != null) { //allows program to end properly still if nothing was ever created
                myConnect.close();
            }


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

    //add below this to database object

    private static void addDataToInvlistDB(ArrayList<String> inputArray) {

        try{
            String mergeQueryStatement = "INSERT INTO invlist VALUES (?,?,?) ON DUPLICATE KEY UPDATE amount = amount + ?";

            myPreparedStat = myConnect.prepareStatement(mergeQueryStatement);

            for (int i=0; i+2<inputArray.size(); i=i+3) {

                myPreparedStat.setInt(1, Integer.parseInt(inputArray.get(i)));
                myPreparedStat.setString(2, inputArray.get(i+1));
                myPreparedStat.setInt(3, Integer.parseInt(inputArray.get(i+2)));
                myPreparedStat.setInt(4, Integer.parseInt(inputArray.get(i+2)));

                //add to batch
                myPreparedStat.addBatch();
                log("UPC: " + inputArray.get(i) + " added to batch successfully.");
            }

            int[] inserted = myPreparedStat.executeBatch();
            log("Batch executed. "+ inserted.length + " records added to database.");

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void getDataFromInvlistDB() {
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

    private static void removeUPCFromInvlistDB(ArrayList<String> inputArray) {
        try{
            String getQueryStatement = "DELETE FROM invlist WHERE UPC=(?)";
            myPreparedStat = myConnect.prepareStatement(getQueryStatement);

            for (int i=0; i<inputArray.size(); i++) {
                myPreparedStat.setInt(1, Integer.parseInt(inputArray.get(i)));

                //add to batch
                myPreparedStat.addBatch();
                log(inputArray.get(i) + " added to batch successfully.");
            }

            int[] inserted = myPreparedStat.executeBatch();
            log("Batch executed. "+ inserted.length + " records removed from database.");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void removeAmountFromInvlistDB(ArrayList<String> inputArray) {
        subtractAmountInInvlistDB(inputArray);

        try{
            String getQueryStatement = "SELECT COUNT(amount) AS total FROM invlist WHERE amount<=?";
            myPreparedStat = myConnect.prepareStatement(getQueryStatement);
            myPreparedStat.setInt(1,0);

            ResultSet rs = myPreparedStat.executeQuery();
            rs.next();
            int count = rs.getInt("total");
            log(count + " records removed for having 0 quantity.");

        } catch(SQLException e){
            e.printStackTrace();
        }

        try{
            String getQueryStatement = "DELETE FROM invlist WHERE amount<=?";
            myPreparedStat = myConnect.prepareStatement(getQueryStatement);
            myPreparedStat.setInt(1,0);
            myPreparedStat.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void subtractAmountInInvlistDB(ArrayList<String> inputArray){
        try{
            String getQueryStatement = "UPDATE invlist SET amount=amount-? WHERE upc=?";
            myPreparedStat = myConnect.prepareStatement(getQueryStatement);

            for (int i=0; i+1<inputArray.size(); i++) {
                myPreparedStat.setInt(1, Integer.parseInt(inputArray.get(i+1)));
                myPreparedStat.setInt(2, Integer.parseInt(inputArray.get(i)));

                //add to batch
                myPreparedStat.addBatch();
                log(inputArray.get(i) + " added to batch successfully.");
            }

            int[] inserted = myPreparedStat.executeBatch();
            log("Batch executed. "+ inserted.length + " records changed in database.");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void addAmountInInvlistDB(ArrayList<String> inputArray){
        try{
            String getQueryStatement = "UPDATE invlist SET amount=amount+? WHERE upc=?";
            myPreparedStat = myConnect.prepareStatement(getQueryStatement);

            for (int i=0; i+1<inputArray.size(); i++) {
                myPreparedStat.setInt(1, Integer.parseInt(inputArray.get(i+1)));
                myPreparedStat.setInt(2, Integer.parseInt(inputArray.get(i)));

                //add to batch
                myPreparedStat.addBatch();
                log(inputArray.get(i) + " added to batch successfully.");
            }

            int[] inserted = myPreparedStat.executeBatch();
            log("Batch executed. "+ inserted.length + " records changed in database.");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //end add to database object*/

    private static void log(String string) {
        System.out.println(string);
    }

    private static String userInput(){
        Scanner s = new Scanner(System.in);
        return s.nextLine().toLowerCase();
    } //main menu options

    private static ArrayList<String> addInput(){
        Scanner s = new Scanner(System.in);

        while (true){
            System.out.println("Enter UPC:");
            if(s.hasNextInt()){
                inputArray.add(String.valueOf(s.nextInt()));
            }
            s.nextLine();
            System.out.println("Enter Item Name:");
            inputArray.add(s.nextLine());
            System.out.println("Enter amount:");
            if(s.hasNextInt()) {
                inputArray.add(String.valueOf(s.nextInt()));
            }
            s.nextLine();

            System.out.println("Do you have more items to add? y/n");
            if("n".equals(s.nextLine())){
                break;
            }
        }

        return inputArray;
    }

    private static ArrayList<String> amountInput(){
        Scanner s = new Scanner(System.in);

        while (true){
            System.out.println("Enter UPC:");
            if(s.hasNextInt()) {
                inputArray.add(String.valueOf(s.nextInt()));
            }
            s.nextLine();
            System.out.println("Enter amount:");
            if(s.hasNextInt()) {
                inputArray.add(String.valueOf(s.nextInt()));
            }
            s.nextLine();

            System.out.println("Do you have more items to change? y/n");
            if("n".equals(s.nextLine())){
                break;
            }
        }

        return inputArray;
    }

    private static ArrayList<String> removeUPCInput(){
        Scanner s = new Scanner(System.in);

        while (true){
            System.out.println("Enter UPC:");
            if(s.hasNextInt()) {
                inputArray.add(String.valueOf(s.nextInt()));
            }
            s.nextLine();

            System.out.println("Do you have more items to remove? y/n");
            if("n".equals(s.nextLine())){
                break;
            }
        }

        return inputArray;
    }
}
