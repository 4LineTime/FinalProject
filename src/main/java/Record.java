import com.sun.source.tree.CaseTree;

import java.util.Date;
import java.util.HashMap;

public class Record {

    private static int iD;
    private static int status;
    private static Date created;
    private static Date updated;
    private static int price;
    private static String artist;
    private static String title;
    private static int consignorID;


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
