import java.sql.*;
import java.util.*;
import java.util.Date;

public class InventoryDB {

    public static HashMap<Integer, String> statusMap = new HashMap<>();
    public static final String[] statusList = {"Standard", "Sold", "Bargain Bin", "Return to Customer", "Donate" };


    //Database connection string
    private static final String DB_CONNECTION_URL = "jdbc:sqlite:databases/RIMDB.sqlite";

    //Strings to reference consignor table
    private static final String CONSIGNOR_TABLE_NAME = "Consignor";
    private static final String CONSIGNOR_COL_NAME = "Name";
    private static final String CONSIGNOR_COL_CONTACT = "Contact";
    private static final String CONSIGNOR_COL_ID = "ID";


    //Strings to reference record table
    private static final String RECORD_TABLE_NAME = "Record";
    private static final String RECORD_COL_TITLE = "Title";
    private static final String RECORD_COL_ARTIST = "Artist";
    private static final String RECORD_COL_PRICE = "Price";
    private static final String RECORD_COL_CREATED = "Created";
    private static final String RECORD_COL_UPDATED = "Updated";
    private static final String RECORD_COL_CONSIGNOR = "ConsignorID";
    private static final String RECORD_COL_STATUS = "Status";
    private static final String RECORD_COL_ID = "ID";

    static final String OK = "Ok";

    private static final String defaultStatus = "Standard";


    //SQL statements
    //Table creation sql
    private static final String CREATE_CONSIGNOR_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)";
    private static final String CREATE_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER not null constraint Record_pk primary key autoincrement, " +
            "%s      TEXT  default 'STANDARD' not null, %s DATETIME default CURRENT_TIMESTAMP not null, %s DATETIME default CURRENT_TIMESTAMP not null, " +
            "%s       REAL  default 10.00 not null, %s TEXT  not null, %s  TEXT  not null, %s INTEGER  default 0 not null references %s)";
    //Queries
    private static final String GET_ALL_RECORDS = "SELECT * FROM Record JOIN Consignor WHERE ConsignorID = CONSIGNOR.ID;";
    private static final String GET_ALL_CONSIGNORS = "SELECT * FROM Consignor;";
    private static final String GET_ALL_TEST = "SELECT * FROM Record";
    private static final String SEARCH_ALL_RECORDS = "SELECT * FROM Record INNER JOIN Consignor ON Record.ConsignorID = Consignor.ID WHERE ";
    private static final String DELETE_RECORD = "DELETE FROM Record WHERE ID = ?";
    private static final String ADD_RECORD = "INSERT INTO Record (Price, Artist, Title, ConsignorID) VALUES (?, ?,?,?) ";

    //private static final String concatSearchString = SEARCH_ALL_RECORDS + RECORD_COL_TITLE + " LIKE \'%" + "?"+ "%\' OR " + RECORD_COL_ARTIST+ " LIKE \'%?%\' OR "+ CONSIGNOR_COL_NAME + " LIKE \'%?%\' OR "+ RECORD_COL_STATUS+ " LIKE \'% ? %\'";
    private static final String concatSearchString = SEARCH_ALL_RECORDS + RECORD_COL_TITLE + " LIKE ? OR " + RECORD_COL_ARTIST+ " LIKE ? OR "+ CONSIGNOR_COL_NAME + " LIKE ? OR "+ RECORD_COL_STATUS+ " LIKE ?";

            InventoryDB() {createTables(); getRecords();}

    private void createTables(){

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL);
            Statement statement = conn.createStatement()) {

            //Create table for consignors
            String createConsignorTableSQLTemplate = CREATE_CONSIGNOR_TABLE;
            String createConsignorTableSQL = String.format(createConsignorTableSQLTemplate,CONSIGNOR_TABLE_NAME,CONSIGNOR_COL_ID,CONSIGNOR_COL_NAME,CONSIGNOR_COL_CONTACT);

            statement.executeUpdate(createConsignorTableSQL);


            //create table for records
            String createRecordTableSQLTemplate = CREATE_RECORDS_TABLE;
            String createRecordTableSQL = String.format(createRecordTableSQLTemplate,RECORD_TABLE_NAME,RECORD_COL_ID,RECORD_COL_STATUS,RECORD_COL_CREATED,RECORD_COL_UPDATED, RECORD_COL_PRICE, RECORD_COL_ARTIST, RECORD_COL_TITLE,RECORD_COL_CONSIGNOR, CONSIGNOR_TABLE_NAME);

            statement.executeUpdate(createRecordTableSQL);



        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }


    }

    Vector getColumnNames() {

        Vector columnNames = new Vector();

        columnNames.add(RECORD_COL_TITLE);
        columnNames.add(RECORD_COL_ARTIST);
        columnNames.add(RECORD_COL_PRICE);
        columnNames.add(RECORD_COL_STATUS);
        columnNames.add(CONSIGNOR_COL_NAME);
        columnNames.add(CONSIGNOR_COL_CONTACT);
        columnNames.add(RECORD_COL_ID);
        columnNames.add(RECORD_COL_CONSIGNOR);

//        columnNames.add("Title");
//        columnNames.add("Artist");
//        columnNames.add("Price");
//        columnNames.add("Status");
//        columnNames.add("Consignor");
//        columnNames.add("Contact Information");


        return columnNames;
    }

    Vector<Vector> getRecords(){
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_RECORDS);

            Vector<Vector> recordsVector = new Vector<>();

            String artist, title, name, contact,priceString,status, statusString;
            Date created, updated; //date
            double price;
            int recordID, consignorID; //int

            while (rs.next()) {


                artist = rs.getString(RECORD_COL_ARTIST);
                title = rs.getString(RECORD_COL_TITLE);

                //Get price as integer and convert it to a string
                price = rs.getDouble(RECORD_COL_PRICE);
                priceString = Record.parsePrice(price);

                //Get status of record
                status = rs.getString(RECORD_COL_STATUS);
                //statusString = getStatus(status);

                name = rs.getString(CONSIGNOR_COL_NAME);
                contact = rs.getString(CONSIGNOR_COL_CONTACT);

                recordID = rs.getInt(RECORD_COL_ID);
                consignorID = rs.getInt(RECORD_COL_CONSIGNOR);




//                price = rs.getString(Record.parsePrice(RECORD_COL_PRICE));
//                status = rs.getString(Record.getStatus(RECORD_COL_STATUS));

                Vector v = new Vector();
                v.add(title); v.add(artist); v.add(priceString); v.add(status); v.add(name); v.add(contact); v.add(recordID); v.add(consignorID);

                recordsVector.add(v);
            }

            return recordsVector;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Vector<Vector> searchRecords(String searchString){
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedSearch = connection.prepareStatement(concatSearchString)) {
            String newSearchString = "\'%"+searchString+"%\'";

            preparedSearch.setString(1, newSearchString);
            preparedSearch.setString(2, newSearchString);
            preparedSearch.setString(3, newSearchString);
            preparedSearch.setString(4, newSearchString);
            //statement.executeUpdate();

            ResultSet rs = preparedSearch.executeQuery();

            Vector<Vector> recordsVector = new Vector<>();

            String artist, title, name, contact,priceString, status;
            //Date created, updated; //date
            double price;
            int recordID, consignorID; //int

            while (rs.next()) {


                artist = rs.getString(RECORD_COL_ARTIST);
                title = rs.getString(RECORD_COL_TITLE);

                //Get price as integer and convert it to a string
                price = rs.getDouble(RECORD_COL_PRICE);
                priceString = Record.parsePrice(price);

                //Get status as integer and convert it to a string
                status = rs.getString(RECORD_COL_STATUS);
                //statusString = getStatus(status);

                name = rs.getString(CONSIGNOR_COL_NAME);
                contact = rs.getString(CONSIGNOR_COL_CONTACT);

                recordID = rs.getInt(RECORD_COL_ID);
                consignorID = rs.getInt(RECORD_COL_CONSIGNOR);


//                price = rs.getString(Record.parsePrice(RECORD_COL_PRICE));
//                status = rs.getString(Record.getStatus(RECORD_COL_STATUS));

                Vector v = new Vector();
                v.add(title); v.add(artist); v.add(priceString); v.add(status); v.add(name); v.add(contact); v.add(recordID); v.add(consignorID);;

                recordsVector.add(v);
            }

            return recordsVector;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    //Deletes record from database
    public void deleteRecord(Integer recordID) {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RECORD)) {

            preparedStatement.setInt(1, recordID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    LinkedHashMap getConsignors(){
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_CONSIGNORS);

            int consignorID;
            String name, contact;

            LinkedHashMap consignorsMap = new LinkedHashMap();

            while(rs.next()) {
                consignorID = rs.getInt(CONSIGNOR_COL_ID);
                name = rs.getString(CONSIGNOR_COL_NAME);
                //contact = rs.getString(CONSIGNOR_COL_CONTACT); //not used currently

                consignorsMap.put(name,consignorID);


            }


            return consignorsMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        }

    public void addToRecordDB(Double price, String artist, String title, int consignorID) {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_RECORD)) {


            preparedStatement.setDouble(1, price);
            preparedStatement.setString(2, artist);
            preparedStatement.setString(3, title);
            preparedStatement.setInt(4, consignorID);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





}


