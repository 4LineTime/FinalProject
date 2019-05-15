import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;



public class NewRecordGUI extends JFrame {
    private JTextField titleTextField;
    private JTextField priceTextField;
    private JComboBox consignorComboBox;
    private JButton cancelButton;
    private JButton submitButton;
    private JTextField artistTextField;
    private JPanel newRecordPanel;
    //private String windowTitle = "New Record";
    private InventoryDB db = new InventoryDB();
    private LinkedHashMap consignorsMap;


    NewRecordGUI(InventoryDB db, final MainGUI parentComponent){

        setContentPane(newRecordPanel);
        pack();
        configureConsignorComboBox(db);


        setVisible(true);
        parentComponent.setEnabled(false);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentComponent.setEnabled(true);
                NewRecordGUI.this.dispose();
        }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecord();
                parentComponent.updateTable();
                parentComponent.setEnabled(true);
                NewRecordGUI.this.dispose();

            }
        });


    }

    private void configureConsignorComboBox(InventoryDB db) {
        consignorsMap = db.getConsignors();
        consignorsMap.forEach((k,v) -> consignorComboBox.addItem(k));
    }

    private void addRecord(){
        try {
            //Convert price string to double data type
            double price = Double.parseDouble(priceTextField.getText());

            //Get Consignor unique ID and send it to addRecord function
            int comboBoxIndex = consignorComboBox.getSelectedIndex(); //Gets index of selected UI element

            ArrayList<Integer> result = new ArrayList<>();//Declare array list to hold indexes of hashmap to iterate over
            result.addAll(consignorsMap.values());

            int consignorID = result.get(comboBoxIndex);//
            String artist = artistTextField.getText();
            String title = titleTextField.getText();


            db.addToRecordDB(price, artist, title,consignorID);
        } catch(Exception ex) {JOptionPane.showMessageDialog(NewRecordGUI.this, "Error: Please enter the correct data\n "+ ex);}
    }

}
