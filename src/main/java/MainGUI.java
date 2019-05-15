
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private String priceChangerText = "Change Price";

    //Operation variables
    private InventoryDB db;

    private DefaultTableModel tableModel;
    private Vector columnNames;

    //static variables for button that enables and disables table editing
    private static boolean tableEditable = true;
    private static String editableTrueButtonText = "Finish Edit";
    private static String editableFalseButtonText = "Edit Table ";
    private static String editableTrueWindowText = " - Editing Enabled";
    private static String editableFalseWindowText = " - Editing Disabled";


    MainGUI(InventoryDB db) {
        this.db = db;

        this.setContentPane(mainPanel);
        pack();
        setTitle(windowTitle);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        configureTable(); //Propagate table UI by accessing the database and then turning that into a vector then using vector to fill table view
        configureStatusComboBox(); // Propagate combo box using a list set in InventoryDB
        enableDisableEditing(); //Doesn't currently work. Is supposed to automatically enable or disable certain columns' editability, as well as act as a save function, and prevent user errors.

        setupDeleteKey();//Doesn't work. Is supposed to allow pressing the delete keyboard key to delete a row.

        setVisible(true);

        setLocationRelativeTo(null);



        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //UI Element completed but does not interact with the settings table or business logic layer.
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //
                //
                Settings changeSettings = new Settings(MainGUI.this);

            }
        });

        //New window to add consignor to Consignor table
        newCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewConsignorGUI newConsignorInput = new NewConsignorGUI(db,MainGUI.this);

            }
        });

        //New window to add record to Record table
        newRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewRecordGUI newRecordInput = new NewRecordGUI(db, MainGUI.this);
                updateTable();
            }
        });




        //Search button duplicates the text field action listener function
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTable(searchTextField.getText());
            }
        });

        //searches all text in table within each column that is text datatype
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTable(searchTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTable(searchTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTable(searchTextField.getText());
            }
        });

        //Deletes Row
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });

        //Does not work. Is meant to enable or disable the editibility of the table, as well as function as a save.
        editingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableDisableEditing();
            }
        });
        //Change sale status of record row
        statusComboBox.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedRecordStatus();
                updateTable();
            }
        }));


        //Change price of row
        priceChangeTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateSelectedRecordPrice();
                updateTable();
            }
        });


        //Remove label when editing, and add back in when done.
        priceChangeTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                priceChangeTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                priceChangeTextField.setText(priceChangerText);
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
            int id = (Integer) tableModel.getValueAt(currentRow, 6);
            db.deleteRecord(id);
            updateTable();
        }
    }

    private void updateSelectedRecordStatus(){
        int currentRow = recordTable.getSelectedRow();
        String status = statusComboBox.getSelectedItem().toString(); //Needs to be converted to string or is seen as Object datatype

        if (currentRow == -1) {      // -1 means no row is selected. Display error message.
            JOptionPane.showMessageDialog(rootPane, "Please choose a record to update");
        } else {
            int id  = (Integer) tableModel.getValueAt(currentRow, 6);
            db.updateStatus(status,id);
            updateTable();
        }
    }

    private void updateSelectedRecordPrice(){
        int currentRow = recordTable.getSelectedRow();
        try {
            if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                JOptionPane.showMessageDialog(rootPane, "Please choose a record to update");
            } else if (priceChangeTextField.getText().isBlank()) { //Text field is blank
                JOptionPane.showMessageDialog(rootPane, "Please enter a valid price.");
            } else {

                Double price = Double.parseDouble(priceChangeTextField.getText());

                int id = (Integer) tableModel.getValueAt(currentRow, 6);
                db.updatePrice(price, id);
                updateTable();

            }
        } catch(NumberFormatException nfe) {JOptionPane.showMessageDialog(rootPane,"Please enter a valid price.");} //If invalid or unparseable data, or if null.

    }


    public void updateTable() {

        Vector data = db.getRecords();
        tableModel.setDataVector(data, columnNames);

    }

    //Not functioning at this time. To enable keyboard key delete to delete a row.
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

    //Does not yet work. Is meant to enable and disable editability of table, as well as act as a save function.
    private void enableDisableEditing(){
        if (tableEditable){ //boolean set as private static
            tableEditable = false;
            JOptionPane.showMessageDialog(MainGUI.this,"Table Not Editable");
            editingButton.setText(editableFalseButtonText);
            setTitle(windowTitle + editableFalseWindowText);

        } else {
            tableEditable = true;
            JOptionPane.showMessageDialog(MainGUI.this,"Table Is Editable");
            editingButton.setText(editableTrueButtonText);
            setTitle(windowTitle + editableTrueWindowText);

        }

    }
}
