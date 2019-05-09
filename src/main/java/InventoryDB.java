import java.sql.*;
import java.util.Vector;

public class InventoryDB {

    //Database connection string
    private static final String DB_CONNECTION_URL = "jdbc.sqlite:rimdb.sqlite";

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

    InventoryDB() {createTables();}

    private void createTables(){
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL);
            Statement statement = conn.createStatement()) {

            //Create table for consignors
            String createConsignorTableSQLTemplate = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)";
            String createConsignorTableSQL = String.format(createConsignorTableSQLTemplate,CONSIGNOR_TABLE_NAME,CONSIGNOR_COL_ID,CONSIGNOR_COL_NAME,CONSIGNOR_COL_CONTACT);

            statement.executeUpdate(createConsignorTableSQL);


            //create table for records
            String createRecordTableSQLTemplate = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER not null constraint Record_pk primary key autoincrement, " +
                    "%s      INTEGER  default 0 not null, %s DATETIME default CURRENT_TIMESTAMP not null, %s DATETIME default CURRENT_TIMESTAMP not null, " +
                    "%s       INTEGER  default 1000 not null, %s TEXT  not null, %s  TEXT  not null, %s INTEGER  default 0 not null references %s)";
            String createRecordTableSQL = String.format(createRecordTableSQLTemplate,RECORD_TABLE_NAME,RECORD_COL_ID,RECORD_COL_STATUS,RECORD_COL_CREATED,RECORD_COL_UPDATED, RECORD_COL_PRICE, RECORD_COL_ARTIST, RECORD_COL_TITLE,CONSIGNOR_COL_ID, CONSIGNOR_TABLE_NAME);

            statement.executeUpdate(createRecordTableSQL);



        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }






}


