
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

//refers to https://github.com/claraj/Java2545Examples/

public class MainGUI extends JFrame{

    //GUI Elements
    private JTextField searchTextField;
    private JButton searchButton;
    private JTable recordTable;
    private JButton newRecordButton;
    private JButton newCustomerButton;
    private JComboBox statusComboBox;
    private JButton exitButton;
    private JButton settingsButton;
    private JPanel mainPanel;
    private JButton editingButton;
    private JTextField priceChangeTextField;
    private JButton deleteButton;
    private String windowTitle = "Used Record Inventory Management";

    //Operation variables
    private InventoryDB db;

    private DefaultTableModel tableModel;
    private Vector columnNames;

    MainGUI(InventoryDB db) {
        this.db = db;

        this.setContentPane(mainPanel);
        pack();
        setTitle(windowTitle);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        configureTable();
        configureStatusComboBox();

        setupDeleteKey();

        setVisible(true);

        setLocationRelativeTo(null);



        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //
                //
                Settings changeSettings = new Settings(MainGUI.this);

            }
        });

        newCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewConsignorGUI newConsignorInput = new NewConsignorGUI(db,MainGUI.this);

            }
        });

        newRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewRecordGUI newRecordInput = new NewRecordGUI(db, MainGUI.this);
                updateTable();
            }
        });

        //searches all text in table
        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchTextField.getText();
                searchTable(searchString);
            }
        });

        //Search button duplicates the text field action listener function
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTable(searchTextField.getText());
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });
    }

    private void configureTable() {
        //Put in because IDK
        recordTable.setGridColor(Color.blue);

        //Enable Sorting
        recordTable.setAutoCreateRowSorter(true);

        //Get Table information
        columnNames = db.getColumnNames();
        Vector data = db.getRecords();

        //Create Table
        tableModel = new DefaultTableModel(data, columnNames);
        recordTable.setModel(tableModel);



    }

    private void searchTable(String searchText) {
        //Put in because IDK
        //recordTable.setGridColor(Color.blue);

        //Enable Sorting
        recordTable.setAutoCreateRowSorter(true);

        columnNames = db.getColumnNames();
        Vector data = db.searchRecords(searchText);

        tableModel = new DefaultTableModel(data, columnNames);
        recordTable.setModel(tableModel);

    }


    private void configureStatusComboBox() {
        for (String status : InventoryDB.statusList){
            statusComboBox.addItem(status);

        }


    }

    private void deleteSelectedRecord() {

        int currentRow = recordTable.getSelectedRow();

        if (currentRow == -1) {      // -1 means no row is selected. Display error message.
            JOptionPane.showMessageDialog(rootPane, "Please choose a record to delete");
        }

        else {
            // Get the ID of the selected record
            int id = (Integer) tableModel.getValueAt(currentRow, 7);
            db.deleteRecord(id);
            updateTable();
        }
    }

    private void updateTable() {

        Vector data = db.getRecords();
        tableModel.setDataVector(data, columnNames);

    }

    private void setupDeleteKey(){
        //based on https://stackoverflow.com/questions/6462842/how-to-remove-a-row-in-jtable-via-pressing-on-delete-on-the-keyboard solution 2
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = recordTable.getInputMap(condition);
        ActionMap actionMap = recordTable.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
        actionMap.put("Delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });
    }
}
