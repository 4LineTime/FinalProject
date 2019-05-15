import java.sql.*;

public class ConsignorDB {
    //Database connection string
    private static final String DB_CONNECTION_URL = "jdbc:sqlite:databases/RIMDB.sqlite";

    //Strings to reference consignor table
    private static final String CONSIGNOR_TABLE_NAME = "Consignor";
    private static final String CONSIGNOR_COL_NAME = "Name";
    private static final String CONSIGNOR_COL_CONTACT = "Contact";
    private static final String CONSIGNOR_COL_ID = "ID";

    //SQL to Create Consignor table
    private static final String ADD_CONSIGNOR = "INSERT INTO Consignor (Name, Contact) VALUES (?, ?) ";

    ConsignorDB() {}

    //Add consignor to database
    public void addConsignor(String name, String contact) {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_CONSIGNOR)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, contact);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
