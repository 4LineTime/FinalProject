import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewRecordGUI extends JFrame {
    private JTextField titleTextField;
    private JTextField priceTextField;
    private JComboBox consignorComboBox;
    private JButton cancelButton;
    private JButton submitButton;
    private JTextField artistTextField;
    private JPanel newRecordPanel;
    //private String windowTitle = "New Record";

    NewRecordGUI(final MainGUI parentComponent){

        setContentPane(newRecordPanel);
        pack();
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
                //TODO send record to database and refresh table in main gui
                //parentComponent.reloadTable();
                parentComponent.setEnabled(true);
                NewRecordGUI.this.dispose();

            }
        });


    }
}
