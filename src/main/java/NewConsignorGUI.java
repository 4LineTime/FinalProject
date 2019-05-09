import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewConsignorGUI extends JFrame {
    private JTextField nameTextField;
    private JTextField contactTextField;
    private JButton cancelButton;
    private JButton submitButton;
    private JPanel newConsignorPanel;

    NewConsignorGUI(final MainGUI parentComponent){
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
                parentComponent.setEnabled(true);
                NewConsignorGUI.this.dispose();

            }
        });
    }
}
