import javax.swing.*;
import java.awt.event.*;


public class Settings extends JFrame{
    private JPanel settingsPanel;
    private JTextField bargainPriceTextField;
    private JTextField bargainDaysTextField;
    private JTextField percentageTextField;
    private JButton submitButton;
    private JButton cancelButton;

    Settings(final MainGUI parentComponent) {
        setContentPane(settingsPanel);
        pack();
        setVisible(true);
        parentComponent.setEnabled(false);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO call method to submit settings to database
                parentComponent.setEnabled(true);
                Settings.this.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentComponent.setEnabled(true);
                Settings.this.dispose();
            }
        });

    }

}
