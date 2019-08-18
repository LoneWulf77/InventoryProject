/*
Potential code to create a database object for each database being used

not currently implemented, needs to be commented out for other code to compile and run
 */
/*
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
    private String database;

    public Database(String database) {
        this.database = database;
    }

    private static void addDataToDB(ArrayList<String> inputArray) {

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

    private static void removeUPCFromDB(ArrayList<String> inputArray) {
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

    private static void removeAmountFromDB(ArrayList<String> inputArray) {
        subtractAmountInDB(inputArray);

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

    private static void subtractAmountInDB(ArrayList<String> inputArray){
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

    private static void addAmountInDB(ArrayList<String> inputArray){
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
}
*/