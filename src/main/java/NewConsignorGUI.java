import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewConsignorGUI extends JFrame {
    private JTextField nameTextField;
    private JTextField contactTextField;
    private JButton cancelButton;
    private JButton submitButton;
    private JPanel newConsignorPanel;

    private ConsignorDB cdb = new ConsignorDB();

    NewConsignorGUI(InventoryDB db, final MainGUI parentComponent){
        setContentPane(newConsignorPanel);
        pack();
        setVisible(true);
        parentComponent.setEnabled(false);




        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentComponent.setEnabled(true);
                NewConsignorGUI.this.dispose();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewConsignor();
                parentComponent.updateTable();
                parentComponent.setEnabled(true);
                NewConsignorGUI.this.dispose();

            }
        });


    }
    private void addNewConsignor(){
        try {
            String name = nameTextField.getText();
            String contact = contactTextField.getText();
            cdb.addConsignor(name, contact);
        } catch (Exception e) {JOptionPane.showMessageDialog(NewConsignorGUI.this, "Error: Please Enter the Correct Data\n"+e);}

    }
}
