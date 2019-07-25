import java.sql.*;


public class InventoryTesting {

    static Connection myConnect = null;
    static PreparedStatement myPreparedStat = null;

    static String inputArray[] = new String[9];

    public static void main(String[] args){

        try {
            log("------ Tut on making JDBC connection to MySQL DB ------");
            makeJDBCConnection();

            log("\n------ Adding data to DB ------");

            addDataToDB(inputArray);
            //addDataToDB( 987654321,"Item 2", 1);
            //addDataToDB( 123454321,"Item 3", 1);

            log("\n------ Get Data from DB ------");
            log("\n    UPC     Name   Amount");
            //getDataFromDB();

            log("\n------ Remove Data from DB ------");
            //removeDataFromDB(123456789);
            //removeDataFromDB(987654321);
            //removeDataFromDB(123454321);

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

        /*try{
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
        }*/
    }

    private static void addDataToDB( String inputArray[]) {

        //another option for batch insert
        //https://stackoverflow.com/questions/4355046/java-insert-multiple-rows-into-mysql-with-preparedstatement
        //https://stackoverflow.com/questions/12012592/jdbc-insert-multiple-rows

        /*try {
            String insertQueryStatement = "INSERT INTO invlist VALUES (?,?,?)";


            for (int i=0; i+2<inputArray.length; i=i+3) {
                myPreparedStat = myConnect.prepareStatement(insertQueryStatement);
                myPreparedStat.setInt(i+1, Integer.parseInt(inputArray[i]));
                myPreparedStat.setString(i+2, inputArray[i+1]);
                myPreparedStat.setInt(i+3, Integer.parseInt(inputArray[i+2]));

                //execute insert SQL statement
                myPreparedStat.executeUpdate();
                log(inputArray[i+1] + " added successfully.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        String insertQueryStatement = "INSERT INTO invlist VALUES (?,?,?)";

        for (int i=0; i+2<inputArray.length; i=i+3) {
            if (i>0){                                       //doesn't add to string on first run
                insertQueryStatement = insertQueryStatement.concat(", (?,?,?)");
            }

            //myPreparedStat setup goes here, same as above
            System.out.println(insertQueryStatement);

            log(inputArray[i+1] + " added successfully.");
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
