import com.sun.source.tree.CaseTree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class Record {
    //Database connection string
    private static final String DB_CONNECTION_URL = "jdbc:sqlite:databases/RIMDB.sqlite";

    //Attributes of Record Class
    private static int iD;
    private static int status;
    private static Date created;
    private static Date updated;
    private static int price;
    private static String artist;
    private static String title;
    private static int consignorID;
    private static final String defaultStatus = "Standard";

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

    private static final String ADD_RECORD = "INSERT INTO Record (Price, Artist, Title, ConsignorID) VALUES (?, ?,?,?) ";


    Record(int iD, int status, Date created, Date updated, int price,String artist, String title, int consignorID ) {
        this.iD =iD;
        this.status = status;
        this.created = created;
        this.updated = updated;
        this.price = price;
        this.artist = artist;
        this.title = title;
        this.consignorID = consignorID;

    }

    public static String parsePrice (double price){
        //double priceDouble = Price /100;
        String priceString = String.format("$%.2f",price);
        return priceString;

    }





}
