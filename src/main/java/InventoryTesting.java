import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class InventoryTesting {

    static Connection myConnect = null;
    static PreparedStatement myPreparedStat = null;

    static ArrayList<String> inputArray = new ArrayList<String>(Arrays.asList("1","test1","2","11","test 2","3"));

    public static void main(String[] args){

        //ask user for items to add
        //addInput();

        try {
            log("------ Tut on making JDBC connection to MySQL DB ------");
            makeJDBCConnection();

            while (true){
                System.out.println();
                String task = userInput();
                inputArray.clear();

                if(task.equals("add")){
                    log("\n------ Adding data to DB ------");
                    addDataToDB(inputArray=addInput());
                }

                else if(task.equals("delete")){
                    log("\n------ Remove Data from DB ------");
                    removeDataFromDB(inputArray=removeInput());
                }

                else if(task.equals("view")){
                    log("\n------ Get Data from DB ------");
                    log("\nUPC  Name  Amount");
                    getDataFromDB();
                }

                else if(task.equals("exit")){
                    break;
                }

                else{
                    System.out.println("Please enter a valid action.");
                }
            }






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

    private static void addDataToDB(ArrayList<String> inputArray) {

        //another option for batch insert
        //https://stackoverflow.com/questions/4355046/java-insert-multiple-rows-into-mysql-with-preparedstatement
        //https://stackoverflow.com/questions/12012592/jdbc-insert-multiple-rows

        try {
            String insertQueryStatement = "INSERT INTO invlist VALUES (?,?,?)";
            myPreparedStat = myConnect.prepareStatement(insertQueryStatement);

            for (int i=0; i+2<inputArray.size(); i=i+3) {
                myPreparedStat.setInt(1, Integer.parseInt(inputArray.get(i)));
                myPreparedStat.setString(2, inputArray.get(i+1));
                myPreparedStat.setInt(3, Integer.parseInt(inputArray.get(i+2)));

                //add to batch
                myPreparedStat.addBatch();
                log(inputArray.get(i+1) + " added to batch successfully.");
            }

            int[] inserted = myPreparedStat.executeBatch();
            log("Batch executed. "+ inserted.length + " records added to database.");


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

    private static void removeDataFromDB(ArrayList<String> inputArray) {
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

    //log utility
    private static void log(String string) {
        System.out.println(string);
    }

    private static String userInput(){
        Scanner s = new Scanner(System.in);
        System.out.println("What would you like to do? Add/Delete/View/Exit");
        return s.nextLine().toLowerCase();
    }

    private static ArrayList<String> addInput(){
        Scanner s = new Scanner(System.in);
        boolean moreItems=true;

        for (int i=0; moreItems; i++) {
            System.out.println("Enter UPC:");
            inputArray.add(String.valueOf(s.nextInt()));
            s.nextLine();
            System.out.println("Enter Item Name:");
            inputArray.add(s.nextLine());
            System.out.println("Enter amount:");
            inputArray.add(String.valueOf(s.nextInt()));
            s.nextLine();

            System.out.println("Do you have more items to add? y/n");
            if("n".equals(s.nextLine())){
                moreItems=false;
            }
        }

        return inputArray;
    }

    private static ArrayList<String> removeInput(){
        Scanner s = new Scanner(System.in);
        boolean moreItems=true;

        for (int i=0; moreItems; i++){
            System.out.println("Enter UPC:");
            inputArray.add(s.nextLine());

            System.out.println("Do you have more items to remove? y/n");
            if("n".equals(s.nextLine())){
                moreItems=false;
            }
        }

        return inputArray;
    }
}
