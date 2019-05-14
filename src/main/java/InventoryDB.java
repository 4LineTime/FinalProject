import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

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

    //SQL statements
    //Table creation sql
    private static final String CREATE_CONSIGNOR_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)";
    private static final String CREATE_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER not null constraint Record_pk primary key autoincrement, " +
            "%s      TEXT  default 'STANDARD' not null, %s DATETIME default CURRENT_TIMESTAMP not null, %s DATETIME default CURRENT_TIMESTAMP not null, " +
            "%s       REAL  default 10.00 not null, %s TEXT  not null, %s  TEXT  not null, %s INTEGER  default 0 not null references %s)";
    //Queries
    private static final String GET_ALL_RECORDS = "SELECT * FROM Record JOIN Consignor WHERE ConsignorID = CONSIGNOR.ID;";
    private static final String GET_ALL_TEST = "SELECT * FROM Record";
    private static final String SEARCH_ALL_RECORDS = "SELECT * FROM Record INNER JOIN Consignor ON Record.ConsignorID = Consignor.ID WHERE ";


    InventoryDB() {createTables(); getRecords();createStatusMap();}

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
            int consignorid; //int

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




//                price = rs.getString(Record.parsePrice(RECORD_COL_PRICE));
//                status = rs.getString(Record.getStatus(RECORD_COL_STATUS));

                Vector v = new Vector();
                v.add(title); v.add(artist); v.add(priceString); v.add(status); v.add(name); v.add(contact);

                recordsVector.add(v);
            }

            return recordsVector;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Vector<Vector> searchRecords(String searchString){
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            //Concatenated search string
            String concatSearchString = SEARCH_ALL_RECORDS + RECORD_COL_TITLE+ " LIKE \'%" + searchString+ "%\' OR " + RECORD_COL_ARTIST+ " LIKE \'%" + searchString+"%\' OR "+ CONSIGNOR_COL_NAME + " LIKE \'%" + searchString+"%\' OR "+ RECORD_COL_STATUS+ " LIKE \'%" + searchString+"%\'";

            ResultSet rs = statement.executeQuery(concatSearchString);

            Vector<Vector> recordsVector = new Vector<>();

            String artist, title, name, contact,priceString, status, statusString;
            Date created, updated; //date
            double price;
            int consignorid; //int

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




//                price = rs.getString(Record.parsePrice(RECORD_COL_PRICE));
//                status = rs.getString(Record.getStatus(RECORD_COL_STATUS));

                Vector v = new Vector();
                v.add(title); v.add(artist); v.add(priceString); v.add(status); v.add(name); v.add(contact);

                recordsVector.add(v);
            }

            return recordsVector;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static String getStatus(Integer statusInt){
        return statusMap.get(statusInt);


    }

    public void createStatusMap(){
        int i =0;
        for(String status : statusList){
            statusMap.put(i,status);
            i++;

        }
    }





}


